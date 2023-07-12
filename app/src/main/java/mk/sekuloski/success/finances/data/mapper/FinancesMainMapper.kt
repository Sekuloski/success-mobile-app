package mk.sekuloski.success.finances.data.mapper

import mk.sekuloski.success.finances.data.local.FinancesMainEntity
import mk.sekuloski.success.finances.domain.model.FinancesMain

fun FinancesMainEntity.toFinancesMain(): FinancesMain {
    return FinancesMain(
        id = id,
        amount_left = amount_left,
        salary = salary,
        bank = bank,
        cash = cash,
        euros = euros,
        expenses = expenses,
        reserved = reserved
    )
}

fun FinancesMain.toFinancesMainEntity(): FinancesMainEntity {
    return FinancesMainEntity(
        id = id,
        amount_left = amount_left,
        salary = salary,
        bank = bank,
        cash = cash,
        euros = euros,
        expenses = expenses,
        reserved = reserved
    )
}