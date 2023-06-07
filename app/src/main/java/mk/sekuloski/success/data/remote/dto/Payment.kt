package mk.sekuloski.success.data.remote.dto

import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonObject
import java.time.ZonedDateTime

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

