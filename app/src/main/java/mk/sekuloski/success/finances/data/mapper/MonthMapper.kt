package mk.sekuloski.success.finances.data.mapper

import mk.sekuloski.success.finances.data.local.MonthEntity
import mk.sekuloski.success.finances.domain.model.Month

fun MonthEntity.toMonth(): Month {
    return Month(
        id = id,
        name = name,
        left = left,
        expenses = expenses
    )
}

fun Month.toMonthEntity(): MonthEntity {
    return MonthEntity(
        id = id,
        name = name,
        left = left,
        expenses = expenses
    )
}