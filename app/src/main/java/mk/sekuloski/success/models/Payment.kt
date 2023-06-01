package mk.sekuloski.success.models

import mk.sekuloski.success.ExpenseType
import mk.sekuloski.success.PaymentType
import org.json.JSONObject
import java.time.LocalDateTime

data class Payment (
    val id: Int = 0,
    val amount: Int = 0,
    val name: String,
    val date: LocalDateTime,
    val necessary: Boolean = false,
    val expenseType: ExpenseType,
    val paymentType: PaymentType,
    val paid: Boolean = false,
    val monthly: Boolean = false,
//    val location: Location,
    val parts: JSONObject
    )