package mk.sekuloski.success.data.remote.dto.workouts

import kotlinx.serialization.Serializable

@Serializable
data class Workout (
    var id: Int,
    var name: String,
    var exercises: List<Exercise>,
    var days: String, // "0,2,4" Monday, Wednesday, Friday
    )