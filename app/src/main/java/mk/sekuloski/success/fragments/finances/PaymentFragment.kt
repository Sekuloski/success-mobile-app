package mk.sekuloski.success.fragments.finances

import android.app.AlertDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ListView
import android.widget.Toast
import androidx.fragment.app.Fragment
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
import mk.sekuloski.success.data.remote.services.finances.FinancesService
import mk.sekuloski.success.databinding.FragmentPaymentBinding


class PaymentFragment(private val payment: Payment) : Fragment(R.layout.fragment_payment), CoroutineScope by MainScope() {
    private var _binding: FragmentPaymentBinding? = null
    private val binding get() = _binding!!
    private val client = FinancesService.create()
    private lateinit var payments: List<Payment>

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
        binding.tvPaymentMonthly.text = payment.monthly.toString()
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
                    payments = client.getPayments(paymentIds)
                    val adapter = PaymentAdapter(view.context, payments)

                    val monthlyPaymentsRecyclerView = binding.rvMonthlyPayments
                    monthlyPaymentsRecyclerView.adapter = adapter
                    monthlyPaymentsRecyclerView.setHasFixedSize(true)
                }
                catch (e: java.lang.NumberFormatException)
                {
                    payments = listOf(payment)
                    payment.monthly = false
                    val toast = Toast(view.context)
                    toast.setText("Not a valid monthly payment!")
                    toast.show()
                }
            }
        }

        binding.btnDelete.setOnClickListener {
            val dialogLayout = layoutInflater.inflate(R.layout.delete_dialog, null)
            val adapter = if (payment.monthly) {
                ArrayAdapter(
                    view.context,
                    R.layout.list_payment_name,
                    payments.map { individual_payment -> individual_payment.name }
                )
            } else {
                ArrayAdapter(view.context, R.layout.list_payment_name, listOf(payment.name))
            }

            val listView = dialogLayout.findViewById<ListView>(R.id.lvPayments)
            listView.adapter = adapter

            AlertDialog.Builder(it.context)
                .setTitle("Deleting Payments")
                .setPositiveButton("Yes") { _, _ ->
                    launch {
                        val toast = Toast(context)
                        if (payment.monthly)
                        {

                            toast.setText(client.deleteMonthlyPayment(payment.name.split(" ").dropLast(1).joinToString(" ")))
                        }
                        else
                        {
                            toast.setText(client.deletePayment(payment.id, false))
                        }
                        toast.show()
                        parentFragmentManager.popBackStack()
                    }
                }
                .setNegativeButton("Cancel") {_, _ ->
                    println("Cancelled")
                }
                .setView(dialogLayout)
                .show()
        }

        binding.btnPay.setOnClickListener {
            val dialogLayout = layoutInflater.inflate(R.layout.pay_dialog, null)
            val adapter = if (payment.monthly) {
                ArrayAdapter(
                    view.context,
                    R.layout.list_payment_name,
                    payments.map { individual_payment -> individual_payment.name }
                )
            } else {
                ArrayAdapter(view.context, R.layout.list_payment_name, listOf(payment.name))
            }

            val listView = dialogLayout.findViewById<ListView>(R.id.lvPayments)
            listView.adapter = adapter

            AlertDialog.Builder(it.context)
                .setTitle("Paying Payments")
                .setPositiveButton("Yes") { _, _ ->
                    launch {
                        val toast = Toast(context)
                        if (payment.monthly)
                        {
//                            toast.setText(client.deleteMonthlyPayment(payment.name.split(" ").dropLast(1).joinToString(" ")))
                            toast.setText(client.payPayments(listOf(payment.id)))
                        }
                        else
                        {
                            toast.setText(client.payPayments(listOf(payment.id)))
                        }
                        toast.show()
                        parentFragmentManager.popBackStack()
                    }
                }
                .setNegativeButton("Cancel") {_, _ ->
                    println("Cancelled")
                }
                .setView(dialogLayout)
                .show()
        }
    }
}