package mk.sekuloski.success.fragments.finances

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.DatePicker
import android.widget.Spinner
import android.widget.Toast
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.Checkbox
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.fragment.app.Fragment
import com.google.android.gms.location.FusedLocationProviderClient
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch
import mk.sekuloski.success.R
import mk.sekuloski.success.data.remote.dto.finances.ExpenseType
import mk.sekuloski.success.data.remote.services.finances.FinancesService
import mk.sekuloski.success.data.remote.dto.finances.Location
import mk.sekuloski.success.data.remote.dto.finances.PaymentRequest
import mk.sekuloski.success.databinding.FragmentAddPaymentBinding
import mk.sekuloski.success.ui.theme.AppTheme
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter
import java.util.*
import kotlin.collections.ArrayList
import kotlin.collections.HashMap


class AddPaymentFragment(
    private val locations: List<Location>,
    private val client: FinancesService
) : Fragment(R.layout.fragment_add_payment), CoroutineScope by MainScope() {

    private var _binding: FragmentAddPaymentBinding? = null
    private val binding get() = _binding!!
    private val calendar = Calendar.getInstance()
    private val permissionId = 2
    private lateinit var locationSpinner: Spinner
    private lateinit var mFusedLocationClient: FusedLocationProviderClient

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAddPaymentBinding.inflate(
            inflater, container, false
        ).apply {
            composeView.setContent {
                Main()
            }
        }
        return binding.root
    }

    @Composable
    fun Main() {
        AppTheme {
            val currencies = listOf("Bank", "Cash", "Euros", "Bank Euros")
            val context = LocalContext.current
            val year = calendar[Calendar.YEAR]
            val month = calendar[Calendar.MONTH]
            val dayOfMonth = calendar[Calendar.DAY_OF_MONTH]
            val hour = calendar[Calendar.HOUR_OF_DAY]
            val minute = calendar[Calendar.MINUTE]

            var amount by remember { mutableStateOf("") }
            var name by remember { mutableStateOf("") }
            var dateText by remember { mutableStateOf(getDate(dayOfMonth, month, year)) }
            var timeText by remember { mutableStateOf(getTime(hour, minute)) }
            var currency by remember { mutableStateOf("Bank") }
            var location by remember { mutableStateOf(locations[0]) }
            var expenseType by remember { mutableStateOf(ExpenseType.BILL) }
            var numberOfPayments by remember { mutableStateOf(1) }
            var necessary by remember { mutableStateOf(false) }
            val scope = rememberCoroutineScope()

            val datePicker = DatePickerDialog(
                context,
                { _: DatePicker, selectedYear: Int, selectedMonth: Int, selectedDayOfMonth: Int ->
                    dateText =
                        getDate(selectedDayOfMonth, selectedMonth, selectedYear)
                }, year, month, dayOfMonth
            )
            val timePicker = TimePickerDialog(
                context,
                { _, selectedHour: Int, selectedMinute: Int ->
                    timeText = getTime(selectedHour, selectedMinute)
                }, hour, minute, false
            )

            Box(Modifier.fillMaxSize()) {
                Column(
                    modifier = Modifier
                        .align(Alignment.TopCenter)
                        .fillMaxSize()
                        .padding(16.dp)
                ) {
                    OutlinedTextField(
                        modifier = Modifier.fillMaxWidth(),
                        value = amount,
                        onValueChange = { amount = it },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        label = { Text("Amount") },
                        maxLines = 1,
                    )
                    OutlinedTextField(
                        modifier = Modifier.fillMaxWidth(),
                        value = name,
                        onValueChange = { name = it },
                        label = { Text("Name") },
                        maxLines = 1,
                    )

                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 10.dp),
                        horizontalArrangement = Arrangement.SpaceAround
                    ) {
                        Text(
                            text = dateText.ifEmpty {
                                "Please pick a date"
                            },
                            modifier = Modifier
                                .clickable {
                                    datePicker.show()
                                },
                            color = MaterialTheme.colorScheme.onBackground,
                            fontSize = 28.sp
                        )
                        Text(
                            text = timeText.ifEmpty {
                                "Please pick a time"
                            },
                            modifier = Modifier
                                .clickable {
                                    timePicker.show()
                                },
                            color = MaterialTheme.colorScheme.onBackground,
                            fontSize = 28.sp
                        )
                    }
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(10.dp),
                        horizontalArrangement = Arrangement.Start
                    ) {
                        Box(modifier = Modifier.weight(1f)) {
                            Currency(currencies, currency) { currency = it }
                        }
                        Spacer(modifier = Modifier.width(16.dp))
                        Box(modifier = Modifier.weight(1f)) {
                            Locations(locations, location) { location = it }
                        }
                        Spacer(modifier = Modifier.width(16.dp))
                        Box(modifier = Modifier.weight(1f)) {
                            ExpenseTypes(ExpenseType.values().toList(), expenseType) {
                                expenseType = it
                            }
                        }
                    }

                    Row {
                        Row(modifier = Modifier.weight(1f)) {
                            Button(
                                onClick = {
                                    if (numberOfPayments != 1) numberOfPayments--
                                },
                                modifier = Modifier.padding(end = 8.dp)
                            ) {
                                Text(text = "-")
                            }

                            Text(
                                text = numberOfPayments.toString(),
                                modifier = Modifier.padding(4.dp),
                                color = MaterialTheme.colorScheme.onBackground,
                                fontSize = 28.sp
                            )

                            Button(
                                onClick = { numberOfPayments++ },
                                modifier = Modifier.padding(start = 8.dp)
                            ) {
                                Text(text = "+")
                            }
                        }

                        Spacer(modifier = Modifier.width(16.dp))

                        Checkbox(
                            checked = necessary,
                            onCheckedChange = { necessary = it },
                        )

                        Text(
                            text = "Necessary",
                            modifier = Modifier
                                .padding(4.dp)
                                .clickable { necessary = !necessary },
                            color = MaterialTheme.colorScheme.onBackground,
                            fontSize = 28.sp
                        )
                    }
                }
                Row(
                    modifier = Modifier
                        .align(Alignment.BottomCenter)
                        .fillMaxWidth()
                ) {
                    OutlinedButton(
                        onClick = {
                            val dateString = "${dateText}T${timeText}+02:00"
                            val date =
                                ZonedDateTime.parse(dateString, DateTimeFormatter.ISO_DATE_TIME)
                            val request = PaymentRequest(
                                amount = amount.toInt(),
                                name = name,
                                date = date,
                                necessary = necessary,
                                expense_type = expenseType.ordinal,
                                cash = currency == "Cash",
                                euros = false,
                                monthly = numberOfPayments > 1,
                                payments = numberOfPayments,
                                credit = false,
                                interest = 0.0,
                                location = location.id,
                                pay = false
                            )

                            scope.launch {
                                val toast = Toast(context)
                                toast.setText(client.addPayment(request))
                                toast.show()
                                parentFragmentManager.popBackStack()
                            }
                        },
                        modifier = Modifier
                            .weight(1f)
                            .padding(16.dp)
                    ) {
                        Text(text = "Add", fontSize = 22.sp)
                    }
                    Button(
                        onClick = {
                            val dateString = "${dateText}T${timeText}+02:00"
                            val date =
                                ZonedDateTime.parse(dateString, DateTimeFormatter.ISO_DATE_TIME)
                            val request = PaymentRequest(
                                amount = amount.toInt(),
                                name = name,
                                date = date,
                                necessary = necessary,
                                expense_type = expenseType.ordinal,
                                cash = currency == "Cash",
                                euros = currency == "Euros",
                                monthly = numberOfPayments > 1,
                                payments = numberOfPayments,
                                credit = false,
                                interest = 0.0,
                                location = location.id,
                                pay = true
                            )
                            scope.launch {
                                val toast = Toast(context)
                                toast.setText(client.addPayment(request))
                                toast.show()
                                parentFragmentManager.popBackStack()
                            }
                        },
                        modifier = Modifier
                            .weight(1f)
                            .padding(16.dp)
                    ) {
                        Text(text = "Pay", fontSize = 22.sp)
                    }
                }
            }

        }
    }

    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    fun Currency(
        currencies: List<String>,
        selectedCurrency: String,
        onItemSelected: (String) -> Unit
    ) {
        var expanded by remember { mutableStateOf(false) }

        Box(
            modifier = Modifier
                .wrapContentSize(Alignment.TopStart)
        ) {
            ExposedDropdownMenuBox(
                expanded = expanded,
                onExpandedChange = {
                    expanded = !expanded
                }
            ) {
                TextField(
                    value = selectedCurrency,
                    onValueChange = {},
                    readOnly = true,
                    trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
                    modifier = Modifier.menuAnchor()
                )

                ExposedDropdownMenu(
                    expanded = expanded,
                    onDismissRequest = { expanded = false }
                ) {
                    currencies.forEach { currency ->
                        DropdownMenuItem(
                            text = { Text(text = currency) },
                            onClick = {
                                onItemSelected(currency)
                                expanded = false
                            }
                        )
                    }
                }
            }
        }
    }


    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    fun Locations(
        locations: List<Location>,
        selectedLocation: Location,
        onItemSelected: (Location) -> Unit
    ) {
        var expanded by remember { mutableStateOf(false) }

        Box(
            modifier = Modifier
                .wrapContentSize(Alignment.TopStart)
        ) {
            ExposedDropdownMenuBox(
                expanded = expanded,
                onExpandedChange = {
                    expanded = !expanded
                }
            ) {
                TextField(
                    value = selectedLocation.name,
                    onValueChange = {},
                    readOnly = true,
                    trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
                    modifier = Modifier.menuAnchor()
                )

                ExposedDropdownMenu(
                    expanded = expanded,
                    onDismissRequest = { expanded = false }
                ) {
                    locations.forEach { location ->
                        DropdownMenuItem(
                            text = { Text(text = location.name) },
                            onClick = {
                                onItemSelected(location)
                                expanded = false
                            }
                        )
                    }
                }
            }
        }
    }

    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    fun ExpenseTypes(
        expenseTypes: List<ExpenseType>,
        selectedType: ExpenseType,
        onItemSelected: (ExpenseType) -> Unit
    ) {
        var expanded by remember { mutableStateOf(false) }

        Box(
            modifier = Modifier
                .wrapContentSize(Alignment.TopStart)
        ) {
            ExposedDropdownMenuBox(
                expanded = expanded,
                onExpandedChange = {
                    expanded = !expanded
                }
            ) {
                TextField(
                    value = selectedType.name,
                    onValueChange = {},
                    readOnly = true,
                    trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
                    modifier = Modifier.menuAnchor()
                )

                ExposedDropdownMenu(
                    expanded = expanded,
                    onDismissRequest = { expanded = false }
                ) {
                    expenseTypes.forEach { expenseType ->
                        DropdownMenuItem(
                            text = { Text(text = expenseType.name) },
                            onClick = {
                                onItemSelected(expenseType)
                                expanded = false
                            }
                        )
                    }
                }
            }
        }
    }


    private fun getDate(dayOfMonth: Int, month: Int, year: Int) =
        year.toString() + "-" +
                (month + 1).toString().padStart(2, '0') + "-" +
                dayOfMonth.toString().padStart(2, '0')

    private fun getTime(hour: Int, minute: Int) =
        "${hour.toString().padStart(2, '0')}:${minute.toString().padStart(2, '0')}"

//    @SuppressLint("SetTextI18n")
//    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
//        super.onViewCreated(view, savedInstanceState)
//
//        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(requireActivity())
//        getLocation()
//
//        locationSpinner = binding.spLocation
//        val paymentTypeSpinner: Spinner = binding.spPaymentType
//        locationAdapter.setDropDownViewResource(R.layout.spinner_item)
//
//        locationSpinner.adapter = locationAdapter
//
//        val paymentTypeAdapter: ArrayAdapter<ExpenseType> = ArrayAdapter<ExpenseType>(
//            view.context,
//            R.layout.spinner_item,
//            ExpenseType.values()
//        )
//        paymentTypeAdapter.setDropDownViewResource(R.layout.spinner_item)
//
//        paymentTypeSpinner.adapter = paymentTypeAdapter
//        paymentTypeSpinner.setSelection(0)
//
//    }
//
//    private fun addPayment(pay: Boolean) {
//        if (binding.etAmount.text.toString() == "") {
//            binding.etAmount.error = "Amount is required!"
//        } else if (binding.etPaymentName.text.toString() == "") {
//            binding.etPaymentName.error = "Payment name is required!"
//        } else {
//            val expenseType = binding.spPaymentType.selectedItemId.toInt()
//            val location = locationsMap[binding.spLocation.selectedItem] ?: 9
//
//            val credit = false
//            val interest = 0.0
//
//            val paymentRequest = PaymentRequest(amount, name, date, necessary, expenseType, cash, false, monthly, payments, credit, interest, location, pay)
//
//        }
//    }
//
//    private fun isLocationEnabled(): Boolean {
//        val locationManager: LocationManager = requireContext().getSystemService(Context.LOCATION_SERVICE) as LocationManager
//        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) || locationManager.isProviderEnabled(
//            LocationManager.NETWORK_PROVIDER
//        )
//    }
//
//    private fun checkPermissions(): Boolean {
//        if (ActivityCompat.checkSelfPermission(
//                requireContext(),
//                android.Manifest.permission.ACCESS_COARSE_LOCATION
//            ) == PackageManager.PERMISSION_GRANTED &&
//            ActivityCompat.checkSelfPermission(
//                requireContext(),
//                android.Manifest.permission.ACCESS_FINE_LOCATION
//            ) == PackageManager.PERMISSION_GRANTED
//        ) {
//            return true
//        }
//        return false
//    }
//    private fun requestPermissions() {
//        ActivityCompat.requestPermissions(
//            requireActivity(),
//            arrayOf(
//                android.Manifest.permission.ACCESS_COARSE_LOCATION,
//                android.Manifest.permission.ACCESS_FINE_LOCATION
//            ),
//            permissionId
//        )
//    }
//    @Deprecated("Deprecated in Java")
//    @SuppressLint("MissingSuperCall")
//    override fun onRequestPermissionsResult(
//        requestCode: Int,
//        permissions: Array<String>,
//        grantResults: IntArray
//    ) {
//        if (requestCode == permissionId) {
//            if ((grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
//                getLocation()
//            }
//        }
//    }
//
//    @SuppressLint("MissingPermission", "SetTextI18n")
//    private fun getLocation() {
//        if (checkPermissions()) {
//            if (isLocationEnabled()) {
//                mFusedLocationClient.lastLocation.addOnCompleteListener(requireActivity()) { task ->
//                    val location: android.location.Location? = task.result
//                    if (location != null) {
//                        locationSpinner.setSelection(locationsMap[findClosestLocation(location.latitude, location.longitude, locations)?.name.toString()]?.minus(
//                            1
//                        ) ?: 9)
//                    }
//                }
//            } else {
//                Toast.makeText(requireContext(), "Please turn on location", Toast.LENGTH_LONG).show()
//                val intent = Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)
//                startActivity(intent)
//            }
//        } else {
//            requestPermissions()
//            getLocation()
//        }
//    }
//


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}