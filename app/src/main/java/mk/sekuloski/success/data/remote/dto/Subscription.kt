package mk.sekuloski.success.data.remote.dto

import kotlinx.serialization.Serializable
import java.time.LocalDateTime

@Serializable
data class Subscription (
    val id: Int = 0,
    val amount: Int = 0,
    val name: String,
    @Serializable(with = DateSerializer::class)
    val date: LocalDateTime,
    val necessary: Boolean = false,
    val expense_type: Int,
    val active: Boolean
    )
