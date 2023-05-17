package mk.sekuloski.success

class Month {
    var amountLeft: Int = 0
    var expenses: Int = 0
    lateinit var name: String
    lateinit var payments: List<Payment>

    constructor()

    constructor(name: String, amountLeft: Int, expenses: Int, payments: List<Payment>)
    {
        this.name = name
        this.amountLeft = amountLeft
        this.expenses = expenses
        this.payments = payments
    }
}
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