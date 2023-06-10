package mk.sekuloski.success.utils

import com.github.mikephil.charting.formatter.ValueFormatter
import java.text.DecimalFormat

class ValuesFormatter : ValueFormatter() {
    override fun getFormattedValue(value: Float): String {
        return value.toInt().toString()
    }

    companion object {
        private val suffix = arrayOf("", "k", "m", "b", "t")
        private const val MAX_LENGTH = 4

        /**
         * outputs:
         *
         * 856 = 856
         * 1000 = 1k
         * 5821 = 5.8k
         * 10500 = 10k
         * 101800 = 102k
         * 2000000 = 2m
         * 7800000 = 7.8m
         * 92150000 = 92m
         * 123200000 = 123m
         * 9999999 = 10m
         */
//        private fun pretty(number: Float): String {
//            var r: String = DecimalFormat("##0E0").format(number)
//            r = r.replace(
//                "E[0-9]".toRegex(), suffix[Character.getNumericValue(
//                    r[r.length - 1]
//                ) / 3]
//            )
//            while (r.length > MAX_LENGTH || r.matches("[0-9]+\\.[a-z]".toRegex())) {
//                r = r.substring(0, r.length - 2) + r.substring(r.length - 1)
//            }
//            return r
//        }
    }
}