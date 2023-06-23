package mk.sekuloski.success.data.remote.services.workouts

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.plugins.ClientRequestException
import io.ktor.client.plugins.RedirectResponseException
import io.ktor.client.plugins.ServerResponseException
import io.ktor.client.request.get
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.client.request.url
import io.ktor.http.ContentType
import io.ktor.http.contentType
import mk.sekuloski.success.data.remote.dto.workouts.Exercise
import mk.sekuloski.success.data.remote.dto.workouts.Workout
import mk.sekuloski.success.data.remote.dto.workouts.WorkoutExecution

class WorkoutsServiceImpl(
    private val client: HttpClient

) : WorkoutsService {
    override suspend fun getExercises(): List<Exercise> {
        return try {
            client.get {
                url(WorkoutsApiRoutes.EXERCISES)
            }.body()
        } catch(e: RedirectResponseException) {
            // 3xx - responses
            println("Error: ${e.response.status.description}")
            emptyList()
        } catch(e: ClientRequestException) {
            // 4xx - responses
            println("Error: ${e.response.status.description}")
            emptyList()
        } catch(e: ServerResponseException) {
            // 5xx - responses
            println("Error: ${e.response.status.description}")
            emptyList()
        } catch(e: Exception) {
            println("Error: ${e.message}")
            emptyList()
        }
    }

    override suspend fun getWorkouts(): List<Workout> {
        return try {
            client.get {
                url(WorkoutsApiRoutes.WORKOUTS)
            }.body()
        } catch(e: RedirectResponseException) {
            // 3xx - responses
            println("Error: ${e.response.status.description}")
            emptyList()
        } catch(e: ClientRequestException) {
            // 4xx - responses
            println("Error: ${e.response.status.description}")
            emptyList()
        } catch(e: ServerResponseException) {
            // 5xx - responses
            println("Error: ${e.response.status.description}")
            emptyList()
        } catch(e: Exception) {
            println("Error: ${e.message}")
            emptyList()
        }
    }

    override suspend fun getWorkoutStatus(): Boolean {
        return try {
            val results = client.get {
                url(WorkoutsApiRoutes.WORKOUT_STATUS)
            }.body() as HashMap<String, Any>

            results["message"] as Boolean
        } catch(e: RedirectResponseException) {
            // 3xx - responses
            println("Error: ${e.response.status.description}")
            false
        } catch(e: ClientRequestException) {
            // 4xx - responses
            println("Error: ${e.response.status.description}")
            false
        } catch(e: ServerResponseException) {
            // 5xx - responses
            println("Error: ${e.response.status.description}")
            false
        } catch(e: Exception) {
            println("Error: ${e.message}")
            false
        }
    }

    override suspend fun getWorkoutHistory(): List<WorkoutExecution> {
        return try {
            client.get {
                url(WorkoutsApiRoutes.WORKOUT_HISTORY)
            }.body()
        } catch(e: RedirectResponseException) {
            // 3xx - responses
            println("Error: ${e.response.status.description}")
            emptyList()
        } catch(e: ClientRequestException) {
            // 4xx - responses
            println("Error: ${e.response.status.description}")
            emptyList()
        } catch(e: ServerResponseException) {
            // 5xx - responses
            println("Error: ${e.response.status.description}")
            emptyList()
        } catch(e: Exception) {
            println("Error: ${e.message}")
            emptyList()
        }
    }

    override suspend fun updateExercise(id: Int, set: String): String {
        return try {
            val body = HashMap<String, String>()
            body["id"] = id.toString()
            body["set"] = set
            client.post {
                url(WorkoutsApiRoutes.UPDATE_EXERCISE)
                contentType(ContentType.Application.Json)
                setBody(body)
            }.body()
        } catch(e: RedirectResponseException) {
            // 3xx - responses
            println("Error: ${e.response.status.description}")
            e.message
        } catch(e: ClientRequestException) {
            // 4xx - responses
            println("Error: ${e.response.status.description}")
            e.message
        } catch(e: ServerResponseException) {
            // 5xx - responses
            println("Error: ${e.response.status.description}")
            e.message
        } catch(e: Exception) {
            println("Error: ${e.message}")
            e.message.toString()
        }
    }

    override suspend fun updateWorkout(id: Int): String {
        return try {
            val body = HashMap<String, String>()
            body["id"] = id.toString()
            client.post {
                url(WorkoutsApiRoutes.UPDATE_WORKOUT)
                contentType(ContentType.Application.Json)
                setBody(body)
            }.body()
        } catch(e: RedirectResponseException) {
            // 3xx - responses
            println("Error: ${e.response.status.description}")
            e.message
        } catch(e: ClientRequestException) {
            // 4xx - responses
            println("Error: ${e.response.status.description}")
            e.message
        } catch(e: ServerResponseException) {
            // 5xx - responses
            println("Error: ${e.response.status.description}")
            e.message
        } catch(e: Exception) {
            println("Error: ${e.message}")
            e.message.toString()
        }
    }
}