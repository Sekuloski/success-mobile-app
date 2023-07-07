package mk.sekuloski.success.data.remote.dto.finances

import kotlinx.serialization.Serializable
import java.time.ZonedDateTime

@Serializable
data class PaymentRequest (
    val amount: Int,
    val name: String,
    @Serializable(with = DateSerializer::class)
    val date: ZonedDateTime,
    val necessary: Boolean,
    val expense_type: Int,
//    val category_id: Int,
    val cash: Boolean,
    val euros: Boolean,
    val monthly: Boolean,
    val payments: Int,
    val credit: Boolean,
    val interest: Double,
    val location: Int,
    val pay: Boolean
)