package mk.sekuloski.success.data.remote.dto.workouts

import kotlinx.serialization.Serializable

@Serializable
data class Exercise (
    var id: Int,
    var name: String,
    var exercise_type: Int,
    var weights: Int = 0, // KG
    var goal: Int,
    var rest: Int, // Seconds
    var best_set: String,
    var last_set: String, // [6, 5, 5, 4, 3]
    var image_url: String? = "",
    var previous_progression: Int? = -1,
    var next_progression: Int? = -1,
    )
