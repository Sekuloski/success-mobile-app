package mk.sekuloski.success.data.remote.dto

import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonArray

@Serializable
data class Month(
    val left: Int,
    val expenses: Int,
    var name: String,
    val normal: JsonArray,
    val normal_sum: Int,
    val six_month: JsonArray,
    val six_month_sum: Int,
    val three_month: JsonArray,
    val three_month_sum: Int,
    val subscriptions: JsonArray,
    val subscription_sum: Int,
    val hypothetical: JsonArray = JsonArray(ArrayList()),
    val hypothetical_sum: Int = 0,
    val credit: JsonArray,
    val credit_sum: Int,
    val incomes: JsonArray
    )