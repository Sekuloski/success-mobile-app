package mk.sekuloski.success.workouts.data.remote

import android.os.Build

object WorkoutsApiRoutes {
    private const val REAL_BASE_URL = "https://workouts.sekuloski.mk"
    private const val DEV_BASE_URL = "http://10.0.2.2:9000"
    private val isRunningOnEmulator: Boolean = Build.HARDWARE.contains("ranchu")
    private val BASE_URL = if (isRunningOnEmulator) DEV_BASE_URL else REAL_BASE_URL
//    private val BASE_URL = if (isRunningOnEmulator) REAL_BASE_URL else DEV_BASE_URL
    val WORKOUTS = "$BASE_URL/workouts"
    val UPDATE_WORKOUT = "$BASE_URL/workouts/update"
    val WORKOUT_HISTORY = "$BASE_URL/workouts/history"
    val WORKOUT_STATUS = "$BASE_URL/workouts/today"
    val EXERCISES = "$BASE_URL/exercises"
    val UPDATE_EXERCISE = "$BASE_URL/exercises/update"
}