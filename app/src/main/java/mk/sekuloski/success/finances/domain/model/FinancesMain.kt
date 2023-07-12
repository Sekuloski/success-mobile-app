package mk.sekuloski.success.finances.domain.model

data class FinancesMain (
    val id: Int,
    val amount_left: Int,
    val salary: Int,
    val bank: Int,
    val cash: Int,
    val euros: Float,
    val expenses: Int,
    val reserved: Int
    )