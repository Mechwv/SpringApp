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

    @GetMapping("/hello")
    fun hello(): String = "Hello"

    @GetMapping
    fun getPlaces(): List<Place> = service.findPlaces()

    @GetMapping("/{id}")
    fun index(@PathVariable id: String): Place? =
        service.findPlaceById(id) ?: throw ResponseStatusException(HttpStatus.NOT_FOUND, "Place not found")

    @PostMapping
    fun post(@RequestBody place: Place) {
        try {
            service.post(place)
        } catch (e: Exception) {
            println("{$e}")
        }
    }

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