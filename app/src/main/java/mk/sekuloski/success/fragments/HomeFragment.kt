package mk.sekuloski.success.fragments

import android.graphics.Color
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
import mk.sekuloski.success.data.remote.FinancesService
import mk.sekuloski.success.databinding.FragmentHomeBinding
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

        pieChart = binding.pieChart
        pieChart.setNoDataText("Loading...")
        pieChart.setNoDataTextColor(R.color.white)
        pieChart.invalidate()

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
                    initPie()
                    setData(
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

    private fun initPie() {
        pieChart.description.isEnabled = false
        pieChart.setExtraOffsets(5f, 10f, 5f, 5f)
        pieChart.dragDecelerationFrictionCoef = 0.95f

        pieChart.isDrawHoleEnabled = true
        context?.let { ContextCompat.getColor(it, R.color.md_theme_dark_secondaryContainer) }
            ?.let { pieChart.setHoleColor(it) }
        pieChart.holeRadius = 25f
        pieChart.transparentCircleRadius = 25f
        pieChart.setDrawCenterText(true)
        pieChart.rotationAngle = 0f
        pieChart.isRotationEnabled = true
        pieChart.isHighlightPerTapEnabled = true

        pieChart.animateY(1400, Easing.EaseInOutQuad)

        pieChart.legend.isEnabled = false
        pieChart.setEntryLabelColor(Color.BLACK)
        pieChart.setEntryLabelTextSize(14f)
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
        ) {
        val entries = ArrayList<PieEntry>()

        if (groceries > 0)
        {
            entries.add(PieEntry(groceries.toFloat(), "Groceries"))
        }
        if (takeaway_food > 0)
        {
            entries.add(PieEntry(takeaway_food.toFloat(), "Takeaway Food"))
        }
        if (gaming_gear > 0)
        {
            entries.add(PieEntry(gaming_gear.toFloat(), "Gaming Gear"))
        }
        if (hanging_out > 0)
        {
            entries.add(PieEntry(hanging_out.toFloat(), "Hang Outs"))
        }
        if (music_gear > 0)
        {
            entries.add(PieEntry(music_gear.toFloat(), "Music Gear"))
        }
        if (sports_gear > 0)
        {
            entries.add(PieEntry(sports_gear.toFloat(), "Sports Gear"))
        }

        val dataSet = PieDataSet(entries, "Expense Types")
        dataSet.setDrawIcons(false)
        dataSet.sliceSpace = 4.5f
        dataSet.iconsOffset = MPPointF(0f, 40f)
        dataSet.selectionShift = 5f

        // add a lot of colors
        val colors = ArrayList<Int>()
        for (c in ColorTemplate.VORDIPLOM_COLORS) colors.add(c)
        for (c in ColorTemplate.JOYFUL_COLORS) colors.add(c)
        for (c in ColorTemplate.COLORFUL_COLORS) colors.add(c)
        for (c in ColorTemplate.LIBERTY_COLORS) colors.add(c)
        for (c in ColorTemplate.PASTEL_COLORS) colors.add(c)
        colors.add(ColorTemplate.getHoloBlue())
        dataSet.colors = colors
        dataSet.selectionShift = 0f
        val data = PieData(dataSet)

//        data.setValueFormatter(PercentFormatter())
        data.setValueFormatter(ValuesFormatter())
        data.setValueTextSize(18f)
        context?.let { ContextCompat.getColor(it, R.color.black) }
            ?.let { data.setValueTextColor(it)}
        pieChart.data = data

        pieChart.setOnChartValueSelectedListener(this)

        // undo all highlights
        pieChart.highlightValues(null)
        pieChart.invalidate()
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
                initPie()
                setData(
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