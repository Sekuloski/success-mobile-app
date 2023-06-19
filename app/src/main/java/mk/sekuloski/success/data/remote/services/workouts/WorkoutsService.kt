package mk.sekuloski.success.data.remote.services.workouts

import io.ktor.client.*
import io.ktor.client.engine.android.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.plugins.cookies.*
import io.ktor.client.plugins.logging.*
import io.ktor.http.*
import io.ktor.serialization.kotlinx.json.*
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonArray
import mk.sekuloski.success.data.remote.dto.finances.FinancesMain
import mk.sekuloski.success.data.remote.dto.finances.Location
import mk.sekuloski.success.data.remote.dto.finances.Month
import mk.sekuloski.success.data.remote.dto.finances.Payment
import mk.sekuloski.success.data.remote.dto.finances.PaymentRequest
import mk.sekuloski.success.data.remote.dto.finances.Subscription
import mk.sekuloski.success.data.remote.dto.workouts.Exercise
import mk.sekuloski.success.data.remote.dto.workouts.Workout

interface WorkoutsService {

    suspend fun getExercises(): List<Exercise>

    suspend fun getWorkouts(): List<Workout>

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