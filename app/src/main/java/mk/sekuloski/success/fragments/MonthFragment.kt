package mk.sekuloski.success.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import mk.sekuloski.success.*
import mk.sekuloski.success.adapter.MonthAdapter
import mk.sekuloski.success.adapter.PaymentAdapter
import mk.sekuloski.success.databinding.FragmentFinancesBinding
import mk.sekuloski.success.databinding.FragmentMonthBinding
import mk.sekuloski.success.models.Month
import mk.sekuloski.success.models.Payment
import okhttp3.OkHttpClient

class MonthFragment(_month: Month) : Fragment(R.layout.fragment_month) {
    private var _binding: FragmentMonthBinding? = null
    private val binding get() = _binding!!
    private val month: Month = _month
    private val payments: List<Payment> = month.payments

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMonthBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.monthName.text = month.name
        binding.amountLeft.text = month.amountLeft.toString()
        binding.expensesAmount.text = month.expenses.toString()

        val paymentsRecyclerView = binding.rvPayments
        paymentsRecyclerView.adapter = PaymentAdapter(view.context, payments)
        paymentsRecyclerView.setHasFixedSize(true)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}