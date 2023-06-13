package mk.sekuloski.success.data.remote.dto.workouts

fun getWorkouts(): List<Workout>
{
    val pullUp = Exercise("Pull Up", 5, 0, listOf(10, 9, 9, 8, 7), rest_time = 180)
    val weightedPullUp = Exercise("10kg Weighted Pull Up", 5, 10, listOf(2, 1, 1, 1, 1), rest_time = 180, previous_progression = pullUp)
    val pushUp = Exercise("Push Up", 5, 0, listOf(20, 18, 17, 16, 15), rest_time = 180)
    val weightedPushUp = Exercise("10kg Weighted Push Up", 5, 10, listOf(10, 8, 7), rest_time = 180, previous_progression = pushUp)
    pullUp.next_progression = weightedPullUp
    pushUp.next_progression = weightedPushUp

    val workout1 = Workout(listOf(pullUp, weightedPullUp, pushUp, weightedPushUp), listOf(0, 2, 4))
    val workout2 = Workout(listOf(pullUp, weightedPullUp, pushUp, weightedPushUp), listOf(1, 3, 5))

    return listOf(workout1, workout2)
}