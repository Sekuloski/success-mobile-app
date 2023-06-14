package mk.sekuloski.success.data.remote.services

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

interface FinancesService {

    suspend fun getPayments(ids: JsonArray): List<Payment>

    suspend fun getSubscriptions(ids: JsonArray): ArrayList<Subscription>

    suspend fun getMonths(months: Int = 12, offset: Int = 0): ArrayList<Month>

    suspend fun getLocations(): ArrayList<Location>

    suspend fun getMainInfo(): FinancesMain?

    suspend fun getSalaryInfo(): Boolean

    suspend fun addPayment(paymentRequest: PaymentRequest): String

    suspend fun addSalary(water: Int, power: Int): String

    suspend fun deletePayment(id: Int, cash: Boolean): String

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
                            ignoreUnknownKeys = true
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