package mk.sekuloski.success.fragments.finances

import android.content.Context
import androidx.compose.animation.core.animateIntAsState
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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import com.github.mikephil.charting.charts.PieChart
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import mk.sekuloski.success.finances.data.remote.FinancesService
import mk.sekuloski.success.finances.domain.model.Month
import mk.sekuloski.success.finances.domain.model.Payment
import mk.sekuloski.success.finances.domain.model.PaymentType
import mk.sekuloski.success.finances.domain.model.Subscription
import mk.sekuloski.success.ui.theme.AppTheme
import mk.sekuloski.success.utils.initPie
import mk.sekuloski.success.utils.resetCategories
import com.ramcosta.composedestinations.annotation.Destination
import mk.sekuloski.success.destinations.PaymentItemScreenDestination
import mk.sekuloski.success.finances.presentation.payments.PaymentItem

val titles =
    listOf("Normal Payments", "Six Month Payments", "Three Month Payments", "Loans")

@Destination
@Composable
fun MonthsScreen(
    navigator: DestinationsNavigator,
    month: Month,
    current: Boolean,
) {
    AppTheme {
        var allPayments by remember {
            mutableStateOf(
                listOf<List<Payment>>(
                    emptyList(),
                    emptyList(),
                    emptyList(),
                    emptyList(),
                    emptyList(),
                )
            )
        }
        val client = FinancesService.create()
        val categories = resetCategories()
        val context = LocalContext.current
        var subscriptions by remember {
            mutableStateOf(emptyList<Subscription>())
        }
        val selectedButtonIndex = remember { mutableStateOf(-1) }
        val modifier: Modifier = Modifier
        modifier.fillMaxSize()
        LaunchedEffect(key1 = true) {
            allPayments =
                sortPayments(
                    client.getMonthPayments(month.id, month.name.split(" ")[1].toInt()),
                    categories
                )
            subscriptions = sortSubscriptions(client.getSubscriptions(), categories, current)
        }
        Column {
            TopView(month, modifier.weight(2f))
            for (i in titles.indices) {
                Box(
                    modifier =
                    if (selectedButtonIndex.value == -1)
                        modifier.weight(1f)
                    else if (selectedButtonIndex.value != i)
                        modifier.weight(1f)
                    else
                        modifier.weight(3f)
                ) {

                    PaymentsRow(
                        titles[i],
                        allPayments[i],
                        sumPayments(allPayments[i]),
                        onClick = {
                            if (selectedButtonIndex.value == i) {
                                selectedButtonIndex.value = -1
                            } else {
                                selectedButtonIndex.value = i
                            }
                        },
                        isSelected = selectedButtonIndex.value == i,
                        navigator
                    )
                }
            }
            Box(
                modifier =
                if (selectedButtonIndex.value == -1)
                    modifier.weight(1f)
                else if (selectedButtonIndex.value != titles.size)
                    modifier.weight(1f)
                else
                    modifier.weight(3f)
            ) {

                SubscriptionsRow(
                    "Subscriptions",
                    subscriptions,
                    sumSubscriptions(subscriptions),
                    onClick = {
                        if (selectedButtonIndex.value == titles.size) {
                            selectedButtonIndex.value = -1
                        } else {
                            selectedButtonIndex.value = titles.size
                        }
                    },
                    isSelected = selectedButtonIndex.value == titles.size,
                )
            }
            if (selectedButtonIndex.value == -1) {
                Box(modifier = modifier.weight(5f)) {
                    if (allPayments.isNotEmpty()) {
                        AndroidView(
                            modifier = modifier
                                .fillMaxSize()
                                .align(Alignment.Center),
                            factory = {
                                PieChart(it)
                            },
                            update = {
                                configurePie(it, allPayments, subscriptions, current, context)
                            }
                        )
                    }
                }
            } else {
                Spacer(modifier = Modifier.weight(3f))
            }
        }
    }
}

@Composable
fun SubscriptionsRow(
    title: String,
    subscriptions: List<Subscription>,
    sum: Int,
    onClick: () -> Unit,
    isSelected: Boolean,
    modifier: Modifier = Modifier
) {
    Column {
        Row(
            modifier = modifier
                .padding(10.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = modifier
                    .weight(1f)
                    .padding(start = 16.dp)
            ) {
                Text(
                    text = title,
                    color = MaterialTheme.colorScheme.onBackground,
                    fontWeight = FontWeight.Bold,
                    fontSize = 28.sp
                )

                Text(
                    text = sum.toString(),
                    color = MaterialTheme.colorScheme.onBackground,
                    fontWeight = FontWeight.Bold,
                    fontSize = 28.sp
                )
            }
            Spacer(modifier = modifier.width(16.dp))
            Row {
                Button(
                    onClick = onClick,
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.tertiaryContainer,
                        contentColor = MaterialTheme.colorScheme.onTertiaryContainer
                    )
                ) {
                    Icon(
                        imageVector = Icons.Default.ArrowDropDown,
                        contentDescription = "Open Dropdown",
                        modifier = modifier.scale(1f, if (isSelected) -1f else 1f)
                    )

                }
            }
        }
        if (isSelected) {
            SubscriptionList(subscriptions)
        }
    }
}

@Composable
fun SubscriptionList(subscriptions: List<Subscription>, modifier: Modifier = Modifier) {
    Card(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.tertiaryContainer,
        ),
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 10.dp
        )
    ) {
        LazyColumn {
            itemsIndexed(subscriptions.sortedBy { it.name }) { _, subscription ->
                val amount by animateIntAsState(targetValue = subscription.amount)
                Row(
                    modifier
                        .padding(12.dp)
                        .fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween,
                ) {
                    Text(
                        text = subscription.name,
                        overflow = TextOverflow.Ellipsis,
                        color = MaterialTheme.colorScheme.onTertiaryContainer,
                        fontWeight = FontWeight.Bold,
                        fontSize = 20.sp
                    )
                    Text(
                        text = amount.toString(),
                        color = MaterialTheme.colorScheme.onTertiaryContainer,
                        fontWeight = FontWeight.Bold,
                        fontSize = 20.sp
                    )
                }
            }
        }
    }

}

@Composable
fun PaymentsRow(
    title: String,
    payments: List<Payment>,
    sum: Int,
    onClick: () -> Unit,
    isSelected: Boolean,
    navigator: DestinationsNavigator,
    modifier: Modifier = Modifier
) {
    Column {
        Row(
            modifier = modifier
                .padding(start = 10.dp, end = 10.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = modifier
                    .weight(1f)
                    .padding(start = 16.dp)
            ) {
                Text(
                    text = title,
                    color = MaterialTheme.colorScheme.onBackground,
                    fontWeight = FontWeight.Bold,
                    fontSize = 24.sp
                )

                Text(
                    text = sum.toString(),
                    color = MaterialTheme.colorScheme.onBackground,
                    fontWeight = FontWeight.Bold,
                    fontSize = 24.sp
                )
            }
            Spacer(modifier = modifier.width(16.dp))
            Row {
                Button(
                    onClick = onClick,
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.tertiaryContainer,
                        contentColor = MaterialTheme.colorScheme.onTertiaryContainer
                    )
                ) {
                    Icon(
                        imageVector = Icons.Default.ArrowDropDown,
                        contentDescription = "Open Dropdown",
                        modifier = modifier.scale(1f, if (isSelected) -1f else 1f)
                    )

                }
            }
        }
        if (isSelected) {
            PaymentList(payments = payments, navigator = navigator)
        }
    }
}

@Composable
fun PaymentList(
    payments: List<Payment>,
    navigator: DestinationsNavigator,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.tertiaryContainer,
        ),
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 10.dp
        )
    ) {
        LazyColumn {
            itemsIndexed(payments.sortedBy { it.name }) { _, payment ->
                PaymentItem(payment, modifier.clickable { navigator.navigate(
                    PaymentItemScreenDestination()
                ) })
            }
        }
    }
}

@Composable
fun TopView(
    month: Month,
    modifier: Modifier = Modifier
) {
    val amountLeft by animateIntAsState(targetValue = month.left)
    val expenses by animateIntAsState(targetValue = month.expenses)
    Column(
        modifier = modifier
            .fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = month.name,
            color = MaterialTheme.colorScheme.onBackground,
            fontWeight = FontWeight.Bold,
            fontSize = 28.sp,
            modifier = Modifier.padding(bottom = 20.dp, top = 20.dp)
        )
        Row(
            modifier = Modifier
                .fillMaxWidth(0.7f),
            horizontalArrangement = Arrangement.SpaceBetween,
        ) {
            Text(
                text = "Amount Left",
                color = MaterialTheme.colorScheme.onBackground,
                fontWeight = FontWeight.Bold,
                fontSize = 28.sp,
            )
            Text(
                text = amountLeft.toString(),
                color = MaterialTheme.colorScheme.onBackground,
                fontSize = 28.sp,
            )
        }
        Row(
            modifier = Modifier
                .fillMaxWidth(0.7f),
            horizontalArrangement = Arrangement.SpaceBetween,
        ) {
            Text(
                text = "Expenses",
                color = MaterialTheme.colorScheme.onBackground,
                fontWeight = FontWeight.Bold,
                fontSize = 28.sp
            )
            Text(
                text = expenses.toString(),
                color = MaterialTheme.colorScheme.onBackground,
                fontSize = 28.sp
            )
        }
    }
}

private fun sortPayments(
    payments: List<Payment>,
    categories: ArrayList<Int>
): List<List<Payment>> {
    val normal = ArrayList<Payment>()
    val threeMonth = ArrayList<Payment>()
    val sixMonth = ArrayList<Payment>()
    val loan = ArrayList<Payment>()
    for (payment: Payment in payments) {
        if (payment.amount > 0) categories[payment.expense_type] += payment.amount
        when (payment.payment_type) {
            PaymentType.SINGLE_PAYMENT.ordinal -> {
                normal.add(payment)
            }

            PaymentType.THREE_MONTHS.ordinal -> {
                threeMonth.add(payment)
            }

            PaymentType.SIX_MONTHS.ordinal -> {
                sixMonth.add(payment)
            }

            PaymentType.LOAN.ordinal -> {
                loan.add(payment)
            }
        }
    }

    return listOf(normal, sixMonth, threeMonth, loan)
}

private fun sumPayments(payments: List<Payment>): Int {
    var sum = 0
    for (payment: Payment in payments) {
        if (payment.amount > 0) sum += payment.amount
    }

    return sum
}

private fun sortSubscriptions(
    subscriptions: List<Subscription>,
    categories: ArrayList<Int>,
    current: Boolean
): List<Subscription> {
    val activeSubscriptions = ArrayList<Subscription>()
    var subscriptionSum = 0

    for (subscription: Subscription in subscriptions) {
        if (subscription.active) {
            activeSubscriptions.add(subscription)
            subscriptionSum += subscription.amount
            if (!current && subscription.hypothetical && subscription.amount > 0)
                categories[subscription.expense_type] += subscription.amount
        }
    }

    return activeSubscriptions
}

private fun sumSubscriptions(subscriptions: List<Subscription>): Int {
    var sum = 0
    for (subscription: Subscription in subscriptions) {
        if (subscription.amount > 0) sum += subscription.amount
    }

    return sum
}

private fun configurePie(
    chart: PieChart,
    payments: List<List<Payment>>,
    subscriptions: List<Subscription>,
    current: Boolean,
    context: Context
) {
    val categories = resetCategories()
    for (list: List<Payment> in payments) {
        for (payment: Payment in list) {
            if (payment.amount > 0)
                categories[payment.expense_type] += payment.amount
        }
    }

    if (!current) {
        for (subscription: Subscription in subscriptions) {
            if (subscription.amount > 0)
                categories[subscription.expense_type] += subscription.amount
        }
    }
    initPie(
        chart,
        context,
        categories
    )
}