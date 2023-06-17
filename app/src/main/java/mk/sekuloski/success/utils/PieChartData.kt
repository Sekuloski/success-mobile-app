package mk.sekuloski.success.utils

import android.graphics.Color
import android.graphics.Typeface
import android.util.Log
import com.github.mikephil.charting.charts.PieChart
import com.github.mikephil.charting.data.PieData
import com.github.mikephil.charting.data.PieDataSet
import com.github.mikephil.charting.data.PieEntry
import com.github.mikephil.charting.utils.ColorTemplate
import com.github.mikephil.charting.utils.MPPointF

fun setData(
    pieChart: PieChart,
    groceries: Int,
    takeaway_food: Int,
    football: Int,
    hanging_out: Int,
    music_gear: Int,
    sports_gear: Int
): MutableMap<String, Int> {
    val entries = ArrayList<PieEntry>()
    val colors = HashMap<String, Int>()

    val allColors = mutableListOf(
        "#4777c0",
        "#4fb3e8",
        "#99cf43",
        "#a374c6",
        "#fd9a47",
        "#fdc135")
        .sorted()
        .toMutableList()

    // Colors are always sorted, as well as the HashMap of Labels.
    // The sort order below represents the order of colors.
    var counter = 1
    if (groceries > 0)
    {
        val label = "$counter. Groceries"
        counter++
        entries.add(PieEntry(groceries.toFloat(), label))
        colors[label] = Color.parseColor(allColors.removeAt(0))
    }
    if (hanging_out > 0)
    {
        val label = "$counter. Hang Outs"
        counter++
        entries.add(PieEntry(hanging_out.toFloat(), label))
        colors[label] = Color.parseColor(allColors.removeAt(0))
    }
    if (music_gear > 0)
    {
        val label = "$counter. Music Gear"
        counter++
        entries.add(PieEntry(music_gear.toFloat(), label))
        colors[label] = Color.parseColor(allColors.removeAt(0))
    }
    if (football > 0)
    {
        val label = "$counter. Football"
        counter++
        entries.add(PieEntry(football.toFloat(), label))
        colors[label] = Color.parseColor(allColors.removeAt(0))
    }
    if (sports_gear > 0)
    {
        val label = "$counter. Sports Gear"
        counter++
        entries.add(PieEntry(sports_gear.toFloat(), label))
        colors[label] = Color.parseColor(allColors.removeAt(0))
    }
    if (takeaway_food > 0)
    {
        val label = "$counter. Takeaway Food"
        entries.add(PieEntry(takeaway_food.toFloat(), label))
        colors[label] = Color.parseColor(allColors.removeAt(0))
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
    data.setValueTextSize(18f)

    pieChart.data = data
    return finalColors
}