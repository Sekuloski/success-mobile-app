package mk.sekuloski.success.data.remote.dto

import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.Serializable
import kotlinx.serialization.Serializer
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import kotlinx.serialization.json.JsonObject
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter

@Serializable
data class Payment (
    val id: Int = 0,
    val amount: Int = 0,
    val name: String,
    @Serializable(with = DateSerializer::class)
    val date: ZonedDateTime,
    val necessary: Boolean = false,
    val expense_type: Int,
    val payment_type: Int,
    val paid: Boolean = false,
    val monthly: Boolean = false,
    val location: String,
    val parts: JsonObject
    )

@OptIn(ExperimentalSerializationApi::class)
@Serializer(forClass = ZonedDateTime::class)
object DateSerializer {
    private val formatter = DateTimeFormatter.ISO_DATE_TIME

    override fun deserialize(decoder: Decoder): ZonedDateTime {
        val dateString = decoder.decodeString()
        return ZonedDateTime.parse(dateString, formatter)
    }

    override fun serialize(encoder: Encoder, value: ZonedDateTime) {
        val dateString = formatter.format(value)
        encoder.encodeString(dateString)
    }
}