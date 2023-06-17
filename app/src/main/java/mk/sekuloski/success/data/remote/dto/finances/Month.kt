package mk.sekuloski.success.data.remote.dto.finances

import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonArray

@Serializable
data class Month(
    var id: Int,
    var name: String,
    val left: Int,
    val expenses: Int,
    )