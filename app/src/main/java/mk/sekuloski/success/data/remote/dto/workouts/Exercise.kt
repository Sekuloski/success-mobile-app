package mk.sekuloski.success.data.remote.dto.workouts

data class Exercise (
    val name: String,
    val sets: Int = 0,
    val best_reps: List<Int>,
    val previous_reps: List<Int>, // [6, 5, 5, 4, 3]
    val rest_time: Int, // Seconds
    val previous_progression: Exercise? = null,
    val next_progression: Exercise? = null,
    val image_url: String = "",
    val muscle_group: String = "",
    val skill: Exercise? = null
    )