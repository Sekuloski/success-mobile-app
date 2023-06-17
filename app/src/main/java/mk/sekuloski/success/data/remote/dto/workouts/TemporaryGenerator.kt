package mk.sekuloski.success.data.remote.dto.workouts

fun getWorkouts(): List<Workout>
{
    val ringPullUps = Exercise("Ring Pull Ups", 5, 0, listOf(10, 9, 9, 8, 7), rest_time = 180)
    val weightedPullUps = Exercise("20kg Pull Ups", 5, 20, listOf(2, 1, 1, 1, 1), rest_time = 180, previous_progression = ringPullUps)
    val ringPushUps = Exercise("Ring Push Ups", 5, 0, listOf(20, 18, 17, 16, 15), rest_time = 180)
    val weightedPushUps = Exercise("20kg Push Ups", 5, 20, listOf(10, 8, 7), rest_time = 180, previous_progression = ringPushUps)
    val weightedDips = Exercise("20kg Dips", 5, 20, listOf(10, 8, 7), rest_time = 180)
    val dragonFlagRaises = Exercise("Dragon Flags", 5, 0, listOf(10, 8, 7), rest_time = 180)
    val lSitHang = Exercise("L Sit Hang", 5, 0, listOf(10, 8, 7), rest_time = 180)
    val squats = Exercise("Squats", 5, 20, listOf(10, 8, 7), rest_time = 180)
    val pistolSquats = Exercise("Pistol Squats", 5, 20, listOf(10, 8, 7), rest_time = 180)
    ringPullUps.next_progression = weightedPullUps
    ringPushUps.next_progression = weightedPushUps

    val workout0 = Workout("Weighted Full Body", listOf(weightedPullUps, weightedDips, lSitHang, squats), listOf(0, 2, 4))
    val workout1 = Workout("Weighted Full Body", listOf(weightedPullUps, weightedPushUps, dragonFlagRaises, squats), listOf(0, 2, 4))
    val workout2 = Workout("Body Weight on Rings", listOf(ringPullUps, ringPushUps, lSitHang, pistolSquats), listOf(1, 3, 5))
    val workout3 = Workout("Full Body 3", listOf(ringPullUps, weightedPullUps, ringPushUps, weightedPushUps), listOf(0, 2, 4))
    val workout4 = Workout("Full Body 4", listOf(ringPullUps, weightedPullUps, ringPushUps, weightedPushUps), listOf(1, 3, 5))

    return listOf(workout0, workout1, workout2, workout3, workout4)
}