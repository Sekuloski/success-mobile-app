package mk.sekuloski.success.workouts.data.local

import androidx.room.Entity
import kotlinx.serialization.Serializable

@Serializable
@Entity
data class Workout (
    var id: Int,
    var name: String,
    var exercises: List<Exercise>,
    var days: String, // "0,2,4" Monday, Wednesday, Friday
    )