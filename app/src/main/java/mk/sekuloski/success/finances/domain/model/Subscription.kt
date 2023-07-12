package mk.sekuloski.success.finances.domain.model

import kotlinx.serialization.json.JsonObject
import java.time.ZonedDateTime

data class Subscription (
    val id: Int = 0,
    val amount: Int,
    val name: String,
    val date: ZonedDateTime,
    val necessary: Boolean = false,
    val expense_type: Int,
    val category: Int,
    val active: Boolean,
    val hypothetical: Boolean,
    val history: String
    )
