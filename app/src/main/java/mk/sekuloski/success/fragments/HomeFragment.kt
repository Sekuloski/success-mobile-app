package mk.sekuloski.success.fragments

import android.graphics.Color
import android.graphics.Typeface
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.github.mikephil.charting.animation.Easing
import com.github.mikephil.charting.charts.PieChart
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.PieData
import com.github.mikephil.charting.data.PieDataSet
import com.github.mikephil.charting.data.PieEntry
import com.github.mikephil.charting.highlight.Highlight
import com.github.mikephil.charting.utils.ColorTemplate
import com.github.mikephil.charting.utils.MPPointF
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch
import mk.sekuloski.success.R
import mk.sekuloski.success.data.remote.services.FinancesService
import mk.sekuloski.success.databinding.FragmentHomeBinding
import mk.sekuloski.success.utils.CustomPieChartRenderer
import mk.sekuloski.success.utils.ValuesFormatter

class HomeFragment(_client: FinancesService) : Fragment(R.layout.fragment_home), CoroutineScope by MainScope(),
    com.github.mikephil.charting.listener.OnChartValueSelectedListener {
    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!
    private val client = _client
    private lateinit var pieChart: PieChart
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.swipeRefresh.setOnRefreshListener {
            launch {
                val newData = client.getMainInfo()
                if (newData != null) {
                    binding.tvAmountLeft.text = newData.amount_left.toString()
                    binding.tvBank.text = newData.bank.toString()
                    binding.tvCash.text = newData.cash.toString()
//                    binding.tvSalary.text = newData.salary.toString()
                    binding.tvExpenses.text = newData.expenses.toString()
                    binding.swipeRefresh.isRefreshing = false
                    initPie(
                        newData.groceries,
                        newData.takeaway_food,
                        newData.gaming_gear,
                        newData.hanging_out,
                        newData.music_gear,
                        newData.sports_gear
                    )
                }
            }
        }
    }

    private fun initPie(
        groceries: Int,
        takeaway_food: Int,
        gaming_gear: Int,
        hanging_out: Int,
        music_gear: Int,
        sports_gear: Int
    ) {
        pieChart = binding.pieChart

        val colors = setData(groceries, takeaway_food, gaming_gear, hanging_out, music_gear, sports_gear)
        pieChart.renderer = CustomPieChartRenderer(pieChart, pieChart.animator, pieChart.viewPortHandler, colors)

        pieChart.description.isEnabled = false
        pieChart.setExtraOffsets(60f, 60f, 60f, 60f)

        pieChart.dragDecelerationFrictionCoef = 0.95f

        pieChart.isDrawHoleEnabled = true
        context?.let { ContextCompat.getColor(it, R.color.md_theme_dark_background) }
            ?.let { pieChart.setHoleColor(it) }
        context?.let { ContextCompat.getColor(it, R.color.md_theme_dark_background) }
            ?.let { pieChart.setTransparentCircleColor(it) }
        pieChart.setTransparentCircleAlpha(110)

        pieChart.transparentCircleRadius = 62f
        pieChart.holeRadius = 50f

        pieChart.setDrawCenterText(true)
        pieChart.rotationAngle = 0f
        pieChart.isRotationEnabled = true
        pieChart.isHighlightPerTapEnabled = true

        pieChart.animateY(1400, Easing.EaseInOutQuad)

        pieChart.legend.isEnabled = false
        context?.let { ContextCompat.getColor(it, R.color.md_theme_dark_onPrimaryContainer) }
            ?.let { pieChart.setEntryLabelColor(it) }
        pieChart.setEntryLabelTextSize(16f)
        pieChart.setEntryLabelTypeface(Typeface.DEFAULT_BOLD)
        pieChart.highlightValues(null)

        pieChart.invalidate()
    }

    private fun setData(
        groceries: Int,
        takeaway_food: Int,
        gaming_gear: Int,
        hanging_out: Int,
        music_gear: Int,
        sports_gear: Int
    ): HashMap<String, Int> {
        val entries = ArrayList<PieEntry>()
        val colors = HashMap<String, Int>()

        if (groceries > 0)
        {
            entries.add(PieEntry(groceries.toFloat(), "Groceries"))
            colors["Groceries"] = Color.parseColor("#4777c0")
        }
        if (takeaway_food > 0)
        {
            entries.add(PieEntry(takeaway_food.toFloat(), "Takeaway Food"))
            colors["Takeaway Food"] = Color.parseColor("#a374c6")
        }
        if (gaming_gear > 0)
        {
            entries.add(PieEntry(gaming_gear.toFloat(), "Gaming Gear"))
            colors["Gaming Gear"] = Color.parseColor("#4fb3e8")
        }
        if (hanging_out > 0)
        {
            entries.add(PieEntry(hanging_out.toFloat(), "Hang Outs"))
            colors["Hang Outs"] = Color.parseColor("#99cf43")
        }
        if (music_gear > 0)
        {
            entries.add(PieEntry(music_gear.toFloat(), "Music Gear"))
            colors["Music Gear"] = Color.parseColor("#fdc135")
        }
        if (sports_gear > 0)
        {
            entries.add(PieEntry(sports_gear.toFloat(), "Sports Gear"))
            colors["Sports Gear"] = Color.parseColor("#fd9a47")
        }

        if (entries.size == 0)
        {
            return colors
        }

        val dataSet = PieDataSet(entries, "Expense Types")
        dataSet.setDrawIcons(false)
        dataSet.sliceSpace = 4.5f
        dataSet.iconsOffset = MPPointF(0f, 40f)
        dataSet.selectionShift = 5f

        dataSet.colors = colors.values.toList()
        dataSet.setValueTextColors(colors.values.toList())
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
        return colors
    }

    override fun onResume() {
        super.onResume()

        launch {
            val newData = client.getMainInfo()
            if (newData != null) {
                binding.tvAmountLeft.text = newData.amount_left.toString()
                binding.tvBank.text = newData.bank.toString()
                binding.tvCash.text = newData.cash.toString()
                binding.tvSalary.text = newData.salary.toString()
                binding.tvExpenses.text = newData.expenses.toString()
                binding.swipeRefresh.isRefreshing = false
                initPie(newData.groceries,
                    newData.takeaway_food,
                    newData.gaming_gear,
                    newData.hanging_out,
                    newData.music_gear,
                    newData.sports_gear)
            }
        }
    }

    override fun onValueSelected(e: Entry?, h: Highlight?) {
        if (e == null)
            return
        if (h != null) {
            Log.i("VAL SELECTED",
                "Value: " + e.y + ", index: " + h.x
                        + ", DataSet index: " + h.dataSetIndex
            )
        }
    }

    override fun onNothingSelected() {
        return
    }
}