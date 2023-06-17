package mk.sekuloski.success.data.remote.dto.finances

import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonArray

@Serializable
data class Month(
    val left: Int,
    val expenses: Int,
    var name: String,
    val football: Int,
    val music_gear: Int,
    val sports_gear: Int,
    val takeaway_food: Int,
    val hanging_out: Int,
    val groceries: Int,
    val normal_ids: JsonArray,
    val normal_sum: Int,
    val six_month_ids: JsonArray,
    val six_month_sum: Int,
    val three_month_ids: JsonArray,
    val three_month_sum: Int,
    val subscription_ids: JsonArray,
    val subscription_sum: Int,
    val hypothetical_ids: JsonArray = JsonArray(ArrayList()),
    val hypothetical_sum: Int = 0,
    val loan_ids: JsonArray,
    val loan_sum: Int,
    val incomes: JsonArray
    )