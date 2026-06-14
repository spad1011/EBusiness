package com.example.ebusiness.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.ebusiness.data.MockData
import com.example.ebusiness.data.Ticket

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BuyTicketScreen(
    eventId: Int,
    onBack: () -> Unit,
    onConfirm: (Ticket) -> Unit
) {
    val event = MockData.events.find { it.id == eventId } ?: return

    var selectedPayment by remember { mutableStateOf("Kreditkarte") }
    val paymentOptions = listOf("Kreditkarte", "PayPal", "Credits (25.00€)")

    Column(modifier = Modifier.fillMaxSize()) {

        TopAppBar(
            title = { Text("Ticket kaufen") },
            navigationIcon = {
                IconButton(onClick = onBack) {
                    Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Zurück")
                }
            }
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {

            Card(
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer
                ),
                shape = RoundedCornerShape(16.dp)
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Text(
                        "Bestellübersicht",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )
                    HorizontalDivider()

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text("Event", color = MaterialTheme.colorScheme.onSurfaceVariant)
                        Text(event.title, fontWeight = FontWeight.Medium)
                    }
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text("Datum", color = MaterialTheme.colorScheme.onSurfaceVariant)
                        Text(event.date)
                    }
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text("Ort", color = MaterialTheme.colorScheme.onSurfaceVariant)
                        Text(event.location)
                    }
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text("Kategorie", color = MaterialTheme.colorScheme.onSurfaceVariant)
                        Text(event.category)
                    }

                    HorizontalDivider()

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text("Gesamtpreis", fontWeight = FontWeight.Bold)
                        Text(
                            "${event.price}€",
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.primary,
                            style = MaterialTheme.typography.titleMedium
                        )
                    }
                }
            }

            Text(
                "Zahlungsmethode",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )

            paymentOptions.forEach { option ->
                val isSelected = selectedPayment == option
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp),
                    border = if (isSelected)
                        BorderStroke(2.dp, MaterialTheme.colorScheme.primary)
                    else
                        BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant),
                    colors = CardDefaults.cardColors(
                        containerColor = if (isSelected)
                            MaterialTheme.colorScheme.primaryContainer
                        else
                            MaterialTheme.colorScheme.surface
                    ),
                    onClick = { selectedPayment = option }
                ) {
                    Row(
                        modifier = Modifier
                            .padding(horizontal = 16.dp, vertical = 12.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        RadioButton(
                            selected = isSelected,
                            onClick = { selectedPayment = option }
                        )
                        Text(option, style = MaterialTheme.typography.bodyLarge)
                    }
                }
            }

            Spacer(Modifier.weight(1f))

            Button(
                onClick = {
                    val newTicket = Ticket(
                        id = System.currentTimeMillis().toInt(),
                        eventId = event.id,
                        eventTitle = event.title,
                        eventDate = event.date,
                        eventLocation = event.location,
                        seat = "Block B, Reihe ${(1..15).random()}, Platz ${(1..25).random()}",
                        qrCode = "SP-${event.id}-${(10000..99999).random()}"
                    )
                    onConfirm(newTicket)
                },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp)
            ) {
                Text(
                    "Jetzt kaufen — ${event.price}€",
                    modifier = Modifier.padding(vertical = 4.dp),
                    style = MaterialTheme.typography.titleSmall
                )
            }
        }
    }
}
