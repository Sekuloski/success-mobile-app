package mk.sekuloski.success.finances.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonObject
import java.time.ZonedDateTime

@Serializable
@Entity
data class PaymentEntity (
    @PrimaryKey val id: Int,
    val amount: Int = 0,
    val name: String,
    @Serializable(with = DateSerializer::class)
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

