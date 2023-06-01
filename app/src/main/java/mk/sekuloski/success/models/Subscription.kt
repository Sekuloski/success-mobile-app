package mk.sekuloski.success.models

import mk.sekuloski.success.ExpenseType
import mk.sekuloski.success.PaymentType
import org.json.JSONObject
import java.time.LocalDateTime

data class Subscription (
    val id: Int = 0,
    val amount: Int = 0,
    val name: String,
    val date: LocalDateTime,
    val necessary: Boolean = false,
    val expenseType: ExpenseType,
    val active: Boolean
)