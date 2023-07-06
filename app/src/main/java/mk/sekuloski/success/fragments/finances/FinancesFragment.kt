package mk.sekuloski.success.fragments.finances

import android.app.AlertDialog
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.Toast
import androidx.compose.animation.core.animateIntAsState
import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.colorResource
import androidx.fragment.app.Fragment
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import com.github.mikephil.charting.charts.PieChart
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import mk.sekuloski.success.data.remote.dto.finances.ExpenseType
import mk.sekuloski.success.data.remote.services.finances.FinancesService
import mk.sekuloski.success.data.remote.dto.finances.Location
import mk.sekuloski.success.data.remote.dto.finances.Month
import mk.sekuloski.success.data.remote.dto.finances.Payment
import mk.sekuloski.success.data.remote.dto.finances.Subscription
import mk.sekuloski.success.fragments.destinations.MonthsScreenDestination
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
                    Box(
                        contentAlignment = Alignment.BottomCenter,
                        modifier = modifier
                            .fillMaxSize()
                            .padding(bottom = 16.dp)
                    ) {
                        Button(
                            onClick = {
//                            onAddSalary()
                            },
                        ) {
                            Text(text = "Add Salary")
                        }
                    }
                }
            }
        }
        Box(modifier = modifier, contentAlignment = Alignment.BottomEnd) {
            FloatingButton(
                modifier = Modifier
                    .padding(16.dp),
                client
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
                        navigator.navigate(
                            MonthsScreenDestination(month, index == 0)
                        )
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
    client: FinancesService
) {
    var locations by remember {
        mutableStateOf(emptyList<Location>())
    }
    LaunchedEffect(key1 = true) {
        locations = client.getLocations()
    }
    FloatingActionButton(
        onClick = {
            // AddPayment
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
//                val response = client.addSalary(
//                    waterBillAmount.text.toString().toInt(),
//                    powerBillAmount.text.toString().toInt()
//                )
//                val toast = Toast(it.context)
//                toast.setText(response)
//                toast.show()
//                dialog.dismiss()
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
