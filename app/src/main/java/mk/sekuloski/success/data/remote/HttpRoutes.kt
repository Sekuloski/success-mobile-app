package mk.sekuloski.success.data.remote

object HttpRoutes {
    private const val BASE_URL = "https://finances.sekuloski.mk"
    private const val DEV_BASE_URL = "http://10.0.2.2:8000"
    const val PAYMENTS = "$BASE_URL/payments"
    const val ADD_PAYMENT = "$BASE_URL/add/payment"
    const val MONTHS = "$BASE_URL/months"
    const val SUBSCRIPTIONS = "$BASE_URL/subscriptions"
    const val LOCATIONS = "$BASE_URL/locations"
    const val MAIN = "$BASE_URL/"
}