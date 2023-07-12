package mk.sekuloski.success.finances.presentation.payments

import androidx.compose.animation.core.animateIntAsState
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Check
import androidx.compose.material.icons.outlined.Close
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import mk.sekuloski.success.finances.domain.model.Payment

@Composable
fun PaymentItem(
    payment: Payment,
    modifier: Modifier = Modifier,
) {
    val amount by animateIntAsState(targetValue = payment.amount)
    Row(
        modifier
            .padding(12.dp)
            .fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Row(
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = modifier
                .fillMaxWidth()
                .weight(1f),
        ) {
            Text(
                text = payment.name,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                color = MaterialTheme.colorScheme.onTertiaryContainer,
                fontWeight = FontWeight.Bold,
                fontSize = 20.sp,
                modifier = modifier.weight(1f)
            )
            Text(
                text = amount.toString(),
                maxLines = 1,
                color = MaterialTheme.colorScheme.onTertiaryContainer,
                fontWeight = FontWeight.Bold,
                fontSize = 20.sp
            )
        }

        Icon(
            if (payment.paid) Icons.Outlined.Check else Icons.Outlined.Close,
            "Payment Status",
            tint = if (payment.paid) Color.Green else Color.Red,
            modifier = Modifier.padding(start = 10.dp)
        )
    }
}