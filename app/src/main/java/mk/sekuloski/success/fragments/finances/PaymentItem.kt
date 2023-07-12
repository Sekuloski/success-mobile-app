package mk.sekuloski.success.fragments.finances

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
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Check
import androidx.compose.material.icons.outlined.Close
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.AlertDialogDefaults
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import kotlinx.coroutines.launch
import kotlinx.serialization.json.JsonArray
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.JsonPrimitive
import mk.sekuloski.success.finances.domain.model.Payment
import mk.sekuloski.success.finances.data.remote.FinancesService
import mk.sekuloski.success.ui.theme.AppTheme
import java.util.Locale
import com.ramcosta.composedestinations.annotation.Destination


@Destination
@Composable
fun PaymentItemScreen(
    payment: Payment,
    navigator: DestinationsNavigator
) {
    val client = FinancesService.create()
    val context = LocalContext.current
    var payments by remember {
        mutableStateOf(listOf<Payment>())
    }
    val scope = rememberCoroutineScope()
    AppTheme {
        Box(modifier = Modifier.fillMaxSize())
        {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .align(Alignment.TopCenter)
                    .padding(16.dp)
            ) {
                TextRow("Name", payment.name)
                Spacer(modifier = Modifier.height(16.dp))
                TextRow("Amount", payment.amount.toString())
                Spacer(modifier = Modifier.height(16.dp))
                TextRow("Date", payment.date.toString().split("T")[0])
                Spacer(modifier = Modifier.height(16.dp))

                TextRow("Paid",
                    payment.paid.toString()
                        .replaceFirstChar { if (it.isLowerCase()) it.titlecase(Locale.ROOT) else it.toString() })

                Spacer(modifier = Modifier.height(16.dp))
                TextRow("Monthly", payment.monthly.toString()
                    .replaceFirstChar { if (it.isLowerCase()) it.titlecase(Locale.ROOT) else it.toString() })

                Spacer(modifier = Modifier.height(16.dp))
                TextRow("Category", payment.category.toString()) // convert to Category obj

                if (payment.monthly) {
                    LaunchedEffect(key1 = true) {
                        try {
                            val id = payment.id
                            val parts = payment.name.split(" ").last().split("/")
                            val startId = id + 1 - parts[0].toInt()
                            val array = ArrayList<JsonElement>()
                            for (i in 0 until parts[1].toInt()) array.add(JsonPrimitive(startId + i))
                            val paymentIds = JsonArray(array)
                            payments = client.getPayments(paymentIds)
                        } catch (e: java.lang.NumberFormatException) {
                            payments = listOf(payment)
                            payment.monthly = false
                            val toast = Toast(context)
                            toast.setText("Not a valid monthly payment!")
                            toast.show()
                        }
                    }
                    MonthlyPayments(payments)
                }
            }
            Row(
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .fillMaxWidth()
            ) {
                var openDeleteDialog by remember { mutableStateOf(false) }
                var openPayDialog by remember { mutableStateOf(false) }
                OutlinedButton(
                    onClick = {
                        openDeleteDialog = true
                    },
                    modifier = Modifier
                        .weight(1f)
                        .padding(16.dp)
                ) {
                    Text(text = "Delete", fontSize = 22.sp)
                }
                Button(
                    onClick = {
                        openPayDialog = true
                    },
                    modifier = Modifier
                        .weight(1f)
                        .padding(16.dp)
                ) {
                    Text(text = "Pay", fontSize = 22.sp)
                }

                if (openPayDialog) {
                    PayDialog(payments, payment) {
                        openPayDialog = it
                    }
                }

                if (openDeleteDialog) {
                    AlertDialog(
                        onDismissRequest = {
                            openDeleteDialog = false
                        },
                        title = {
                            Text(text = "Deleting Payments")
                        },
                        text = {
                            Column {
                                Text(
                                    "Are you sure you want to delete the following payments?",
                                    fontSize = 24.sp
                                )
                                if (payments.isEmpty()) {
                                    MonthlyPayments(listOf(payment))
                                } else {
                                    MonthlyPayments(payments)
                                }
                            }
                        },
                        confirmButton = {
                            Button(
                                onClick = {
                                    scope.launch {
                                        val toast = Toast(context)
                                        if (payment.monthly) {

                                            toast.setText(
                                                client.deleteMonthlyPayment(
                                                    payment.name.split(" ").dropLast(1)
                                                        .joinToString(" ")
                                                )
                                            )
                                        } else {
                                            toast.setText(
                                                client.deletePayment(
                                                    payment.id,
                                                    false
                                                )
                                            )
                                        }
                                        toast.show()
                                        navigator.popBackStack()
                                        openDeleteDialog = false
                                    }
                                }) {
                                Text("Delete")
                            }
                        },
                        dismissButton = {
                            Button(
                                onClick = {
                                    openDeleteDialog = false
                                }) {
                                Text("Cancel")
                            }
                        }
                    )
                }
            }
        }
    }
}

@Composable
private fun MonthlyPayments(
    payments: List<Payment>
) {
    LazyColumn {
        itemsIndexed(payments.sortedBy { it.name }) { _, payment ->
            val amount by animateIntAsState(targetValue = payment.amount)
            Row(
                Modifier
                    .padding(top = 12.dp)
                    .fillMaxWidth()
                    .clickable {
//                            (context as MainActivity).supportFragmentManager
//                                .beginTransaction()
//                                .apply {
//                                    replace(R.id.flFragment, PaymentFragment(payment))
//                                    addToBackStack(null)
//                                    commit()
//                                }
                    },
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(
                    horizontalArrangement = Arrangement.SpaceBetween,
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f)
                        .padding(bottom = 12.dp),
                ) {
                    Text(
                        text = payment.name,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        color = MaterialTheme.colorScheme.onBackground,
                        fontWeight = FontWeight.Bold,
                        fontSize = 20.sp,
                        modifier = Modifier.weight(1f)
                    )
                    Text(
                        text = amount.toString(),
                        maxLines = 1,
                        color = MaterialTheme.colorScheme.onBackground,
                        fontWeight = FontWeight.Bold,
                        fontSize = 20.sp
                    )
                }

                Icon(
                    if (payment.paid) Icons.Outlined.Check else Icons.Outlined.Close,
                    "Payment Status",
                    tint = if (payment.paid) Color.Green else Color.Red,
                    modifier = Modifier
                        .padding(start = 10.dp)
                        .offset(y = (-5f).dp)
                )
            }
        }
    }
}

@Composable
private fun TextRow(
    name: String,
    value: String
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = name,
            color = MaterialTheme.colorScheme.onBackground,
            fontSize = 28.sp
        )
        Text(
            text = value,
            color = MaterialTheme.colorScheme.onBackground,
            fontSize = 28.sp
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PayDialog(
    payments: List<Payment>,
    payment: Payment,
    onDismiss: (Boolean) -> Unit,
) {
    AlertDialog(
        onDismissRequest = {
            onDismiss(false)
        }
    ) {
        Surface(
            modifier = Modifier
                .wrapContentWidth()
                .wrapContentHeight(),
            shape = MaterialTheme.shapes.large,
            tonalElevation = AlertDialogDefaults.TonalElevation
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                if (payment.monthly) Text(
                    text = "Pay one or all Payments?",
                )
                else Text(
                    text = "Pay?",
                )

                Spacer(modifier = Modifier.height(24.dp))
                MonthlyPayments(payments)
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
                            onDismiss(false)
                        },
                    ) {
                        Text("Pay All")
                    }
                    TextButton(
                        onClick = {
                            onDismiss(false)
                        },
                    ) {
                        Text("Pay One")
                    }
                }
            }
        }
    }
//        if (payment.monthly) {
//            builder.setNeutralButton("Pay One") { _, _ ->
//                launch {
//                    val toast = Toast(context)
//                    toast.setText(client.payPayments(listOf(payment.id)))
//                    toast.show()
//                    parentFragmentManager.popBackStack()
//                }
//            }
//            builder.setPositiveButton("Pay All") { _, _ ->
//                launch {
//                    val toast = Toast(context)
//                    toast.setText(client.payPayments(payments.map { individual_payment -> individual_payment.id }))
//                    toast.show()
//                    parentFragmentManager.popBackStack()
//                }
//            }
//        } else {
//            builder.setPositiveButton("Yes") { _, _ ->
//                launch {
//                    val toast = Toast(context)
//                    toast.setText(client.payPayments(listOf(payment.id)))
//                    toast.show()
//                    parentFragmentManager.popBackStack()
//                }
//            }
//        }
//        builder.show()
}