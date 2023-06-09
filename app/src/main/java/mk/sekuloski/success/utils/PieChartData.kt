package mk.sekuloski.success.utils

import android.content.Context
import android.graphics.Color
import android.graphics.Typeface
import androidx.core.content.ContextCompat
import com.github.mikephil.charting.animation.Easing
import com.github.mikephil.charting.charts.PieChart
import com.github.mikephil.charting.data.PieData
import com.github.mikephil.charting.data.PieDataSet
import com.github.mikephil.charting.data.PieEntry
import com.github.mikephil.charting.utils.ColorTemplate
import com.github.mikephil.charting.utils.MPPointF
import mk.sekuloski.success.R

fun initPie(
    pieChart: PieChart,
    context: Context,
    categories: MutableMap<String, Int>
) {
    val colors = setData(pieChart, categories)
    pieChart.renderer = CustomPieChartRenderer(pieChart, pieChart.animator, pieChart.viewPortHandler, colors)

    pieChart.description.isEnabled = false
    pieChart.setExtraOffsets(50f, 50f, 50f, 50f)

    pieChart.dragDecelerationFrictionCoef = 0.95f

    pieChart.isDrawHoleEnabled = true
    context.let { ContextCompat.getColor(it, R.color.md_theme_dark_background) }
           .let { pieChart.setHoleColor(it) }
    context.let { ContextCompat.getColor(it, R.color.md_theme_dark_background) }
           .let { pieChart.setTransparentCircleColor(it) }
    pieChart.setTransparentCircleAlpha(110)

    pieChart.transparentCircleRadius = 62f
    pieChart.holeRadius = 50f

    pieChart.setDrawCenterText(true)
    pieChart.rotationAngle = 0f
    pieChart.isRotationEnabled = true
    pieChart.isHighlightPerTapEnabled = true

    pieChart.animateY(1400, Easing.EaseInOutQuad)

    pieChart.legend.isEnabled = false
    context.let { ContextCompat.getColor(it, R.color.md_theme_dark_onPrimaryContainer) }
           .let { pieChart.setEntryLabelColor(it) }
    pieChart.setEntryLabelTextSize(13f)
    pieChart.setEntryLabelTypeface(Typeface.DEFAULT_BOLD)
    pieChart.highlightValues(null)

    pieChart.invalidate()
}

fun setData(
    pieChart: PieChart,
    categories: MutableMap<String, Int>
): MutableMap<String, Int> {
    val entries = ArrayList<PieEntry>()
    val colors = HashMap<String, Int>()

    val allColors = mutableListOf( // 8 Colors for now
        "#4777c0", // Blue
        "#4fb3e8", // Light Blue
        "#99cf43", // Lime Green
        "#a374c6", // Purple
        "#fd9a47", // Orange
        "#fdc135", // Yellow
        "#eb6e7a", // Pink
        "#6785c2", // Dark Blue
        "#ff6384", // Coral Pink
        "#36a2eb", // Sky Blue
        "#ffce56", // Golden Yellow
        "#7bc043", // Lime Green
        "#cc65fe", // Lavender
        "#4bc0c0", // Turquoise
        "#ff9f40", // Tangerine
        "#6c757d"  // Steel Gray
    )
        .sorted()
        .toMutableList()

    // Colors are always sorted, as well as the HashMap of Labels.
    // The sort order below represents the order of colors.
    var counter = 1

    alternateSort(categories)
    for (category: String in categories.keys) {
        if (categories[category]!! > 0) {
            val label = "${counter.toString().padStart(2, '0')}. $category"
            counter++
            entries.add(PieEntry(categories[category]!!.toFloat(), label))
            colors[label] = Color.parseColor(allColors.removeAt(0))
        }
    }

    if (entries.size == 0) {
        return colors
    }
    val finalColors = colors.toSortedMap()

    val dataSet = PieDataSet(entries, "Expense Types")
    dataSet.setDrawIcons(false)
    dataSet.sliceSpace = 4.5f
    dataSet.iconsOffset = MPPointF(0f, 40f)
    dataSet.selectionShift = 5f

    dataSet.colors = finalColors.values.toList()
    dataSet.setValueTextColors(finalColors.values.toList())
    dataSet.selectionShift = 0f
    val data = PieData(dataSet)

    dataSet.xValuePosition = PieDataSet.ValuePosition.OUTSIDE_SLICE
    dataSet.yValuePosition = PieDataSet.ValuePosition.OUTSIDE_SLICE
    dataSet.valueLinePart1OffsetPercentage = 100f
    dataSet.valueLinePart1Length = 1.0f
    dataSet.valueLinePart2Length = 0f
    dataSet.valueTypeface = Typeface.DEFAULT_BOLD
    dataSet.valueLineColor = ColorTemplate.COLOR_NONE

    data.setValueFormatter(ValuesFormatter())
    data.setValueTextSize(16f)

    pieChart.data = data
    return finalColors
}

fun alternateSort(categories: MutableMap<String, Int>) {
    val sortedEntries = categories.entries.sortedBy { it.value }
    val result = LinkedHashMap<String, Int>()

    var left = 0
    var right = sortedEntries.size - 1

    while (left <= right) {
        if (left == right) {
            val entry = sortedEntries[left]
            result[entry.key] = entry.value
            break
        }

        val rightEntry = sortedEntries[right]
        val leftEntry = sortedEntries[left]
        result[rightEntry.key] = rightEntry.value
        result[leftEntry.key] = leftEntry.value
        right--
        left++
    }

    categories.clear()
    categories.putAll(result)
}
