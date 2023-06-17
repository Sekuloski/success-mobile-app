package mk.sekuloski.success.data.remote.services

import android.util.Log
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.plugins.*
import io.ktor.client.request.*
import io.ktor.client.statement.bodyAsText
import io.ktor.http.*
import kotlinx.serialization.json.JsonArray
import kotlinx.serialization.json.JsonObject
import mk.sekuloski.success.data.remote.dto.finances.FinancesMain
import mk.sekuloski.success.data.remote.dto.finances.Location
import mk.sekuloski.success.data.remote.dto.finances.Month
import mk.sekuloski.success.data.remote.dto.finances.Payment
import mk.sekuloski.success.data.remote.dto.finances.PaymentRequest
import mk.sekuloski.success.data.remote.dto.finances.Subscription

class FinancesServiceImpl(
    private val client: HttpClient
    
) : FinancesService {

    override suspend fun getPayments(ids: JsonArray): List<Payment> {
        return try {
            val body = HashMap<String, JsonArray>()
            body["ids"] = ids

            client.post {
                url(FinanceApiRoutes.PAYMENTS)
                contentType(ContentType.Application.Json)
                setBody(JsonObject(body))
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

    override suspend fun getMonthPayments(month: Int, year: Int): List<Payment> {
        return try {
            val body = HashMap<String, Int>()
            if (month != -1)
            {
                body["month"] = month
            }
            if (year != -1)
            {
                body["year"] = year
            }

            client.post {
                url(FinanceApiRoutes.MONTH_PAYMENTS)
                contentType(ContentType.Application.Json)
                if (body.isNotEmpty()) setBody(body)
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

    override suspend fun getSubscriptions(ids: JsonArray?): ArrayList<Subscription> {
        return try {
            val body = HashMap<String, JsonArray>()
            val response: ArrayList<Subscription>
            if (!ids.isNullOrEmpty())
            {
                body["ids"] = ids
                response = client.post {
                    url(FinanceApiRoutes.SUBSCRIPTIONS)
                    contentType(ContentType.Application.Json)
                    setBody(JsonObject(body))
                }.body()
            }
            else
            {
                response = client.get {
                    url(FinanceApiRoutes.SUBSCRIPTIONS)
                    contentType(ContentType.Application.Json)
                }.body()
            }
            response

        } catch(e: RedirectResponseException) {
            // 3xx - responses
            Log.e("Finances Service", "Error: ${e.response.status.description}")
            ArrayList()
        } catch(e: ClientRequestException) {
            // 4xx - responses
            Log.e("Finances Service", "Error: ${e.response.status.description}")
            ArrayList()
        } catch(e: ServerResponseException) {
            // 5xx - responses
            Log.e("Finances Service", "Error: ${e.response.status.description}")
            ArrayList()
        } catch(e: Exception) {
            Log.e("Finances Service", "Error: ${e.message}")
            ArrayList()
        }
    }

    override suspend fun getMonths(months: Int, offset: Int): ArrayList<Month> {
        val body = HashMap<String, Int>()
        body["months"] = months
        body["offset"] = offset

        return try {
            client.post {
                url(FinanceApiRoutes.MONTHS)
                contentType(ContentType.Application.Json)
                setBody(body)
            }.body()
        } catch(e: RedirectResponseException) {
            // 3xx - responses
            println("Error: ${e.response.status.description}")
            ArrayList()
        } catch(e: ClientRequestException) {
            // 4xx - responses
            println("Error: ${e.response.status.description}")
            ArrayList()
        } catch(e: ServerResponseException) {
            // 5xx - responses
            println("Error: ${e.response.status.description}")
            ArrayList()
        } catch(e: Exception) {
            println("Error: ${e.message}")
            ArrayList()
        }
    }

    override suspend fun getMainInfo(): FinancesMain? {
        return try {
            client.get {
                url(FinanceApiRoutes.MAIN)
            }.body()
        } catch(e: RedirectResponseException) {
            // 3xx - responses
            println("Error: ${e.response.status.description}")
            println("Error Message: ${e.message}")
            null
        } catch(e: ClientRequestException) {
            // 4xx - responses
            println("Error: ${e.response.status.description}")
            println("Error Message: ${e.message}")
            null
        } catch(e: ServerResponseException) {
            // 5xx - responses
            println("Error: ${e.response.status.description}")
            println("Error Message: ${e.message}")
            null
        } catch(e: Exception) {
            Log.e("Finances Service", "Error: ${e.message}")
            null
        }
    }

    override suspend fun getLocations(): ArrayList<Location> {
        return try {
            client.get {
                url(FinanceApiRoutes.LOCATIONS)
            }.body()
        } catch(e: RedirectResponseException) {
            // 3xx - responses
            println("Error: ${e.response.status.description}")
            ArrayList()
        } catch(e: ClientRequestException) {
            // 4xx - responses
            println("Error: ${e.response.status.description}")
            ArrayList()
        } catch(e: ServerResponseException) {
            // 5xx - responses
            println("Error: ${e.response.status.description}")
            ArrayList()
        } catch(e: Exception) {
            println("Error: ${e.message}")
            ArrayList()
        }
    }

    override suspend fun addPayment(paymentRequest: PaymentRequest): String {
        return try {
            client.post {
                url(FinanceApiRoutes.ADD_PAYMENT)
                contentType(ContentType.Application.Json)
                setBody(paymentRequest)
            }.body()
        } catch(e: RedirectResponseException) {
            // 3xx - responses
            println("3xx Error: ${e.response.status.description}")
            e.response.status.description
        } catch(e: ClientRequestException) {
            // 4xx - responses
            println("4xx Error: ${e.response.status.description}")
            e.message
        } catch(e: ServerResponseException) {
            // 5xx - responses
            println("5xx Error: ${e.response.status.description}")
            e.message
        } catch(e: Exception) {
            "Error: ${e.message}"
        }
    }

    override suspend fun addSalary(water: Int, power: Int): String {
        val body = HashMap<String, Int>()
        body["water"] = water
        body["power"] = power

        return try {
            client.post{
                url(FinanceApiRoutes.ADD_SALARY)
                contentType(ContentType.Application.Json)
                setBody(body)
            }.body()
        } catch(e: RedirectResponseException) {
            // 3xx - responses
            println("3xx Error: ${e.response.status.description}")
            e.message
        } catch(e: ClientRequestException) {
            // 4xx - responses
            println("4xx Error: ${e.response.status.description}")
            e.message
        } catch(e: ServerResponseException) {
            // 5xx - responses
            println("5xx Error: ${e.response.status.description}")
            e.message
        } catch(e: Exception) {
            "Something went wrong!"
        }
    }

    override suspend fun deletePayment(id: Int, cash: Boolean): String {
        try {
            val body = HashMap<String, Any>()
            body["id"] = id
//            body["cash"] = cash
            val response: String = client.delete {
                url(FinanceApiRoutes.DELETE_PAYMENT)
                contentType(ContentType.Application.Json)
                setBody(body)
            }.body()
            return response
        } catch(e: RedirectResponseException) {
            // 3xx - responses
            println("3xx Error: ${e.response.status.description}")
            return "3xx Error: ${e.response.status.description}"
        } catch(e: ClientRequestException) {
            // 4xx - responses
            println("4xx Error: ${e.response.status.description}")
            return "4xx Error: ${e.response.status.description}"
        } catch(e: ServerResponseException) {
            // 5xx - responses
            println("5xx Error: ${e.response.status.description}")
            return "5xx Error: ${e.response.status.description}"
        } catch(e: Exception) {
            println(e.message)
            return ""
        }
    }

    override suspend fun deleteMonthlyPayment(name: String): String {
        try {
            val body = HashMap<String, Any>()
            body["name"] = name
            val response: String = client.delete {
                url(FinanceApiRoutes.DELETE_MONTHLY)
                contentType(ContentType.Application.Json)
                setBody(body)
            }.body()
            return response
        } catch(e: RedirectResponseException) {
            // 3xx - responses
            println("3xx Error: ${e.response.status.description}")
            return "3xx Error: ${e.response.status.description}"
        } catch(e: ClientRequestException) {
            // 4xx - responses
            println("4xx Error: ${e.response.status.description}")
            return "4xx Error: ${e.response.status.description}"
        } catch(e: ServerResponseException) {
            // 5xx - responses
            println("5xx Error: ${e.response.status.description}")
            return "5xx Error: ${e.response.status.description}"
        } catch(e: Exception) {
            println(e.message)
            return ""
        }
    }

    override suspend fun getSalaryInfo(): Boolean {
        return try {
            client.get {
                url(FinanceApiRoutes.SALARY)
            }.bodyAsText() == "True"
        } catch(e: RedirectResponseException) {
            // 3xx - responses
            println("3xx Error: ${e.response.status.description}")
            false
        } catch(e: ClientRequestException) {
            // 4xx - responses
            println("4xx Error: ${e.response.status.description}")
            false
        } catch(e: ServerResponseException) {
            // 5xx - responses
            println("5xx Error: ${e.response.status.description}")
            false
        } catch(e: Exception) {
            println(e.message)
            false
        }
    }

}