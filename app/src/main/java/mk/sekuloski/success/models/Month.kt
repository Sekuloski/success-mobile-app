package mk.sekuloski.success.models

import org.json.JSONArray

data class Month(val amountLeft: Int, val name: String, val payments: JSONArray)
/*
Total Left": 12212,
"Starting Amount": 12212,
"Bank": 11622,
"Cash": 590,
"Expenses": 33252,
"Normal Payments": {

},
"Three Month Payments": {

},
"Six Month Payments": {

},
"Credit": {},
"Subscriptions": {

},
"Incomes": {

},
"Bonuses": 5000
 */