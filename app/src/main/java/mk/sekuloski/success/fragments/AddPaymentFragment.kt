package mk.sekuloski.success.fragments

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
import androidx.fragment.app.Fragment
import mk.sekuloski.success.API
import mk.sekuloski.success.ExpenseType
import mk.sekuloski.success.MainActivity
import mk.sekuloski.success.R
import mk.sekuloski.success.databinding.FragmentAddPaymentBinding
import mk.sekuloski.success.models.Location
import org.json.JSONObject
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList
import kotlin.collections.HashMap


const val KOZLE = 8


class AddPaymentFragment(_locations: HashMap<String, Int>) : Fragment(R.layout.fragment_add_payment) {
    private var _binding: FragmentAddPaymentBinding? = null
    private val binding get() = _binding!!
    private val calendar = Calendar.getInstance()
    private val api = API()
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

        binding.tvDate.setOnClickListener(View.OnClickListener {
            DatePickerDialog(
                view.context,
                date,
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH)
            ).show()
        })

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
        binding.tvTime.text = "$hour:${minute.toString().padStart(2, '0')}:00"

        val locationSpinner: Spinner = binding.spLocation
        val paymentTypeSpinner: Spinner = binding.spPaymentType

        val locationAdapter: ArrayAdapter<String> = ArrayAdapter<String>(
            view.context,
            R.layout.spinner_item,
            locations.keys.toList()
        )
        println(locations.keys.toList())
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
        val jsonObject = JSONObject()

        if (binding.etAmount.text.toString() == "") {
            binding.etAmount.error = "Amount is required!"
        } else if (binding.etPaymentName.text.toString() == "") {
            binding.etPaymentName.error = "Payment name is required!"
        } else {
            jsonObject.put("amount", binding.etAmount.text.toString().toInt())
            jsonObject.put("name", binding.etPaymentName.text)
            jsonObject.put("date", "${binding.tvDate.text}T${binding.tvTime.text}+01:00")
            jsonObject.put("necessary", binding.cbNecessary.isChecked)
            jsonObject.put("expense_type", binding.spPaymentType.selectedItemId)
            jsonObject.put("location", locations[binding.spLocation.selectedItem])
            jsonObject.put("pay", pay)
            jsonObject.put("cash", binding.cbCash.isChecked)
            api.addPayment(jsonObject)

            parentFragmentManager.popBackStack()
        }
    }

    /*
    {
        "amount": 207,
        "date": "2023-05-18T16:00:00+01:00",
        "name": "Drinks for Niki and Petar",
        "necessary": true,
        "expense_type": 6,
        "location": 15,
        "pay": true,
        "cash": false,
        "parts": {
            "Koka Kola x2 1.75l": 138,
            "Chipsy Chips Domakjinski": 69
        }
    }
    */

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}