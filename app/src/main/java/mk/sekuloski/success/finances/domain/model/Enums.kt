package mk.sekuloski.success.finances.domain.model

enum class ExpenseType {
    BILL, GAMING_GEAR, MUSIC_GEAR, SPORTS_GEAR, CLOTHES, TAKEAWAY_FOOD, TRAVEL,
    OUTSIDE_ACTIVITY, HANGING_OUT, FOOTBALL, TOOLS, FURNITURE, WHISKEY, GROCERIES, HOLIDAY,
    DEBT, MEDICAL;

    companion object {
        fun getValues(): Array<String?> {
            val strs = arrayOfNulls<String>(ExpenseType.values().size)
            var i = 0
            for (p in ExpenseType.values()) strs[i++] = p.toString()
            return strs
        }
    }
}

enum class PaymentType {
    SINGLE_PAYMENT, THREE_MONTHS, SIX_MONTHS, LOAN
}