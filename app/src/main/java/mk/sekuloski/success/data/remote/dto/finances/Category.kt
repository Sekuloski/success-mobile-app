package mk.sekuloski.success.data.remote.dto.finances

import kotlinx.serialization.Serializable

@Serializable
data class Category(
    val id: Int,
    val name: String,
    val limit: Int,
)
