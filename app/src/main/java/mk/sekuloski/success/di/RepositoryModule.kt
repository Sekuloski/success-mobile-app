package mk.sekuloski.success.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import mk.sekuloski.success.finances.data.repository.FinancesRepositoryImpl
import mk.sekuloski.success.finances.domain.repository.FinancesRepository
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Binds
    @Singleton
    abstract fun bindFinancesRepository(
        financesRepositoryImpl: FinancesRepositoryImpl
    ): FinancesRepository
}