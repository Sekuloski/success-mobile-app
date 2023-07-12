package mk.sekuloski.success.finances.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.serialization.Serializable

@Serializable
@Entity
data class MonthEntity(
    @PrimaryKey val id: Int,
    var name: String,
    val left: Int,
    val expenses: Int,
    )