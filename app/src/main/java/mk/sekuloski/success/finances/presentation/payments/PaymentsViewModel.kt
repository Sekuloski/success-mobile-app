package mk.sekuloski.success.finances.presentation.payments

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.serialization.json.JsonArray
import mk.sekuloski.success.finances.domain.repository.FinancesRepository
import mk.sekuloski.success.utils.Resource
import javax.inject.Inject

@HiltViewModel
class PaymentsViewModel @Inject constructor(
    private val repository: FinancesRepository
): ViewModel() {

    var state by mutableStateOf(PaymentsState())
    private var searchJob: Job? = null

    fun onEvent(event: PaymentsEvent) {
        when(event) {
            is PaymentsEvent.Refresh -> {
                getPayments(fetchFromRemote = true)
            }
            is PaymentsEvent.OnSearchQueryChange -> {
                state = state.copy(searchQuery = event.query)
                searchJob?.cancel()
                searchJob = viewModelScope.launch {
                    delay(200L)
                    getPayments()
                }
            }
        }
    }

    private fun getPayments(
        query: String = state.searchQuery.lowercase(),
        fetchFromRemote: Boolean = false,
        ids: JsonArray = JsonArray(emptyList())
    ) {
        viewModelScope.launch {
            repository
                .getPayments(fetchFromRemote, query, ids)
                .collect { result ->
                    when(result) {
                        is Resource.Success -> {
                            result.data?.let { payments ->
                                state = state.copy(
                                    payments = payments
                                )
                            }
                        }
                        is Resource.Error -> Unit
                        is Resource.Loading -> {
                            state = state.copy(isLoading = result.isLoading)
                        }
                    }
                }
        }
    }
}