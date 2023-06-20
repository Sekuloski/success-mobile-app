package mk.sekuloski.success.fragments.finances

import android.annotation.SuppressLint
import android.app.DatePickerDialog
import android.app.DatePickerDialog.OnDateSetListener
import android.app.TimePickerDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Spinner
import android.widget.Toast
import androidx.fragment.app.Fragment
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch
import mk.sekuloski.success.*
import mk.sekuloski.success.data.remote.services.finances.FinancesService
import mk.sekuloski.success.data.remote.dto.finances.ExpenseType
import mk.sekuloski.success.data.remote.dto.finances.PaymentRequest
import mk.sekuloski.success.databinding.FragmentAddPaymentBinding
import java.text.SimpleDateFormat
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter
import java.util.*
import kotlin.collections.HashMap


const val KOZLE = 8


class AddPaymentFragment(_locations: HashMap<String, Int>, _client: FinancesService) : Fragment(R.layout.fragment_add_payment), CoroutineScope by MainScope() {
    private var _binding: FragmentAddPaymentBinding? = null
    private val binding get() = _binding!!
    private val calendar = Calendar.getInstance()
    private val client = _client
    private val locations = _locations

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAddPaymentBinding.inflate(inflater, container, false)
        return binding.root
    }

    @SuppressLint("SetTextI18n")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val dateFormat = "yyyy-MM-dd"
        val sdf = SimpleDateFormat(dateFormat, Locale.UK)

        val date =
            OnDateSetListener { _, year, month, day ->
                calendar.set(Calendar.YEAR, year)
                calendar.set(Calendar.MONTH, month)
                calendar.set(Calendar.DAY_OF_MONTH, day)

                binding.tvDate.text = sdf.format(calendar.time)
            }

        binding.tvDate.setOnClickListener {
            DatePickerDialog(
                view.context,
                date,
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH)
            ).show()
        }

        val hour = calendar.get(Calendar.HOUR_OF_DAY)
        val minute = calendar.get(Calendar.MINUTE)
        binding.tvTime.setOnClickListener {

            val timePickerDialog = TimePickerDialog(
                view.context,
                { _, hourOfDay, minuteOfDay ->
                    binding.tvTime.text = "$hourOfDay:${minuteOfDay.toString().padStart(2, '0')}:00"
                },
                hour,
                minute,
                true
            )
            timePickerDialog.show()
        }

        binding.tvDate.text = sdf.format(calendar.time)
        binding.tvTime.text = "${hour.toString().padStart(2, '0')}:${minute.toString().padStart(2, '0')}:00"
        binding.etPayments.setText("1")

        val locationSpinner: Spinner = binding.spLocation
        val paymentTypeSpinner: Spinner = binding.spPaymentType

        val locationAdapter: ArrayAdapter<String> = ArrayAdapter<String>(
            view.context,
            R.layout.spinner_item,
            locations.keys.toList()
        )
        locationAdapter.setDropDownViewResource(R.layout.spinner_item)

        locationSpinner.adapter = locationAdapter
        locationSpinner.setSelection(KOZLE)

        val paymentTypeAdapter: ArrayAdapter<ExpenseType> = ArrayAdapter<ExpenseType>(
            view.context,
            R.layout.spinner_item,
            ExpenseType.values()
        )
        paymentTypeAdapter.setDropDownViewResource(R.layout.spinner_item)

        paymentTypeSpinner.adapter = paymentTypeAdapter
        paymentTypeSpinner.setSelection(0)

        binding.btnAdd.setOnClickListener {
            addPayment(false)
        }

        binding.btnPay.setOnClickListener {
            addPayment(true)
        }
    }

    private fun addPayment(pay: Boolean) {
        if (binding.etAmount.text.toString() == "") {
            binding.etAmount.error = "Amount is required!"
        } else if (binding.etPaymentName.text.toString() == "") {
            binding.etPaymentName.error = "Payment name is required!"
        } else {
            val amount = binding.etAmount.text.toString().toInt()
            val name = binding.etPaymentName.text.toString()
            val dateString = "${binding.tvDate.text}T${binding.tvTime.text}+02:00"
            val necessary = binding.cbNecessary.isChecked
            val expenseType = binding.spPaymentType.selectedItemId.toInt()
            val location = locations[binding.spLocation.selectedItem] ?: 9

            val date = ZonedDateTime.parse(dateString, DateTimeFormatter.ISO_DATE_TIME)

            val payments = binding.etPayments.text.toString().toInt()
            val monthly = payments != 1
            val cash = binding.cbCash.isChecked

            val credit = false
            val interest = 0.0

            val paymentRequest = PaymentRequest(amount, name, date, necessary, expenseType, cash, monthly, payments, credit, interest, location, pay)

            launch {
                val toast = Toast(context)
                toast.setText(client.addPayment(paymentRequest))
                toast.show()
                parentFragmentManager.popBackStack()
            }
        }
    }

    override fun onResume() {
        super.onResume()
        (context as MainActivity).supportActionBar?.title = "Add Payment"
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}