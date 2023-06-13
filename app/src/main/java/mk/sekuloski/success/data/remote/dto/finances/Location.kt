package mk.sekuloski.success.data.remote.dto.finances

import kotlinx.serialization.Serializable

@Serializable
data class Location(
    var id: Int,
    var name: String,
    var coordinates: String
    )