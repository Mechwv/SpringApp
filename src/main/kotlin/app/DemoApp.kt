package app

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.data.annotation.Id
import org.springframework.data.jdbc.repository.query.Query
import org.springframework.data.relational.core.mapping.Table
import org.springframework.data.repository.CrudRepository
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
class PlaceService(val db: PlaceRepository) {

    fun findPlaces(): List<Place> = db.findPlaces()

    fun post(place: Place){
        db.save(place)
    }
}

interface PlaceRepository : CrudRepository<Place, String> {

    @Query("select * from places")
    fun findPlaces(): List<Place>
}

@Table("PLACES")
data class Place(
    @Id
    val id: String?,
    val latitude: Double,
    val longtitude: Double,
    val place_name: String,
    val description: String?,
)
