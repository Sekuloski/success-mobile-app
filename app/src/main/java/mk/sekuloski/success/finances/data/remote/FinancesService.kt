package mk.sekuloski.success.finances.data.remote

import io.ktor.client.*
import io.ktor.client.engine.android.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.plugins.cookies.*
import io.ktor.client.plugins.logging.*
import io.ktor.http.*
import io.ktor.serialization.kotlinx.json.*
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonArray
import mk.sekuloski.success.finances.domain.model.Category
import mk.sekuloski.success.finances.domain.model.FinancesMain
import mk.sekuloski.success.finances.domain.model.Location
import mk.sekuloski.success.finances.domain.model.Month
import mk.sekuloski.success.finances.domain.model.Payment
import mk.sekuloski.success.finances.data.local.PaymentRequest
import mk.sekuloski.success.finances.domain.model.Subscription

interface FinancesService {

    suspend fun getPayments(ids: JsonArray): List<Payment>

    suspend fun getMonthPayments(month: Int = -1, year: Int = -1): List<Payment>

    suspend fun getSubscriptions(ids: JsonArray? = null): ArrayList<Subscription>

    suspend fun getMonths(months: Int = 12, offset: Int = 0): ArrayList<Month>

    suspend fun getLocations(): ArrayList<Location>

    suspend fun getMainInfo(): FinancesMain

    suspend fun getSalaryInfo(): Boolean

    suspend fun addPayment(paymentRequest: PaymentRequest): String

    suspend fun addSalary(water: Int, power: Int): String

    suspend fun payPayments(ids: List<Int>): String

    suspend fun deletePayment(id: Int, cash: Boolean): String

    suspend fun deleteMonthlyPayment(name: String): String
    suspend fun getCategories(): List<Category>

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