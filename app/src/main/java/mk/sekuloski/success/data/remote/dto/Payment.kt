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
import java.time.ZoneId
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter
import java.time.format.DateTimeParseException

@Serializable
data class Payment (
    val id: Int = 0,
    val amount: Int = 0,
    val name: String,
    @Serializable(with = DateSerializer::class)
    val date: LocalDateTime,
    val necessary: Boolean = false,
    val expense_type: Int,
    val payment_type: Int,
    val paid: Boolean = false,
    val monthly: Boolean = false,
    val location: String,
    val parts: JsonObject
    )

@OptIn(ExperimentalSerializationApi::class)
@Serializer(forClass = LocalDateTime::class)
object DateSerializer {
    private val formatter = DateTimeFormatter.ISO_DATE_TIME

    override fun deserialize(decoder: Decoder): LocalDateTime {
        val dateString = decoder.decodeString()
        return LocalDateTime.parse(dateString, formatter) as LocalDateTime
    }

    override fun serialize(encoder: Encoder, value: LocalDateTime) {
        val dateString = formatter.format(value)
        encoder.encodeString(dateString)
    }
}