package mk.sekuloski.success.finances.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonObject
import java.time.ZonedDateTime

@Serializable
@Entity
data class SubscriptionEntity (
    @PrimaryKey val id: Int,
    val amount: Int,
    val name: String,
    @Serializable(with = DateSerializer::class)
    val date: ZonedDateTime,
    val necessary: Boolean = false,
    val expense_type: Int,
    val category: Int,
    val active: Boolean,
    val hypothetical: Boolean,
    val history: String
    )
