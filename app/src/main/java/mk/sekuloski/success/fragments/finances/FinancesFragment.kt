package mk.sekuloski.success.fragments.finances

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.graphics.Typeface
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.github.mikephil.charting.animation.Easing
import com.github.mikephil.charting.charts.PieChart
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch
import mk.sekuloski.success.*
import mk.sekuloski.success.adapter.finances.MonthAdapter
import mk.sekuloski.success.data.remote.services.FinancesService
import mk.sekuloski.success.data.remote.dto.finances.Location
import mk.sekuloski.success.databinding.FragmentFinancesBinding
import mk.sekuloski.success.data.remote.dto.finances.Month
import mk.sekuloski.success.utils.CustomPieChartRenderer
import mk.sekuloski.success.utils.setData

class FinancesFragment(_client: FinancesService) : Fragment(R.layout.fragment_finances), CoroutineScope by MainScope() {
    private var _binding: FragmentFinancesBinding? = null
    private val binding get() = _binding!!
    private val client = _client
    private lateinit var locations: ArrayList<Location>
    private lateinit var months: ArrayList<Month>
    private lateinit var pieChart: PieChart

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentFinancesBinding.inflate(inflater, container, false)
        return binding.root
    }

    @SuppressLint("SetTextI18n")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val monthsRecyclerView = binding.rvMonths

        launch {
            months = client.getMonths()
            locations = client.getLocations()
            val salaryReceived = client.getSalaryInfo()
            if (!salaryReceived)
            {
                binding.addSalary.visibility = View.VISIBLE
            }
            monthsRecyclerView.adapter = MonthAdapter(view.context, months, client)

            val newData = client.getMainInfo()
            if (newData != null)
            {
                initPie(
                    newData.groceries,
                    newData.takeaway_food,
                    newData.football,
                    newData.hanging_out,
                    newData.music_gear,
                    newData.sports_gear
                )
            }
        }

        monthsRecyclerView.setHasFixedSize(true)

        binding.swipeRefresh.setOnRefreshListener {
            launch {
                months = client.getMonths()
                monthsRecyclerView.adapter = MonthAdapter(view.context, months, client)
                monthsRecyclerView.swapAdapter(MonthAdapter(view.context, months, client), true)
                binding.swipeRefresh.isRefreshing = false
            }
        }

        binding.fabAddPayment.setOnClickListener {
            (context as MainActivity).supportFragmentManager.beginTransaction().apply {
                replace(R.id.flFragment, AddPaymentFragment(locations.associate { it.name to it.id } as HashMap<String, Int>, client))
                addToBackStack(null)
                commit()
            }
        }

        binding.addSalary.setOnClickListener { it ->
            val dialogLayout = layoutInflater.inflate(R.layout.add_salary, null)
            val waterBillAmount = dialogLayout.findViewById<EditText>(R.id.etWaterBill)
            val powerBillAmount = dialogLayout.findViewById<EditText>(R.id.etPowerBill)

            val dialog = AlertDialog.Builder(it.context)
                .setTitle("Enter Bills:")
                .setPositiveButton("OK") { _, _ ->
                    println("OK")
                }
                .setNegativeButton("Cancel") {_, _ ->
                    println("Cancelled")
                }
                .setView(dialogLayout)
                .show()

            dialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener {
                if (waterBillAmount.text.toString() == "") {
                    waterBillAmount.error = "Amount is required!"
                }
                else if (powerBillAmount.text.toString() == "") {
                    powerBillAmount.error = "Amount is required!"
                } else {
                    launch {
                        val response = client.addSalary(waterBillAmount.text.toString().toInt(), powerBillAmount.text.toString().toInt())
                        val toast = Toast(it.context)
                        toast.setText(response)
                        toast.show()
                        dialog.dismiss()
                    }
                }
            }

        }
    }

    private fun initPie(
        groceries: Int,
        takeaway_food: Int,
        football: Int,
        hanging_out: Int,
        music_gear: Int,
        sports_gear: Int
    ) {
        pieChart = binding.pieChart

        val colors = setData(pieChart, groceries, takeaway_food, football, hanging_out, music_gear, sports_gear)
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

    override fun onResume() {
        super.onResume()
        launch {
            months = client.getMonths()
            val salaryReceived = client.getSalaryInfo()
            if (!salaryReceived)
            {
                binding.addSalary.visibility = View.VISIBLE
            }
            binding.rvMonths.swapAdapter(context?.let { MonthAdapter(it, months, client) }, true)
            val newData = client.getMainInfo()
            if (newData != null)
            {
                initPie(newData.groceries,
                    newData.takeaway_food,
                    newData.football,
                    newData.hanging_out,
                    newData.music_gear,
                    newData.sports_gear)
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}