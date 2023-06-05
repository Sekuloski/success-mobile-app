package mk.sekuloski.success.data.remote.dto

import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.Serializable
import kotlinx.serialization.Serializer
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import kotlinx.serialization.json.JsonObject
import mk.sekuloski.success.ExpenseType
import mk.sekuloski.success.PaymentType
import org.json.JSONObject
import java.time.LocalDateTime
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter

@Serializable
data class PaymentRequest (
    val amount: Int,
    val name: String,
    @Serializable(with = DateSerializer::class)
    val date: ZonedDateTime,
    val necessary: Boolean,
    val expense_type: Int,
    val cash: Boolean,
    val monthly: Boolean,
    val credit: Boolean,
    val interest: Double,
    val location: Int,
    val pay: Boolean
)