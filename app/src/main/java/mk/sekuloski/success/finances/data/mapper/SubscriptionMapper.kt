package mk.sekuloski.success.finances.data.mapper

import mk.sekuloski.success.finances.data.local.PaymentEntity
import mk.sekuloski.success.finances.data.local.SubscriptionEntity
import mk.sekuloski.success.finances.domain.model.Payment
import mk.sekuloski.success.finances.domain.model.Subscription

fun SubscriptionEntity.toSubscription(): Subscription {
    return Subscription(
        id = id,
        name = name,
        amount = amount,
        date = date,
        necessary = necessary,
        expense_type = expense_type,
        category = category,
        active = active,
        history = history,
        hypothetical = hypothetical
    )
}

fun Subscription.toSubscriptionEntity(): SubscriptionEntity {
    return SubscriptionEntity(
        id = id,
        name = name,
        amount = amount,
        date = date,
        necessary = necessary,
        expense_type = expense_type,
        category = category,
        active = active,
        history = history,
        hypothetical = hypothetical
    )
}