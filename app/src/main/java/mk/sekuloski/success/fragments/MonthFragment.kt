package mk.sekuloski.success.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import mk.sekuloski.success.*
import mk.sekuloski.success.adapter.PaymentAdapter
import mk.sekuloski.success.databinding.FragmentMonthBinding
import mk.sekuloski.success.models.Month
import mk.sekuloski.success.models.Payment
import org.json.JSONArray
import java.util.stream.Collectors

const val openedRecyclerViewHeight = 480
const val closedRecyclerViewHeight = 40

class MonthFragment(_month: Month) : Fragment(R.layout.fragment_month) {
    private var _binding: FragmentMonthBinding? = null
    private val binding get() = _binding!!
    private val api = APISingleton.getInstance()
    private val month: Month = _month

    private var normalPayments = api?.getPayments(month.normalPayments) ?: ArrayList()
    private var sixMonthPayments = api?.getPayments(month.sixMonthPayments) ?: ArrayList()
    private val threeMonthPayments = api?.getPayments(month.threeMonthPayments) ?: ArrayList()

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
        val normalPaymentsRecyclerView = binding.rvPayments
        val sixMonthPaymentsRecyclerView = binding.rvSixMonthPayments
        val threeMonthPaymentsRecyclerView = binding.rvThreeMonthPayments
        val fullNormalAdapter = PaymentAdapter(view.context, normalPayments)
        val fullSixMonthAdapter = PaymentAdapter(view.context, sixMonthPayments)
        val fullThreeMonthAdapter = PaymentAdapter(view.context, threeMonthPayments)
        val emptyAdapter = PaymentAdapter(view.context, ArrayList())

        binding.monthName.text = month.name
        binding.amountLeft.text = month.amountLeft.toString()
        binding.expensesAmount.text = month.expenses.toString()
        binding.tvNormalSum.text = month.normalSum.toString()
        binding.tvSixMonthSum.text = month.sixMonthSum.toString()
        binding.tvThreeMonthSum.text = month.threeMonthSum.toString()

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

        normalPaymentsRecyclerView.adapter = emptyAdapter
        normalPaymentsRecyclerView.layoutParams.height = closedRecyclerViewHeight
        normalPaymentsRecyclerView.setHasFixedSize(true)

        sixMonthPaymentsRecyclerView.adapter = emptyAdapter
        sixMonthPaymentsRecyclerView.layoutParams.height = closedRecyclerViewHeight
        sixMonthPaymentsRecyclerView.setHasFixedSize(true)

        threeMonthPaymentsRecyclerView.adapter = emptyAdapter
        threeMonthPaymentsRecyclerView.layoutParams.height = closedRecyclerViewHeight
        threeMonthPaymentsRecyclerView.setHasFixedSize(true)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}