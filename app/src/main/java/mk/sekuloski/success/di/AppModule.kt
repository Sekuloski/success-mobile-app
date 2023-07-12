package mk.sekuloski.success.di

import android.app.Application
import androidx.room.Room
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import io.ktor.client.HttpClient
import io.ktor.client.engine.android.Android
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.cookies.ConstantCookiesStorage
import io.ktor.client.plugins.cookies.HttpCookies
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logging
import io.ktor.http.Cookie
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json
import mk.sekuloski.success.finances.data.local.FinancesDatabase
import mk.sekuloski.success.finances.data.remote.FinancesService
import mk.sekuloski.success.finances.data.remote.FinancesServiceImpl
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideFinancesService(): FinancesService {
        return FinancesServiceImpl(
            client = HttpClient(Android) {
                install(Logging) {
                    level = LogLevel.ALL
                }
                install(ContentNegotiation) {
                    json(Json {
                        prettyPrint = true
                        isLenient = true
                        ignoreUnknownKeys = true
                    })
                }
                install(HttpCookies) {
                    storage = ConstantCookiesStorage(Cookie(name = "sekuloski-was-here", value = "true", domain = "finances.sekuloski.mk"))
                }
            }
        )
    }

    @Provides
    @Singleton
    fun provideFinancesDatabase(app: Application): FinancesDatabase {
        return Room.databaseBuilder(
            app,
            FinancesDatabase::class.java,
            "finances.db"
        ).build()
    }

}