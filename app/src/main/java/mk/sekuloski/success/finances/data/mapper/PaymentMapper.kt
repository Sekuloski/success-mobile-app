package mk.sekuloski.success.finances.data.mapper

import mk.sekuloski.success.finances.data.local.PaymentEntity
import mk.sekuloski.success.finances.domain.model.Payment

fun PaymentEntity.toPayment(): Payment {
    return Payment(
        id = id,
        name = name,
        amount = amount,
        date = date,
        necessary = necessary,
        expense_type = expense_type,
        payment_type = payment_type,
        category = category,
        paid = paid,
        monthly = monthly,
        location = location,
        parts = parts
    )
}

fun Payment.toPaymentEntity(): PaymentEntity {
    return PaymentEntity(
        id = id,
        name = name,
        amount = amount,
        date = date,
        necessary = necessary,
        expense_type = expense_type,
        payment_type = payment_type,
        category = category,
        paid = paid,
        monthly = monthly,
        location = location,
        parts = parts
    )
}