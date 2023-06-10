package mk.sekuloski.success.utils

import com.github.mikephil.charting.formatter.ValueFormatter

class SalaryFormatter : ValueFormatter() {
    override fun getFormattedValue(value: Float): String {
        return (value/36000*100).toInt().toString() + "%"
    }
}