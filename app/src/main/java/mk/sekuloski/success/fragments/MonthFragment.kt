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

class MonthFragment(_month: Month) : Fragment(R.layout.fragment_month) {
    private var _binding: FragmentMonthBinding? = null
    private val binding get() = _binding!!
    private val month: Month = _month
    private val paymentIds: JSONArray = _month.payments
    private val api = API()

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

        val payments = api.getPayments(paymentIds)

        while(true)
        {
            if (payments.size == 0)
            {
                continue
            }

            break
        }

        val fullNormalAdapter = PaymentAdapter(view.context, payments)
        val emptyAdapter = PaymentAdapter(view.context, ArrayList())

        binding.monthName.text = month.name
        binding.amountLeft.text = month.amountLeft.toString()

        binding.btnShowMoreNormal.setOnClickListener {
            if (binding.btnShowMoreNormal.text.toString() == "See more") {
                normalPaymentsRecyclerView.swapAdapter(fullNormalAdapter, true)
                normalPaymentsRecyclerView.layoutParams.height = 450
                binding.btnShowMoreNormal.text = "See less"
            } else {
                normalPaymentsRecyclerView.swapAdapter(emptyAdapter, true)
                normalPaymentsRecyclerView.layoutParams.height = 40
                binding.btnShowMoreNormal.text = "See more"
            }
        }

//        binding.btnShowMoreSixMonth.setOnClickListener {
//            if (binding.btnShowMoreSixMonth.text.toString() == "See more") {
//                sixMonthPaymentsRecyclerView.swapAdapter(fullSixMonthAdapter, true)
//                sixMonthPaymentsRecyclerView.layoutParams.height = 450
//                binding.btnShowMoreSixMonth.text = "See less"
//            } else {
//                sixMonthPaymentsRecyclerView.swapAdapter(emptyAdapter, true)
//                sixMonthPaymentsRecyclerView.layoutParams.height = 40
//                binding.btnShowMoreSixMonth.text = "See more"
//            }
//        }

        normalPaymentsRecyclerView.adapter = emptyAdapter
        normalPaymentsRecyclerView.layoutParams.height = 40
        normalPaymentsRecyclerView.setHasFixedSize(true)

        sixMonthPaymentsRecyclerView.adapter = emptyAdapter
        sixMonthPaymentsRecyclerView.layoutParams.height = 40
        sixMonthPaymentsRecyclerView.setHasFixedSize(true)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}