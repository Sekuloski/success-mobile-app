package mk.sekuloski.success.fragments.finances

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch
import mk.sekuloski.success.MainActivity
import mk.sekuloski.success.R
import mk.sekuloski.success.data.remote.dto.finances.Payment
import mk.sekuloski.success.data.remote.services.FinancesService
import mk.sekuloski.success.databinding.FragmentMonthBinding
import mk.sekuloski.success.databinding.FragmentPaymentBinding


class PaymentFragment(_payment: Payment) : Fragment(R.layout.fragment_payment), CoroutineScope by MainScope() {
    private var _binding: FragmentPaymentBinding? = null
    private val binding get() = _binding!!
    private val payment = _payment
    private val client = FinancesService.create()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentPaymentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        (context as MainActivity).supportActionBar?.title = payment.name
        binding.tvPaymentCost.text = payment.amount.toString()
        binding.tvPaymentDate.text = payment.date.toString()
        binding.tvPaymentPaid.text = payment.paid.toString()
        binding.btnDelete.setOnClickListener {
            launch {
                val toast = Toast(context)
                toast.setText(client.deletePayment(payment.id, false).toString())
                toast.show()
                parentFragmentManager.popBackStack()
            }
        }
    }
}