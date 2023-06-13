package mk.sekuloski.success.data.remote.dto.workouts

data class Workout (
    var name: String,
    var exercises: List<Exercise>,
    var days: List<Int>, // [0, 2, 4] Monday, Wednesday, Friday
    )