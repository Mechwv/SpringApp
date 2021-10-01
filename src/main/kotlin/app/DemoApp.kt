package app

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.data.annotation.Id
import org.springframework.data.jdbc.repository.query.Query
import org.springframework.data.relational.core.mapping.Table
import org.springframework.data.repository.CrudRepository
import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.jdbc.core.RowMapper
import org.springframework.jdbc.core.query
import org.springframework.stereotype.Service
import org.springframework.web.bind.annotation.*
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
    fun index(): List<Place> = service.findPlaces()

    @GetMapping("/{id}")
    fun index(@PathVariable id: String): List<Place> = service.findPlacesById(id)

    @PostMapping
    fun post(@RequestBody place: Place) {
        try {
            service.post(place)
        } catch (e: Exception) {
            println("{$e}")
        }

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

    fun findPlacesById(id: String): List<Place> =
            db.query("select * from places where id = ?", id) { rs, _ ->
            Place(rs.getString("id"), rs.getDouble("latitude"), rs.getDouble("longtitude"),
                rs.getString("place_name"), rs.getString("description"))}


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
