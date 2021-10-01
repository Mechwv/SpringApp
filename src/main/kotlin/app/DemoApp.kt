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
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController

@SpringBootApplication
class DemoApp

fun main(args: Array<String>) {
    runApplication<DemoApp>(*args)
}

@RestController
class PlaceResource(val service: PlaceService) {

    @GetMapping
    fun index(): List<Place> = service.findPlaces()

    @PostMapping
    fun post(@RequestBody place: Place) {
        service.post(place)
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
           place.id,
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
