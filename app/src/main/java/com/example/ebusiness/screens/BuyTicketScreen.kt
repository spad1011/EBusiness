package com.example.ebusiness.screens

// Kaufabschluss-Screen: Bereich wählen, Zahlungsmethode auswählen, Ticket kaufen.
// Zahlungsmethoden kommen aus der DB (PaymentMethodEntity) — kein Hardcoding.

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ebusiness.data.Event
import com.example.ebusiness.data.currencySymbol
import com.example.ebusiness.data.convertFromEur
import com.example.ebusiness.data.formatPrice
import com.example.ebusiness.entities.PaymentMethodEntity

/**
 * Ticket-Kaufscreen für ein bestimmtes Event.
 * Baut die Zahlungsoptionen aus der DB-Liste auf — Credits werden immer als
 * letzte Option angehängt. Gibt Sitz und Bereich an onConfirm zurück.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BuyTicketScreen(
    event: Event?,
    paymentMethods: List<PaymentMethodEntity> = emptyList(),
    credits: Double = 0.0,
    currency: String = "EUR",
    onBack: () -> Unit,
    onConfirm: (seat: String, section: String) -> Unit
) {
    val sym = currencySymbol(currency)

    val sections = listOf("Floor", "Section A", "Section B", "VIP", "General Admission")
    var selectedSection by remember { mutableStateOf(sections.first()) }
    var selectedSeat    by remember { mutableStateOf("${(1..50).random()}") }
    var selectedPayIdx  by remember { mutableStateOf(0) }

    val paymentOptions = buildList {
        paymentMethods.forEach { add("${it.emoji} ${it.name}") }
        add("🪙 Credits (${formatPrice(credits, currency)} available)")
    }.ifEmpty { listOf("💳 Credit Card", "🪙 Credits (${formatPrice(credits, currency)} available)") }

    Column(modifier = Modifier.fillMaxSize()) {
        TopAppBar(
            title = { Text("Buy Ticket") },
            navigationIcon = {
                IconButton(onClick = onBack) {
                    Icon(Icons.AutoMirrored.Filled.ArrowBack, "Back")
                }
            },
            colors = TopAppBarDefaults.topAppBarColors(
                containerColor = MaterialTheme.colorScheme.primary,
                titleContentColor = MaterialTheme.colorScheme.onPrimary,
                navigationIconContentColor = MaterialTheme.colorScheme.onPrimary
            )
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Order summary card
            Card(
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer),
                shape = RoundedCornerShape(16.dp)
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Text("Order Summary", style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold)
                    HorizontalDivider()
                    SummaryRow("Event",    event?.title    ?: "—")
                    SummaryRow("Date",     event?.date     ?: "—")
                    SummaryRow("Location", event?.location ?: "—")
                    SummaryRow("Category", event?.category ?: "—")
                    HorizontalDivider()
                    Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                        Text("Total", fontWeight = FontWeight.Bold)
                        Text(formatPrice(event?.price ?: 0.0, currency),
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.primary,
                            style = MaterialTheme.typography.titleMedium)
                    }
                }
            }

            // Section selector
            Text("Section", style = MaterialTheme.typography.titleSmall, fontWeight = FontWeight.SemiBold)
            Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                sections.forEach { sec ->
                    val sel = selectedSection == sec
                    FilterChip(
                        selected = sel,
                        onClick  = { selectedSection = sec },
                        label    = { Text(sec, fontSize = 12.sp) }
                    )
                }
            }

            // Payment method
            Text("Payment Method", style = MaterialTheme.typography.titleSmall, fontWeight = FontWeight.SemiBold)
            paymentOptions.forEachIndexed { idx, option ->
                val sel = selectedPayIdx == idx
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape    = RoundedCornerShape(12.dp),
                    border   = if (sel) BorderStroke(2.dp, MaterialTheme.colorScheme.primary)
                               else BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant),
                    colors   = CardDefaults.cardColors(
                        containerColor = if (sel) MaterialTheme.colorScheme.primaryContainer
                                         else MaterialTheme.colorScheme.surface
                    ),
                    onClick  = { selectedPayIdx = idx }
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 16.dp, vertical = 12.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        RadioButton(selected = sel, onClick = { selectedPayIdx = idx })
                        Text(option, style = MaterialTheme.typography.bodyLarge)
                    }
                }
            }

            Spacer(Modifier.height(8.dp))

            // Buy button
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(12.dp))
                    .background(Color(0xFF111827))
                    .clickable(
                        indication = null,
                        interactionSource = remember { MutableInteractionSource() }
                    ) { onConfirm(selectedSeat, selectedSection) }
                    .padding(vertical = 16.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    "Buy Now — ${formatPrice(event?.price ?: 0.0, currency)}",
                    color = Color.White,
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 16.sp
                )
            }
        }
    }
}

/** Einzelne Zeile in der Bestellzusammenfassung: Label links, Wert rechts */
@Composable
private fun SummaryRow(label: String, value: String) {
    Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
        Text(label, color = MaterialTheme.colorScheme.onSurfaceVariant)
        Text(value, fontWeight = FontWeight.Medium)
    }
}
