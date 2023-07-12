package mk.sekuloski.success.finances.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.serialization.Serializable

@Serializable
@Entity
data class LocationEntity(
    @PrimaryKey val id: Int,
    var name: String,
    var coordinates: String
    )