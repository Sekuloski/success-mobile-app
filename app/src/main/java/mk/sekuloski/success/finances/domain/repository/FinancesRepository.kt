package mk.sekuloski.success.finances.domain.repository

import kotlinx.coroutines.flow.Flow
import kotlinx.serialization.json.JsonArray
import mk.sekuloski.success.finances.domain.model.Payment
import mk.sekuloski.success.utils.Resource

interface FinancesRepository {
    suspend fun getPayments(
        fetchFromRemote: Boolean,
        query: String,
        ids: JsonArray
    ): Flow<Resource<List<Payment>>>
}