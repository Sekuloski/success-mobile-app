package mk.sekuloski.success.data.remote.dto.finances

import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonObject
import java.time.ZonedDateTime

@Serializable
data class Subscription (
    val id: Int = 0,
    val amount: Int,
    val name: String,
    @Serializable(with = DateSerializer::class)
    val date: ZonedDateTime,
    val necessary: Boolean = false,
    val expense_type: Int,
    val active: Boolean,
    val hypothetical: Boolean,
    val history: JsonObject
    )
