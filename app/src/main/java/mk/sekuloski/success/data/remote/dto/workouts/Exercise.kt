package mk.sekuloski.success.data.remote.dto.workouts

data class Exercise (
    var name: String,
    var sets: Int = 0,
    var weights: Int = 0, // KG
    var best_reps: List<Int>,
    var previous_reps: List<Int> = best_reps, // [6, 5, 5, 4, 3]
    var rest_time: Int, // Seconds
    var previous_progression: Exercise? = null,
    var next_progression: Exercise? = null,
    var image_url: String = "",
    var muscle_group: String = "",
    var skill: Exercise? = null
    )