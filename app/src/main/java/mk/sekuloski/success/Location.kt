package mk.sekuloski.success

import java.time.LocalDateTime

class Location {
    var id: Int = 0
    lateinit var name: String
    lateinit var coordinates: String

    constructor(id: Int, name: String, coordinates: String) {
        this.id = id
        this.name = name
        this.coordinates = coordinates
    }

    constructor()
}