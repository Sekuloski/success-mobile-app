package mk.sekuloski.success.fragments.finances

import android.annotation.SuppressLint
import android.graphics.Color
import android.graphics.Typeface
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.github.mikephil.charting.animation.Easing
import com.github.mikephil.charting.charts.PieChart
import com.github.mikephil.charting.data.PieData
import com.github.mikephil.charting.data.PieDataSet
import com.github.mikephil.charting.data.PieEntry
import com.github.mikephil.charting.utils.ColorTemplate
import com.github.mikephil.charting.utils.MPPointF
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch
import mk.sekuloski.success.*
import mk.sekuloski.success.adapter.finances.MonthAdapter
import mk.sekuloski.success.adapter.finances.PaymentAdapter
import mk.sekuloski.success.adapter.finances.SubscriptionAdapter
import mk.sekuloski.success.data.remote.services.FinancesService
import mk.sekuloski.success.databinding.FragmentMonthBinding
import mk.sekuloski.success.data.remote.dto.finances.Month
import mk.sekuloski.success.utils.CustomPieChartRenderer
import mk.sekuloski.success.utils.ValuesFormatter

const val openedRecyclerViewHeight = 480
const val closedRecyclerViewHeight = 40
class MonthFragment(_month: Month, _client: FinancesService, _name: String) : Fragment(R.layout.fragment_month), CoroutineScope by MainScope() {
    private var _binding: FragmentMonthBinding? = null
    private val binding get() = _binding!!
    private val month: Month = _month
    private val name: String = _name
    private val client: FinancesService = _client
    private lateinit var pieChart: PieChart
    private lateinit var fullNormalAdapter: PaymentAdapter
    private lateinit var fullSixMonthAdapter: PaymentAdapter
    private lateinit var fullThreeMonthAdapter: PaymentAdapter
    private lateinit var fullLoanAdapter: PaymentAdapter
    private lateinit var fullSubscriptionAdapter: SubscriptionAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMonthBinding.inflate(inflater, container, false)
        return binding.root
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

        val normalPaymentsRecyclerView = binding.rvPayments
        val sixMonthPaymentsRecyclerView = binding.rvSixMonthPayments
        val threeMonthPaymentsRecyclerView = binding.rvThreeMonthPayments
        val loansRecyclerView = binding.rvLoans
        val subscriptionsRecyclerView = binding.rvSubscriptions

        binding.monthName.text = name

        val emptyAdapter = PaymentAdapter(requireContext(), ArrayList())
        normalPaymentsRecyclerView.adapter = emptyAdapter
        normalPaymentsRecyclerView.layoutParams.height = closedRecyclerViewHeight
        normalPaymentsRecyclerView.setHasFixedSize(true)

        sixMonthPaymentsRecyclerView.adapter = emptyAdapter
        sixMonthPaymentsRecyclerView.layoutParams.height = closedRecyclerViewHeight
        sixMonthPaymentsRecyclerView.setHasFixedSize(true)

        threeMonthPaymentsRecyclerView.adapter = emptyAdapter
        threeMonthPaymentsRecyclerView.layoutParams.height = closedRecyclerViewHeight
        threeMonthPaymentsRecyclerView.setHasFixedSize(true)

        loansRecyclerView.adapter = emptyAdapter
        loansRecyclerView.layoutParams.height = closedRecyclerViewHeight
        loansRecyclerView.setHasFixedSize(true)

        subscriptionsRecyclerView.adapter = emptyAdapter
        subscriptionsRecyclerView.layoutParams.height = closedRecyclerViewHeight
        subscriptionsRecyclerView.setHasFixedSize(true)


        launch {
            fullNormalAdapter = PaymentAdapter(requireContext(), client.getPayments(month.normal_ids))
            fullSixMonthAdapter = PaymentAdapter(requireContext(), client.getPayments(month.six_month_ids))
            fullThreeMonthAdapter = PaymentAdapter(requireContext(), client.getPayments(month.three_month_ids))
            fullLoanAdapter = PaymentAdapter(requireContext(), client.getPayments(month.loan_ids))
            fullSubscriptionAdapter = SubscriptionAdapter(requireContext(), client.getSubscriptions(month.subscription_ids))

            binding.amountLeft.text = month.left.toString()
            binding.expensesAmount.text = month.expenses.toString()
            binding.tvNormalSum.text = month.normal_sum.toString()
            binding.tvSixMonthSum.text = month.six_month_sum.toString()
            binding.tvThreeMonthSum.text = month.three_month_sum.toString()
            binding.tvLoansSum.text = month.loan_sum.toString()
            binding.tvSubscriptionsSum.text = month.subscription_sum.toString()

            initPie(
                month.groceries,
                month.takeaway_food,
                month.gaming_gear,
                month.hanging_out,
                month.music_gear,
                month.sports_gear
            )

            binding.btnShowMoreNormal.setOnClickListener {
                if (binding.btnShowMoreNormal.text.toString() == "See more") {
                    normalPaymentsRecyclerView.swapAdapter(fullNormalAdapter, true)
                    normalPaymentsRecyclerView.layoutParams.height = openedRecyclerViewHeight
                    binding.btnShowMoreNormal.text = "See less"
                } else {
                    normalPaymentsRecyclerView.swapAdapter(emptyAdapter, true)
                    normalPaymentsRecyclerView.layoutParams.height = closedRecyclerViewHeight
                    binding.btnShowMoreNormal.text = "See more"
                }
            }

            binding.btnShowMoreSixMonth.setOnClickListener {
                if (binding.btnShowMoreSixMonth.text.toString() == "See more") {
                    sixMonthPaymentsRecyclerView.swapAdapter(fullSixMonthAdapter, true)
                    sixMonthPaymentsRecyclerView.layoutParams.height = openedRecyclerViewHeight
                    binding.btnShowMoreSixMonth.text = "See less"
                } else {
                    sixMonthPaymentsRecyclerView.swapAdapter(emptyAdapter, true)
                    sixMonthPaymentsRecyclerView.layoutParams.height = closedRecyclerViewHeight
                    binding.btnShowMoreSixMonth.text = "See more"
                }
            }

            binding.btnShowMoreThreeMonth.setOnClickListener {
                if (binding.btnShowMoreThreeMonth.text.toString() == "See more") {
                    threeMonthPaymentsRecyclerView.swapAdapter(fullThreeMonthAdapter, true)
                    threeMonthPaymentsRecyclerView.layoutParams.height = openedRecyclerViewHeight
                    binding.btnShowMoreThreeMonth.text = "See less"
                } else {
                    threeMonthPaymentsRecyclerView.swapAdapter(emptyAdapter, true)
                    threeMonthPaymentsRecyclerView.layoutParams.height = closedRecyclerViewHeight
                    binding.btnShowMoreThreeMonth.text = "See more"
                }
            }

            binding.btnShowMoreLoans.setOnClickListener {
                if (binding.btnShowMoreLoans.text.toString() == "See more") {
                    loansRecyclerView.swapAdapter(fullLoanAdapter, true)
                    loansRecyclerView.layoutParams.height = openedRecyclerViewHeight
                    binding.btnShowMoreLoans.text = "See less"
                } else {
                    loansRecyclerView.swapAdapter(emptyAdapter, true)
                    loansRecyclerView.layoutParams.height = closedRecyclerViewHeight
                    binding.btnShowMoreLoans.text = "See more"
                }
            }

            binding.btnShowMoreSubscriptions.setOnClickListener {
                if (binding.btnShowMoreSubscriptions.text.toString() == "See more") {
                    subscriptionsRecyclerView.swapAdapter(fullSubscriptionAdapter, true)
                    subscriptionsRecyclerView.layoutParams.height = openedRecyclerViewHeight
                    binding.btnShowMoreSubscriptions.text = "See less"
                } else {
                    subscriptionsRecyclerView.swapAdapter(emptyAdapter, true)
                    subscriptionsRecyclerView.layoutParams.height = closedRecyclerViewHeight
                    binding.btnShowMoreSubscriptions.text = "See more"
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}