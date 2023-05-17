package mk.sekuloski.success.models

import mk.sekuloski.success.ExpenseType
import mk.sekuloski.success.PaymentType
import org.json.JSONObject
import java.time.LocalDateTime

class Payment {
    var id: Int = 0
    var amount: Int = 0
    var necessary: Boolean = false
    var paid: Boolean = false
    var monthly: Boolean = false
    lateinit var date: LocalDateTime
    lateinit var name: String
    lateinit var expenseType: ExpenseType
    lateinit var paymentType: PaymentType
    lateinit var location: Location
    lateinit var parts: JSONObject

    constructor(
        id: Int,
        amount: Int,
        name: String,
        date: LocalDateTime,
        necessary: Boolean,
        expenseType: ExpenseType,
        paymentType: PaymentType,
        paid: Boolean,
        monthly: Boolean,
        parts: JSONObject
    ) {
        this.id = id
        this.amount = amount
        this.name = name
        this.date = date
        this.necessary = necessary
        this.expenseType = expenseType
        this.paymentType = paymentType
        this.paid = paid
        this.monthly = monthly
        this.parts = parts
    }

    constructor()
}