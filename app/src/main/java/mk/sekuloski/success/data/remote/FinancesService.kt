package mk.sekuloski.success.data.remote

import io.ktor.client.*
import io.ktor.client.engine.android.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.plugins.cookies.*
import io.ktor.client.plugins.logging.*
import io.ktor.http.*
import io.ktor.serialization.kotlinx.json.*
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonArray
import mk.sekuloski.success.data.remote.dto.*

interface FinancesService {

    suspend fun getPayments(ids: JsonArray): List<Payment>

    suspend fun getSubscriptions(ids: JsonArray): ArrayList<Subscription>

    suspend fun getMonths(): ArrayList<Month>

    suspend fun getLocations(): ArrayList<Location>

    suspend fun getMainInfo(): FinancesMain?

    suspend fun addPayment(paymentRequest: PaymentRequest): String

    companion object {
        fun create(): FinancesService {
            return FinancesServiceImpl(
                client = HttpClient(Android) {
                    install(Logging) {
                        level = LogLevel.ALL
                    }
                    install(ContentNegotiation) {
                        json(Json {
                            prettyPrint = true
                            isLenient = true
                        })
                    }
                    install(HttpCookies) {
                        storage = ConstantCookiesStorage(Cookie(name = "sekuloski-was-here", value = "true", domain = "finances.sekuloski.mk"))
                    }
                }
            )
        }
    }
}