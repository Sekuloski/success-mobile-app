package mk.sekuloski.success.fragments.finances

import android.app.AlertDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch
import mk.sekuloski.success.*
import mk.sekuloski.success.adapter.finances.MonthAdapter
import mk.sekuloski.success.data.remote.dto.finances.ExpenseType
import mk.sekuloski.success.data.remote.services.finances.FinancesService
import mk.sekuloski.success.data.remote.dto.finances.Location
import mk.sekuloski.success.databinding.FragmentFinancesBinding
import mk.sekuloski.success.data.remote.dto.finances.Month
import mk.sekuloski.success.data.remote.dto.finances.Payment
import mk.sekuloski.success.utils.initPie
import mk.sekuloski.success.utils.resetCategories

class FinancesFragment(private val client: FinancesService) : Fragment(R.layout.fragment_finances), CoroutineScope by MainScope() {
    private var _binding: FragmentFinancesBinding? = null
    private val binding get() = _binding!!
    private val selectedCategories: BooleanArray = BooleanArray(ExpenseType.values().size)
    private var expenseList = ExpenseType.values().toMutableList()
    private lateinit var locations: ArrayList<Location>
    private lateinit var months: ArrayList<Month>
    private lateinit var payments: List<Payment>
    private var categories = ArrayList<Int>()

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

        (context as MainActivity).supportActionBar?.title = "Finances"
        launch {
            months = client.getMonths()
            monthsRecyclerView.adapter = MonthAdapter(requireContext(), months, client)

            locations = client.getLocations()

            val salaryReceived = client.getSalaryInfo()
            if (!salaryReceived)
            {
                binding.addSalary.visibility = View.VISIBLE
            }

            payments = client.getMonthPayments()

            binding.fabAddPayment.visibility = View.VISIBLE

            configurePie()

            selectedCategories.fill(true)
            binding.tvCategories.setOnClickListener {
                val builder = AlertDialog.Builder(
                    it.context
                )
                builder.setTitle("Categories")
                builder.setMultiChoiceItems(ExpenseType.getValues(), selectedCategories) { _, which, isChecked ->
                    if (isChecked) {
                        expenseList.add(ExpenseType.values()[which])
                    } else {
                        expenseList.remove(ExpenseType.values()[which])
                    }
                }
                builder.setPositiveButton("OK") { _, _ ->
                    resetCategories()
                    configurePie()
                }
                builder.setNegativeButton("Cancel") {_, _ ->
                    selectedCategories.fill(true)
                    expenseList = ExpenseType.values().toMutableList()
                }
                builder.setNeutralButton("Select All") {_, _ ->
                    selectedCategories.fill(true)
                    expenseList = ExpenseType.values().toMutableList()
                    configurePie()
                }
                builder.show()
            }
        }

        monthsRecyclerView.setHasFixedSize(true)

        binding.swipeRefresh.setOnRefreshListener {
            onRefresh(monthsRecyclerView)
        }

        binding.fabAddPayment.setOnClickListener {
            (context as MainActivity).supportFragmentManager.beginTransaction().apply {
                replace(R.id.flFragment, AddPaymentFragment(locations, locations.associate { it.name to it.id } as HashMap<String, Int>, client))
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

    private fun onRefresh(monthsRecyclerView: RecyclerView) {
        launch {
            months = client.getMonths()
            monthsRecyclerView.adapter = MonthAdapter(requireContext(), months, client)
            monthsRecyclerView.swapAdapter(MonthAdapter(requireContext(), months, client), true)
            val salaryReceived = client.getSalaryInfo()
            if (!salaryReceived) {
                binding.addSalary.visibility = View.VISIBLE
            }

            payments = client.getMonthPayments()
            binding.fabAddPayment.visibility = View.VISIBLE
            configurePie()

            binding.swipeRefresh.isRefreshing = false
        }
    }

    private fun configurePie()
    {
        categories = resetCategories()
        for (payment: Payment in payments) {
            if (ExpenseType.values()[payment.expense_type] in expenseList && payment.amount > 0)
                categories[payment.expense_type] += payment.amount
        }
        initPie(
            binding.pieChart,
            requireContext(),
            categories
        )
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}