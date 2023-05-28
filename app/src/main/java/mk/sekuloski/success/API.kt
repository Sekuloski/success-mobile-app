package mk.sekuloski.success

import android.util.Log
import mk.sekuloski.success.models.Location
import mk.sekuloski.success.models.Month
import mk.sekuloski.success.models.Payment
import okhttp3.*
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject
import java.io.IOException
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter
import java.time.format.DateTimeParseException

const val local = false
const val main_url = "https://finances.sekuloski.mk"
const val dev_url = "http://10.0.2.2:8000"
const val payments_url = "/payments"
const val months_url = "/months"
const val add_payment_url = "/add/payment"
const val locations_url = "/locations"

val JSON = "application/json; charset=utf-8".toMediaType()
val cookie = Cookie.Builder()
    .name("sekuloski-was-here")
    .value("true")
    .domain("finances.sekuloski.mk")
    .build()

object APISingleton {
    @Volatile
    private var INSTANCE: API? = null

    fun getInstance(): API? {
        if (INSTANCE == null) {
            synchronized(this) {
                if (INSTANCE == null) {
                    INSTANCE = API()
                }
            }
        }

        return INSTANCE
    }
}

class API {
    private val client = OkHttpClient()

    private fun getUrl(endpoint: String): String
    {
        return if(local)
        {
            dev_url + endpoint
        }
        else
        {
            main_url + endpoint
        }
    }
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

    fun getMainInfo(): HashMap<String, Int>
    {
        val returnData = HashMap<String, Int>()

        val request = Request.Builder()
            .url(getUrl(""))
            .addHeader("Cookie", cookie.toString())
            .build()

        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                e.printStackTrace()
            }

            override fun onResponse(call: Call, response: Response) {
                val data = JSONObject(response.body.string())

                returnData["amount_left"] = data.getInt("amount_left")
                returnData["salary"] = data.getInt("salary")
                returnData["bank"] = data.getInt("bank")
                returnData["cash"] = data.getInt("cash")
                returnData["expenses"] = data.getInt("expenses")
            }
        })

        return returnData
    }

    fun addPayment(json: JSONObject)
    {
        val requestBody = json.toString().toRequestBody(JSON)

        val request = Request.Builder()
            .url(getUrl(add_payment_url))
            .addHeader("Cookie", cookie.toString())
            .post(requestBody)
            .build()


        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                e.printStackTrace()
            }

            override fun onResponse(call: Call, response: Response) {
                Log.d("API","Payment added!")
            }
        })
    }

    fun getPayments(ids: JSONArray): ArrayList<Payment> {
        if (ids.length() == 0)
        {
            return ArrayList()
        }
        val payments = ArrayList<Payment>()
        val jsonObject = JSONObject()
        jsonObject.put("ids", ids)
        val requestBody = jsonObject.toString().toRequestBody(JSON)

        val cookie = Cookie.Builder()
            .name("sekuloski-was-here")
            .value("true")
            .domain("finances.sekuloski.mk")
            .build()

        val request = Request.Builder()
            .url(getUrl(payments_url))
            .addHeader("Cookie", cookie.toString())
            .post(requestBody)
            .build()

        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                e.printStackTrace()
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

    fun getMonths(): ArrayList<Month> {
        val months = ArrayList<Month>()

        val request = Request.Builder()
            .url(getUrl(months_url))
            .addHeader("Cookie", cookie.toString())
            .build()

        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                println(e.stackTraceToString())
            }

            override fun onResponse(call: Call, response: Response) {
                val jsonObject = JSONObject(response.body.string())

                for (year: String in jsonObject.keys())
                {
                    val monthsObject = jsonObject.getJSONObject(year)

                    for (month: String in monthsObject.keys())
                    {
                        val monthObject = monthsObject.getJSONObject(month)
                        val amountLeft = monthObject.getInt("left")
                        val expenses = monthObject.getInt("expenses")

                        val normalPaymentIds = monthObject.getJSONArray("normal")
                        val normalPaymentSum = monthObject.getInt("normal_sum")

                        val threeMonthIds = monthObject.getJSONArray("three_month")
                        val threeMonthSum = monthObject.getInt("three_month_sum")

                        val sixMonthIds = monthObject.getJSONArray("six_month")
                        val sixMonthSum = monthObject.getInt("six_month_sum")

                        months.add(Month(amountLeft, expenses, "$month $year", normalPaymentIds,
                            normalPaymentSum, sixMonthIds, sixMonthSum, threeMonthIds, threeMonthSum))
                    }
                }

            }
        })

        return months
    }

    fun getLocations(): ArrayList<Location> {
        val locations = ArrayList<Location>()

        val request = Request.Builder()
            .url(getUrl(locations_url))
            .addHeader("Cookie", cookie.toString())
            .build()

        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                e.printStackTrace()
            }

            override fun onResponse(call: Call, response: Response) {
                val jsonArrayInfo = JSONArray(response.body.string())
                val size:Int = jsonArrayInfo.length()
                for (i in 0 until size) {
                    val jsonObjectDetail: JSONObject = jsonArrayInfo.getJSONObject(i)

                    val id = jsonObjectDetail.getInt("id")
                    val name = jsonObjectDetail.getString("name")
                    val coordinates = jsonObjectDetail.getString("coordinates")

                    locations.add(Location(id, name, coordinates))
                }
            }
        })

        return locations
    }
}