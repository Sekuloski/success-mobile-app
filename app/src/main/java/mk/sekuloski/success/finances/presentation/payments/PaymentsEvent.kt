package mk.sekuloski.success.finances.presentation.payments

sealed class PaymentsEvent {
    object Refresh: PaymentsEvent()
    data class OnSearchQueryChange(val query: String): PaymentsEvent()
}
