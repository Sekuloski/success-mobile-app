package mk.sekuloski.success.finances.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface FinancesDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPayments(
        paymentEntities: List<PaymentEntity>
    )

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertSubscriptions(
        subscriptionEntities: List<SubscriptionEntity>
    )

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCategories(
        categoryEntities: List<CategoryEntity>
    )

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMonths(
        monthEntities: List<MonthEntity>
    )

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertLocations(
        locationEntities: List<LocationEntity>
    )

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertFinancesMain(
        financesMainEntity: FinancesMainEntity
    )

    @Query("DELETE FROM paymententity")
    suspend fun clearPayments()

    @Query("DELETE FROM subscriptionentity")
    suspend fun clearSubscriptions()

    @Query("DELETE FROM categoryentity")
    suspend fun clearCategories()

    @Query("DELETE FROM locationentity")
    suspend fun clearLocations()

    @Query("DELETE FROM monthentity")
    suspend fun clearMonths()

    @Query("DELETE FROM financesmainentity")
    suspend fun clearFinancesMain()

    @Query(
        """
            SELECT *
            FROM paymententity
            WHERE LOWER(name) LIKE '%' || LOWER(:query) || '%' OR
                UPPER(:query) == name
        """
    )
    suspend fun searchPayments(query: String): List<PaymentEntity>

    @Query(
        """
            SELECT *
            FROM subscriptionentity
            WHERE LOWER(name) LIKE '%' || LOWER(:query) || '%' OR
                UPPER(:query) == name
        """
    )
    suspend fun searchSubscriptions(query: String): List<SubscriptionEntity>

    @Query(
        """
            SELECT *
            FROM categoryentity
            WHERE LOWER(name) LIKE '%' || LOWER(:query) || '%' OR
                UPPER(:query) == name
        """
    )
    suspend fun searchCategories(query: String): List<CategoryEntity>

    @Query(
        """
            SELECT *
            FROM monthentity
            WHERE LOWER(name) LIKE '%' || LOWER(:query) || '%' OR
                UPPER(:query) == name
        """
    )
    suspend fun searchMonths(query: String): List<MonthEntity>

    @Query(
        """
            SELECT *
            FROM locationentity
            WHERE LOWER(name) LIKE '%' || LOWER(:query) || '%' OR
                UPPER(:query) == name
        """
    )
    suspend fun searchLocations(query: String): List<LocationEntity>
}