package mk.sekuloski.success.finances.domain.model

import kotlinx.serialization.json.JsonObject
import java.time.ZonedDateTime

data class Payment (
    val id: Int = 0,
    val amount: Int = 0,
    val name: String,
    val date: ZonedDateTime,
    val necessary: Boolean = false,
    val expense_type: Int,
    val payment_type: Int,
    val category: Int,
    val paid: Boolean = false,
    var monthly: Boolean = false,
    val location: Int,
    val parts: String
    )

