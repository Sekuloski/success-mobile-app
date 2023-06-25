package mk.sekuloski.success.data.remote.services.workouts

import io.ktor.client.*
import io.ktor.client.engine.android.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.plugins.cookies.*
import io.ktor.client.plugins.logging.*
import io.ktor.http.*
import io.ktor.serialization.kotlinx.json.*
import kotlinx.serialization.json.Json
import mk.sekuloski.success.data.remote.dto.workouts.Exercise
import mk.sekuloski.success.data.remote.dto.workouts.Workout
import mk.sekuloski.success.data.remote.dto.workouts.WorkoutExecution

interface WorkoutsService {

    suspend fun getExercises(): List<Exercise>

    suspend fun getWorkouts(): List<Workout>

    suspend fun getWorkoutStatus(): Boolean

    suspend fun getWorkoutHistory(): List<WorkoutExecution>

    suspend fun updateExercise(id: Int, set: String): String

    suspend fun updateWorkout(id: Int): String

    companion object {
        fun create(): WorkoutsService {
            return WorkoutsServiceImpl(
                client = HttpClient(Android) {
                    install(Logging) {
                        level = LogLevel.ALL
                    }
                    install(ContentNegotiation) {
                        json(Json {
                            prettyPrint = true
                            isLenient = true
                            ignoreUnknownKeys = true
                        })
                    }
                    install(HttpCookies) {
                        storage = ConstantCookiesStorage(Cookie(name = "sekuloski-was-here", value = "true", domain = "workouts.sekuloski.mk"))
                    }
                }
            )
        }
    }
}