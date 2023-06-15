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
import kotlinx.serialization.json.JsonArray
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.JsonPrimitive
import mk.sekuloski.success.MainActivity
import mk.sekuloski.success.R
import mk.sekuloski.success.adapter.finances.PaymentAdapter
import mk.sekuloski.success.data.remote.dto.finances.Payment
import mk.sekuloski.success.data.remote.services.FinancesService
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
                toast.setText(client.deletePayment(payment.id, false))
                toast.show()
                parentFragmentManager.popBackStack()
            }
        }
        if (payment.monthly)
        {
            launch {
                try
                {
                    val id = payment.id
                    val parts = payment.name.split(" ").last().split("/")
                    val startId = id + 1 - parts[0].toInt()
                    val array = ArrayList<JsonElement>()
                    for (i in 0 until parts[1].toInt()) array.add(JsonPrimitive(startId + i))
                    val paymentIds = JsonArray(array)
                    val adapter = PaymentAdapter(view.context, client.getPayments(paymentIds))

                    val monthlyPaymentsRecyclerView = binding.rvMonthlyPayments
                    monthlyPaymentsRecyclerView.adapter = adapter
                    monthlyPaymentsRecyclerView.setHasFixedSize(true)
                }
                catch (e: java.lang.NumberFormatException)
                {
                    val toast = Toast(view.context)
                    toast.setText("Not a valid monthly payment!")
                    toast.show()
                }
            }
        }
    }
}