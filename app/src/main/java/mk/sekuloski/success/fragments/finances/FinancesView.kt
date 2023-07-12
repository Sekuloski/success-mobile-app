package mk.sekuloski.success.fragments.finances

import android.content.Context
import android.widget.Toast
import androidx.compose.animation.core.animateIntAsState
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.AlertDialogDefaults
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.TextButton
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import com.github.mikephil.charting.charts.PieChart
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import kotlinx.coroutines.launch
import mk.sekuloski.success.finances.domain.model.ExpenseType
import mk.sekuloski.success.finances.data.remote.FinancesService
import mk.sekuloski.success.finances.domain.model.Month
import mk.sekuloski.success.finances.domain.model.Payment
import mk.sekuloski.success.finances.domain.model.Subscription
import mk.sekuloski.success.destinations.AddPaymentScreenDestination
import mk.sekuloski.success.destinations.MonthsScreenDestination
import mk.sekuloski.success.ui.theme.AppTheme
import mk.sekuloski.success.utils.initPie
import mk.sekuloski.success.utils.resetCategories
import java.time.LocalDate
import java.time.format.DateTimeFormatter

@Destination
@Composable
fun FinancesMainScreen(
    navigator: DestinationsNavigator,
) {
    AppTheme {
        val client = FinancesService.create()
        val context = LocalContext.current
        var salaryReceived by remember {
            mutableStateOf(true)
        }
        var payments by remember {
            mutableStateOf(emptyList<Payment>())
        }
        var subscriptions by remember {
            mutableStateOf(emptyList<Subscription>())
        }
        LaunchedEffect(key1 = true) {
            salaryReceived = client.getSalaryInfo()
            payments = client.getMonthPayments()
            subscriptions = client.getSubscriptions()
        }
        val modifier = Modifier.fillMaxSize()
        Column(modifier) {
            Box(modifier = modifier.weight(1f)) {
                MonthsList(modifier, client, navigator)
            }
            Box(modifier = modifier.weight(1.2f)) {
                if (payments.isNotEmpty()) {
                    AndroidView(
                        modifier = modifier
                            .fillMaxSize(0.9f)
                            .align(Alignment.Center),
                        factory = {
                            PieChart(it)
                        },
                        update = {
                            configurePie(it, payments, context)
                        }
                    )
                }
                if (!salaryReceived) {
                    var addSalaryOpened by remember {
                        mutableStateOf(false)
                    }
                    Box(
                        contentAlignment = Alignment.BottomCenter,
                        modifier = modifier
                            .fillMaxSize()
                            .padding(bottom = 16.dp)
                    ) {
                        Button(
                            onClick = {
                                addSalaryOpened = true
                            },
                        ) {
                            Text(text = "Add Salary")
                        }
                    }

                    if (addSalaryOpened) {
                        OnAddSalary(client, context) {
                            addSalaryOpened = it
                        }
                    }
                }
            }
        }
        Box(modifier = modifier, contentAlignment = Alignment.BottomEnd) {
            FloatingButton(
                modifier = Modifier
                    .padding(16.dp),
                navigator
            )
        }
    }
}

@Composable
fun MonthsList(
    modifier: Modifier = Modifier,
    client: FinancesService,
    navigator: DestinationsNavigator
) {
    var months by remember {
        mutableStateOf(getNext12Months())
    }
    LaunchedEffect(key1 = true) {
        months = client.getMonths()
    }
    LazyColumn(
        modifier = Modifier.padding(top = 40.dp)
    ) {
        itemsIndexed(months) { index, month ->
            val amount by animateIntAsState(targetValue = month.left)
            Row(
                modifier
                    .padding(20.dp)
                    .fillMaxWidth()
                    .clickable {
//                        navigator.navigate(MonthsScreenDestination(month, index == 0))
                    },
                horizontalArrangement = Arrangement.SpaceBetween,
            ) {
                Text(
                    text = month.name,
                    color = MaterialTheme.colorScheme.onBackground,
                    fontWeight = FontWeight.Bold,
                    fontSize = 28.sp
                )
                Text(
                    text = amount.toString(),
                    color = MaterialTheme.colorScheme.primary,
                    fontWeight = FontWeight.Bold,
                    fontSize = 28.sp
                )
            }
        }
    }

}

@Composable
fun FloatingButton(
    modifier: Modifier = Modifier,
    navigator: DestinationsNavigator
) {
    FloatingActionButton(
        onClick = {
//            navigator.navigate(
//                AddPaymentScreenDestination()
//            )
        },
        modifier = modifier
    ) {
        Icon(Icons.Filled.Add, "Add Button")
    }
}

private fun getNext12Months(): List<Month> {
    val formatter = DateTimeFormatter.ofPattern("MMMM yyyy")
    val currentDate = LocalDate.now()
    val months = mutableListOf<Month>()

    for (i in 0 until 12) {
        val futureDate = currentDate.plusMonths(i.toLong())
        val formattedDate = futureDate.format(formatter)
        months.add(Month(i, formattedDate, 0, 0))
    }

    return months
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun OnAddSalary(
    client: FinancesService,
    context: Context,
    onDismiss: (Boolean) -> Unit,
    ) {
        AlertDialog(
            onDismissRequest = {
                onDismiss(false)
            }
        ) {
            var waterBill by remember {
                mutableStateOf("0")
            }
            var powerBill by remember {
                mutableStateOf("0")
            }
            val scope = rememberCoroutineScope()
            Surface(
                modifier = Modifier
                    .wrapContentWidth()
                    .wrapContentHeight(),
                shape = MaterialTheme.shapes.large,
                tonalElevation = AlertDialogDefaults.TonalElevation
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = "Enter Water and Power bills.",
                    )

                    Spacer(modifier = Modifier.height(24.dp))
                    OutlinedTextField(
                        modifier = Modifier.fillMaxWidth(),
                        value = waterBill,
                        onValueChange = { waterBill = it },
                        label = { Text("Water Bill") },
                        maxLines = 1,
                    )
                    OutlinedTextField(
                        modifier = Modifier.fillMaxWidth(),
                        value = powerBill,
                        onValueChange = { powerBill = it },
                        label = { Text("Power Bill") },
                        maxLines = 1,
                    )

                    Row(modifier = Modifier.align(Alignment.End)) {
                        TextButton(
                            onClick = {
                                onDismiss(false)
                            },
                        ) {
                            Text("Cancel")
                        }
                        TextButton(
                            onClick = {
                                if (waterBill != "" && waterBill.toInt() >= 0 && powerBill != "" && powerBill.toInt() >= 0) {
                                    scope.launch {
                                        val response = client.addSalary(
                                            waterBill.toInt(),
                                            powerBill.toInt()
                                        )
                                        val toast = Toast(context)
                                        toast.setText(response)
                                        toast.show()

                                        onDismiss(false)
                                    }
                                }
                            },
                        ) {
                            Text("Add Salary")
                        }
                    }
                }
            }
        }
}

//private fun onAddSalary() {
//    val dialogLayout = layoutInflater.inflate(R.layout.add_salary, null)
//    val waterBillAmount = dialogLayout.findViewById<EditText>(R.id.etWaterBill)
//    val powerBillAmount = dialogLayout.findViewById<EditText>(R.id.etPowerBill)
//
//    val dialog = AlertDialog.Builder(requireContext())
//        .setTitle("Enter Bills:")
//        .setPositiveButton("OK") { _, _ ->
//            println("OK")
//        }
//        .setNegativeButton("Cancel") { _, _ ->
//            println("Cancelled")
//        }
//        .setView(dialogLayout)
//        .show()
//
//    dialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener {
//        if (waterBillAmount.text.toString() == "") {
//            waterBillAmount.error = "Amount is required!"
//        } else if (powerBillAmount.text.toString() == "") {
//            powerBillAmount.error = "Amount is required!"
//        } else {
//            launch {

//            }
//        }
//    }
//}

private fun configurePie(chart: PieChart, payments: List<Payment>, context: Context) {
    val expenseList = ExpenseType.values().toMutableList()
    val categories = resetCategories()
    for (payment: Payment in payments) {
        if (ExpenseType.values()[payment.expense_type] in expenseList && payment.amount > 0)
            categories[payment.expense_type] += payment.amount
    }
    initPie(
        chart,
        context,
        categories
    )
}
