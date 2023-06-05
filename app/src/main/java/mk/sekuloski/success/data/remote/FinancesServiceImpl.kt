package mk.sekuloski.success.data.remote

import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.plugins.*
import io.ktor.client.request.*
import io.ktor.http.*
import kotlinx.serialization.json.JsonArray
import kotlinx.serialization.json.JsonObject
import mk.sekuloski.success.data.remote.dto.*

class FinancesServiceImpl(
    private val client: HttpClient
    
) : FinancesService {

    override suspend fun getPayments(ids: JsonArray): List<Payment> {
        return try {
            val body = HashMap<String, JsonArray>()
            body["ids"] = ids

            client.post {
                url(HttpRoutes.PAYMENTS)
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

    override suspend fun getSubscriptions(ids: JsonArray): ArrayList<Subscription> {
        return try {
            val body = HashMap<String, JsonArray>()
            body["ids"] = ids

            client.post {
                url(HttpRoutes.SUBSCRIPTIONS)
                contentType(ContentType.Application.Json)
                setBody(JsonObject(body))
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

    override suspend fun getMonths(months: Int): ArrayList<Month> {
        val body = HashMap<String, Int>()
        body["months"] = months

        return try {
            client.post {
                url(HttpRoutes.MONTHS)
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
                url(HttpRoutes.MAIN)
            }.body()
        } catch(e: RedirectResponseException) {
            // 3xx - responses
            println("Error: ${e.response.status.description}")
            null
        } catch(e: ClientRequestException) {
            // 4xx - responses
            println("Error: ${e.response.status.description}")
            null
        } catch(e: ServerResponseException) {
            // 5xx - responses
            println("Error: ${e.response.status.description}")
            null
        } catch(e: Exception) {
            println("Error: ${e.message}")
            null
        }
    }

    override suspend fun getLocations(): ArrayList<Location> {
        return try {
            client.get {
                url(HttpRoutes.LOCATIONS)
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
                url(HttpRoutes.ADD_PAYMENT)
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

    override suspend fun addSalary(): String {
        TODO("Not yet implemented")
    }

}