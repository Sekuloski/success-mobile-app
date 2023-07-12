package mk.sekuloski.success.workouts.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.serialization.Serializable
import mk.sekuloski.success.finances.data.local.DateSerializer
import java.time.ZonedDateTime

@Serializable
@Entity
data class WorkoutExecution (
    @PrimaryKey val id: Int,
    val workout_id: String,
    @Serializable(with = DateSerializer::class)
    val date: ZonedDateTime,
)
