package mk.sekuloski.success.models

import org.json.JSONArray

data class Month(
    val amountLeft: Int,
    val expenses: Int,
    val name: String,
    val normalPayments: JSONArray,
    val normalSum: Int,
    val sixMonthPayments: JSONArray,
    val sixMonthSum: Int,
    val threeMonthPayments: JSONArray,
    val threeMonthSum: Int,
    val subscriptions: JSONArray,
    val subscriptionSum: Int,
    )