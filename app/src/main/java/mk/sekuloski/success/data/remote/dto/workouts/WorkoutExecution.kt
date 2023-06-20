package mk.sekuloski.success.data.remote.dto.workouts

import kotlinx.serialization.Serializable
import mk.sekuloski.success.data.remote.dto.finances.DateSerializer
import java.time.ZonedDateTime

@Serializable
data class WorkoutExecution (
    val id: Int,
    val workout_id: Int,
    @Serializable(with = DateSerializer::class)
    val date: ZonedDateTime,
)
