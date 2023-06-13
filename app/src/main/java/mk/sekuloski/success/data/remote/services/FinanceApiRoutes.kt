package mk.sekuloski.success.data.remote.services

import android.os.Build

object FinanceApiRoutes {
    private const val REAL_BASE_URL = "https://finances.sekuloski.mk"
    private const val DEV_BASE_URL = "http://10.0.2.2:8000"
    private val isRunningOnEmulator: Boolean = Build.HARDWARE.contains("ranchu")
    private val BASE_URL = if (isRunningOnEmulator) DEV_BASE_URL else REAL_BASE_URL
    val PAYMENTS = "$BASE_URL/payments"
    val ADD_PAYMENT = "$BASE_URL/add/payment"
    val MONTHS = "$BASE_URL/months"
    val SUBSCRIPTIONS = "$BASE_URL/subscriptions"
    val LOCATIONS = "$BASE_URL/locations"
    val SALARY = "$BASE_URL/salary"
    val ADD_SALARY = "$BASE_URL/pay/monthly"
    val MAIN = "$BASE_URL/"
}