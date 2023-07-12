package mk.sekuloski.success.finances.data.local

import kotlinx.serialization.Serializable
import androidx.room.Entity
import androidx.room.PrimaryKey

@Serializable
@Entity
data class CategoryEntity(
    @PrimaryKey val id: Int,
    val name: String,
    val limit: Int,
)
