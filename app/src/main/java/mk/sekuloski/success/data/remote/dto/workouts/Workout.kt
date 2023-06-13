package mk.sekuloski.success.data.remote.dto.workouts

data class Workout (
    val exercises: List<Exercise>,
    val days: List<Int>, // [0, 2, 4] Monday, Wednesday, Friday
    )