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
import mk.sekuloski.success.finances.domain.model.ExpenseType

fun initPie(
    pieChart: PieChart,
    context: Context,
    categories: ArrayList<Int>
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
    categories: ArrayList<Int>
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

    for (category: ExpenseType in ExpenseType.values())
    {
        if (categories[category.ordinal] > 0)
        {
            val label = "${counter.toString().padStart(2, '0')}. ${ExpenseType.getValues()[category.ordinal]}"
            counter++
            entries.add(PieEntry(categories[category.ordinal].toFloat(), label))
            colors[label] = Color.parseColor(allColors.removeAt(0))
        }
    }

    if (entries.size == 0)
    {
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

fun resetCategories(): ArrayList<Int> {
    val categories = ArrayList<Int>()
    for (category: ExpenseType in ExpenseType.values())
    {
        categories.add(0)
    }
    return categories
}