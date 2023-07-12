package mk.sekuloski.success.finances.presentation.payments

import mk.sekuloski.success.finances.domain.model.Payment

data class PaymentsState(
    val payments: List<Payment> = emptyList(),
    val isLoading: Boolean = false,
    val isRefreshing: Boolean = false,
    val searchQuery: String = ""
)