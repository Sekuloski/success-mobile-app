package mk.sekuloski.success

import okhttp3.*
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject
import java.io.IOException
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter
import java.time.format.DateTimeParseException

const val payments_url = "https://finances.sekuloski.mk/payments"
const val months_url = "https://finances.sekuloski.mk/months"
val cookie = Cookie.Builder()
    .name("sekuloski-was-here")
    .value("true")
    .domain("finances.sekuloski.mk")
    .build()

fun convertStringToDate(dateTimeString: String): LocalDateTime {
    val zonedDateTime: ZonedDateTime = try {
        val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ssXXX")
        ZonedDateTime.parse(dateTimeString, formatter)
    } catch (exception: DateTimeParseException) {
        val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSSSSSXXX")
        ZonedDateTime.parse(dateTimeString, formatter)
    }

    return zonedDateTime.withZoneSameInstant(ZoneId.systemDefault()).toLocalDateTime()
}

fun getPayments(client: OkHttpClient): ArrayList<Payment> {
    val payments = ArrayList<Payment>()

    val cookie = Cookie.Builder()
        .name("sekuloski-was-here")
        .value("true")
        .domain("finances.sekuloski.mk")
        .build()

    val request = Request.Builder()
        .url(payments_url)
        .addHeader("Cookie", cookie.toString())
        .build()

    client.newCall(request).enqueue(object : Callback {
        override fun onFailure(call: Call, e: IOException) {

        }

        override fun onResponse(call: Call, response: Response) {
            val jsonArrayInfo = JSONArray(response.body.string())
            val size:Int = jsonArrayInfo.length()
            for (i in 0 until size) {
                val jsonObjectDetail: JSONObject = jsonArrayInfo.getJSONObject(i)

                val id = jsonObjectDetail.getInt("id")
                val name = jsonObjectDetail.getString("name")
                val location = jsonObjectDetail.getString("location")
                val date = convertStringToDate(jsonObjectDetail.getString("date"))
                val amount = jsonObjectDetail.getInt("amount")
                val necessary = jsonObjectDetail.getBoolean("necessary")
                val expenseType = ExpenseType.values()[jsonObjectDetail.getInt("expense_type")]
                val paymentType = PaymentType.values()[jsonObjectDetail.getInt("payment_type")]
                val paid = jsonObjectDetail.getBoolean("paid")
                val monthly = jsonObjectDetail.getBoolean("monthly")
                var parts = JSONObject()
                try
                {
                    parts = jsonObjectDetail.getJSONObject("parts")
                }
                catch (exception: JSONException)
                {
                        println("Parts object is empty for $name")
                }

                val payment = Payment(id, amount, name, date, necessary, expenseType, paymentType, paid, monthly, parts)

                payments.add(payment)
            }
        }
    })

    return payments
}

fun getMonths(client: OkHttpClient): ArrayList<Month> {
    val months = ArrayList<Month>()

    val request = Request.Builder()
        .url(months_url)
        .addHeader("Cookie", cookie.toString())
        .build()

    client.newCall(request).enqueue(object : Callback {
        override fun onFailure(call: Call, e: IOException) {

        }

        override fun onResponse(call: Call, response: Response) {
            val jsonObject = JSONObject(response.body.string())

            for (year: String in jsonObject.keys())
            {
                val monthsObject = jsonObject.getJSONObject(year)
                for (month: String in monthsObject.keys())
                {
                    val monthObject = monthsObject.getJSONObject(month)
                    val amountLeft = monthObject.getInt("Total Left")
                    val expenses = monthObject.getInt("Expenses")
//                    val paymentsObject = monthObject.getJSONObject("")

                    months.add(Month("$month $year", amountLeft, expenses, ArrayList()))
                }
            }

        }
    })

    return months
}