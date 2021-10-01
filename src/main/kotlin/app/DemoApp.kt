package app

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.data.annotation.Id
import org.springframework.data.jdbc.repository.query.Query
import org.springframework.data.relational.core.mapping.Table
import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Service
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController

@SpringBootApplication
class DemoApp

fun main(args: Array<String>) {
    runApplication<DemoApp>(*args)
}

@RestController
class PlaceResource(service: PlaceService) {

    @GetMapping
    fun index(): List<Place> = listOf(
        Place(0,123.0, 234.0, "Blue octopus bar", null),
        Place(0,256.5673567, 322.456345, "White octopus bar", "cool place")
    )
}

@Service
class PlaceService(val db: MessageRepository) {

    fun findPlaces(): List<Place> {
        TODO()
    }

    fun post(place: Place){
        TODO()
    }
}

interface MessageRepository : CrudRepository<Place, String> {

    @Query("select * from places")
    fun findPlaces(): List<Place>
}

@Table("PLACES")
data class Place(
    @Id
    val id: Int,
    val latitude: Double,
    val longtitude: Double,
    val name: String,
    val description: String?,
)
