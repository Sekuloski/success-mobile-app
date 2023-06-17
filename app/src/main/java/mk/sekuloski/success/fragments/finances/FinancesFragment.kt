package mk.sekuloski.success.fragments.finances

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
import mk.sekuloski.success.data.remote.dto.finances.ExpenseType
import mk.sekuloski.success.data.remote.services.FinancesService
import mk.sekuloski.success.data.remote.dto.finances.Location
import mk.sekuloski.success.databinding.FragmentFinancesBinding
import mk.sekuloski.success.data.remote.dto.finances.Month
import mk.sekuloski.success.data.remote.dto.finances.Payment
import mk.sekuloski.success.utils.CustomPieChartRenderer
import mk.sekuloski.success.utils.initPie
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

    override fun onResume() {
        super.onResume()
        val monthsRecyclerView = binding.rvMonths

        launch {
            months = client.getMonths()
            monthsRecyclerView.adapter = MonthAdapter(requireContext(), months, client)

            locations = client.getLocations()

            val salaryReceived = client.getSalaryInfo()
            if (!salaryReceived)
            {
                binding.addSalary.visibility = View.VISIBLE
            }

            val payments = client.getMonthPayments()
            var groceries = 0
            var takeawayFood = 0
            var football = 0
            var hangingOut = 0
            var musicGear = 0
            var sportsGear = 0
            var gamingGear = 0

            binding.fabAddPayment.visibility = View.VISIBLE

            for (payment: Payment in payments)
            {
                when (payment.expense_type) {
                    ExpenseType.GROCERIES.ordinal -> groceries += payment.amount
                    ExpenseType.TAKEAWAY_FOOD.ordinal -> takeawayFood += payment.amount
                    ExpenseType.FOOTBALL.ordinal -> football += payment.amount
                    ExpenseType.HANGING_OUT.ordinal -> hangingOut += payment.amount
                    ExpenseType.MUSIC_GEAR.ordinal -> musicGear += payment.amount
                    ExpenseType.SPORTS_GEAR.ordinal -> sportsGear += payment.amount
                    ExpenseType.GAMING_GEAR.ordinal -> gamingGear += payment.amount
                }
            }
            initPie(
                binding.pieChart,
                requireContext(),
                groceries,
                takeawayFood,
                football,
                hangingOut,
                musicGear,
                sportsGear,
                gamingGear
            )
        }

        monthsRecyclerView.setHasFixedSize(true)

        binding.swipeRefresh.setOnRefreshListener {
            launch {
                months = client.getMonths()
                monthsRecyclerView.adapter = MonthAdapter(requireContext(), months, client)
                monthsRecyclerView.swapAdapter(MonthAdapter(requireContext(), months, client), true)
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

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}