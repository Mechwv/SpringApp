package app

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.http.HttpStatus
import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.jdbc.core.query
import org.springframework.stereotype.Service
import org.springframework.web.bind.annotation.*
import org.springframework.web.server.ResponseStatusException
import uuid
import java.lang.Exception

@SpringBootApplication
class DemoApp

fun main(args: Array<String>) {
    runApplication<DemoApp>(*args)
}

@RestController
class PlaceResource(val service: PlaceService) {

    /**
     * Метод для получения строки “Hello” при отправке get запроса на http://localhost:8080/hello
     */
    @GetMapping("/hello")
    fun hello(): String = "Hello"

    /**
     * Метод для получения списка всех объектов типа Place из БД при отправке get запроса на http://localhost:8080/
     */
    @GetMapping
    fun getPlaces(): List<Place> = service.findPlaces()

    /**
     * Метод для получения одного объекта типа Place из БД при отправке get запроса на http://localhost:8080/{id},
     * где id - идентификатор сущности в таблице places
     */
    @GetMapping("/{id}")
    fun index(@PathVariable id: String): Place? =
        service.findPlaceById(id) ?: throw ResponseStatusException(HttpStatus.NOT_FOUND, "Place not found")

    /**
     * Метод для добавления одного объекта типа Place в БД при отправке post запроса на http://localhost:8080/,
     * post запрос должен содержать сущность Place
     */
    @PostMapping
    fun post(@RequestBody place: Place) {
        try {
            service.post(place)
        } catch (e: Exception) {
            println("{$e}")
        }
    }

    /**
     * Метод для удаления одного объекта типа Place из БД по id при отправке post запроса на http://localhost:8080/delete/{id},
     * где id - идентификатор сущности в таблице places
     */
    @PostMapping("/delete/{id}")
    fun delete(@PathVariable id: String) {
        service.deletePlaceById(id)
    }
}

@Service
class PlaceService(val db: JdbcTemplate) {

    fun findPlaces(): List<Place> = db.query("select * from places") { rs, _ ->
        Place(
            rs.getString("id"),
            rs.getDouble("latitude"),
            rs.getDouble("longtitude"),
            rs.getString("place_name"),
            rs.getString("description")
        )
    }

    fun findPlaceById(id: String): Place? =
            db.query("select * from places where id = ?", id) { rs, _ ->
            Place(rs.getString("id"), rs.getDouble("latitude"), rs.getDouble("longtitude"),
                rs.getString("place_name"), rs.getString("description"))}.firstOrNull()

    fun deletePlaceById(id: String) {
        db.execute("delete from places where id = '$id'")
    }

    fun post(place: Place){
       db.update("insert into places values (?, ?, ?, ?, ?)",
           place.id ?: place.place_name.uuid(),
           place.latitude,
           place.longtitude,
           place.place_name,
           place.description)
    }
}


data class Place(
    val id: String?,
    val latitude: Double,
    val longtitude: Double,
    val place_name: String,
    val description: String?,
)