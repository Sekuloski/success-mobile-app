package mk.sekuloski.success.finances.data.repository

import coil.network.HttpException
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.serialization.json.JsonArray
import mk.sekuloski.success.finances.data.local.FinancesDatabase
import mk.sekuloski.success.finances.data.mapper.toPayment
import mk.sekuloski.success.finances.data.mapper.toPaymentEntity
import mk.sekuloski.success.finances.data.remote.FinancesService
import mk.sekuloski.success.finances.domain.model.Payment
import mk.sekuloski.success.finances.domain.repository.FinancesRepository
import mk.sekuloski.success.utils.Resource
import java.io.IOException
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class FinancesRepositoryImpl @Inject constructor(
    private val api: FinancesService,
    db: FinancesDatabase
): FinancesRepository {

    private val dao = db.dao

    override suspend fun getPayments(
        fetchFromRemote: Boolean,
        query: String,
        ids: JsonArray
    ): Flow<Resource<List<Payment>>> {
        return flow {
            emit(Resource.Loading())
            val localPayments = dao.searchPayments(query)
            emit(Resource.Success(
                data = localPayments.map { it.toPayment() }
            ))

            val isDbEmpty = localPayments.isEmpty() && query.isBlank()
            val shouldJustLoadFromCache = !isDbEmpty && !fetchFromRemote

            if (shouldJustLoadFromCache) {
                emit(Resource.Loading(false))
                return@flow
            }

            val remotePayments = try {
                api.getPayments(ids)
            } catch (e: IOException) {
                e.printStackTrace()
                emit(Resource.Error("Couldn't Load Data, IOException"))
                null
            } catch (e: HttpException) {
                e.printStackTrace()
                emit(Resource.Error("Couldn't Load Data, HttpException"))
                null
            }

            remotePayments?.let { payments ->
                dao.clearPayments()
                dao.insertPayments(
                    payments.map { it.toPaymentEntity() }
                )
                emit(Resource.Success(
                    data = dao.searchPayments("").map { it.toPayment() }
                ))
                emit(Resource.Loading(false))
            }
        }
    }
}