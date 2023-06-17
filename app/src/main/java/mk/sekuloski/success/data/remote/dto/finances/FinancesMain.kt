package mk.sekuloski.success.data.remote.dto.finances

import kotlinx.serialization.Serializable

@Serializable
data class FinancesMain (
    val amount_left: Int,
    val salary: Int,
    val bank: Int,
    val cash: Int,
    val expenses: Int,
    val reserved: Int
    )