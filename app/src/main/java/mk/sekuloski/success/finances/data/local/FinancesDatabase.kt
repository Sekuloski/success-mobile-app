package mk.sekuloski.success.finances.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters

@Database(
    entities = [
        PaymentEntity::class,
        CategoryEntity::class,
        FinancesMainEntity::class,
        LocationEntity::class,
        MonthEntity::class,
        SubscriptionEntity::class],
    version = 1
)
@TypeConverters(Converters::class)
abstract class FinancesDatabase: RoomDatabase() {
    abstract val dao: FinancesDao
}