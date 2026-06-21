package com.example.ebusiness.screens

// Liste aller Events mit Lotterie-Option — gefiltert aus allEvents (hasLottery = true).
// Zeigt ob der User schon teilgenommen hat (grünes "Entered"-Badge).

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ebusiness.data.Event
import com.example.ebusiness.entities.LotteryEntryEntity
import com.example.ebusiness.data.currencySymbol
import com.example.ebusiness.data.formatPrice
import com.example.ebusiness.data.convertFromEur

/**
 * Übersicht aller Events mit Lotterie.
 * Filtert clientseitig alle Events mit hasLottery=true und zeigt an,
 * ob der eingeloggte User für das jeweilige Event schon eingetragen ist.
 */
@Composable
fun LotteryEventsScreen(
    events: List<Event>,
    lotteryEntries: List<LotteryEntryEntity>,
    onBack: () -> Unit,
    onEnterLottery: (Int) -> Unit,
    currency: String = "EUR"
) {
    val lotteryEvents = events.filter { it.hasLottery }
    val enteredEventIds = lotteryEntries.map { it.eventId }.toSet()

    Column(modifier = Modifier.fillMaxSize()) {
        // Blue gradient banner with back arrow and title inside
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(bottomStart = 28.dp, bottomEnd = 28.dp))
                .background(
                    Brush.verticalGradient(
                        listOf(Color(0xFF4A8AFF), Color(0xFF6B60F0))
                    )
                )
                .statusBarsPadding()
                .padding(start = 4.dp, end = 20.dp, top = 4.dp, bottom = 26.dp)
        ) {
            Column {
                IconButton(onClick = onBack) {
                    Icon(
                        Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = "Back",
                        tint = Color.White
                    )
                }
                Spacer(Modifier.height(4.dp))
                Text(
                    "Lottery Events",
                    color = Color.White,
                    fontSize = 22.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(start = 16.dp)
                )
                Spacer(Modifier.height(2.dp))
                Text(
                    "${lotteryEvents.size} events with fair access",
                    color = Color.White.copy(alpha = 0.8f),
                    fontSize = 13.sp,
                    modifier = Modifier.padding(start = 16.dp)
                )
            }
        }

        if (lotteryEvents.isEmpty()) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Icon(Icons.Default.Stars, null,
                        modifier = Modifier.size(72.dp),
                        tint = MaterialTheme.colorScheme.outlineVariant)
                    Text("No lottery events available",
                        style = MaterialTheme.typography.titleMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant)
                }
            }
        } else {
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(14.dp)
            ) {
                // Info banner
                item {
                    Surface(
                        shape = RoundedCornerShape(14.dp),
                        color = Color(0xFF4A8AFF).copy(alpha = 0.1f)
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(14.dp),
                            horizontalArrangement = Arrangement.spacedBy(12.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(Icons.Default.Info, null,
                                tint = Color(0xFF4A8AFF),
                                modifier = Modifier.size(20.dp))
                            Text(
                                "Enter the lottery for a chance to buy a ticket at the original price. Results are announced 48h before the event.",
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onSurface,
                                lineHeight = 18.sp
                            )
                        }
                    }
                }

                items(lotteryEvents, key = { it.id }) { event ->
                    val alreadyEntered = event.id in enteredEventIds
                    LotteryEventCard(
                        event = event,
                        alreadyEntered = alreadyEntered,
                        currency = currency,
                        onClick = { onEnterLottery(event.id) }
                    )
                }
            }
        }
    }
}

/** Berechnet das Ziehungsdatum: 48h (= 2 Tage) vor dem Event */
private fun drawDate(eventDateStr: String): String {
    return try {
        val eventDate = LocalDate.parse(
            eventDateStr,
            DateTimeFormatter.ofPattern("MMM d, yyyy")
        )
        val draw = eventDate.minusDays(2)
        draw.format(DateTimeFormatter.ofPattern("MMM d, yyyy"))
    } catch (e: Exception) {
        "–"
    }
}

/**
 * Einzelne Event-Karte in der Lotterie-Liste.
 * Zeigt Ziehungsdatum, Teilnahmegebühr und ob der User schon eingetragen ist.
 */
@Composable
private fun LotteryEventCard(
    event: Event,
    alreadyEntered: Boolean,
    currency: String = "EUR",
    onClick: () -> Unit
) {
    val baseColor = Color(event.imageColor)
    val drawDateStr = remember(event.date) { drawDate(event.date) }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() },
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(3.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
    ) {
        Column {
            // Coloured gradient header
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(100.dp)
                    .background(
                        Brush.horizontalGradient(
                            listOf(baseColor, baseColor.copy(alpha = 0.7f))
                        )
                    )
                    .padding(16.dp)
            ) {
                Column(modifier = Modifier.align(Alignment.TopStart)) {
                    Text(event.title, color = Color.White,
                        fontWeight = FontWeight.Bold, fontSize = 16.sp,
                        lineHeight = 20.sp)
                    Spacer(Modifier.height(6.dp))
                    Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                        Row(verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                            Icon(Icons.Default.CalendarToday, null,
                                tint = Color.White.copy(alpha = 0.85f),
                                modifier = Modifier.size(12.dp))
                            Text(event.date, color = Color.White.copy(alpha = 0.85f),
                                fontSize = 12.sp)
                        }
                        Row(verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                            Icon(Icons.Default.LocationOn, null,
                                tint = Color.White.copy(alpha = 0.85f),
                                modifier = Modifier.size(12.dp))
                            Text(event.location, color = Color.White.copy(alpha = 0.85f),
                                fontSize = 12.sp)
                        }
                    }
                }

                // Entered / Lottery badge
                Surface(
                    modifier = Modifier.align(Alignment.TopEnd),
                    shape = RoundedCornerShape(20.dp),
                    color = if (alreadyEntered) Color(0xFF16A34A) else Color.White
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        Icon(
                            if (alreadyEntered) Icons.Default.CheckCircle else Icons.Default.Stars,
                            null,
                            tint = if (alreadyEntered) Color.White else Color(0xFF4A8AFF),
                            modifier = Modifier.size(12.dp)
                        )
                        Text(
                            if (alreadyEntered) "Entered" else "Lottery",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = if (alreadyEntered) Color.White else Color(0xFF4A8AFF)
                        )
                    }
                }
            }

            // Category + tickets left + price row
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 12.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Surface(
                        shape = RoundedCornerShape(8.dp),
                        color = MaterialTheme.colorScheme.surfaceVariant
                    ) {
                        Text(
                            event.category,
                            modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp),
                            style = MaterialTheme.typography.labelSmall,
                            fontWeight = FontWeight.Medium
                        )
                    }
                    if (event.ticketsLeft <= 10) {
                        Surface(
                            shape = RoundedCornerShape(8.dp),
                            color = Color(0xFFB71C1C).copy(alpha = 0.1f)
                        ) {
                            Text(
                                "Only ${event.ticketsLeft} left",
                                modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp),
                                style = MaterialTheme.typography.labelSmall,
                                color = Color(0xFFB71C1C),
                                fontWeight = FontWeight.Medium
                            )
                        }
                    }
                }
                Text(
                    formatPrice(event.price, currency),
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp,
                    color = Color(0xFF4A8AFF)
                )
            }

            // Ziehungsdatum
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 6.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                Icon(
                    Icons.Default.Casino,
                    contentDescription = null,
                    tint = Color(0xFF7C3AED),
                    modifier = Modifier.size(14.dp)
                )
                Text(
                    "Draw: $drawDateStr  (48h before event)",
                    style = MaterialTheme.typography.labelSmall,
                    color = Color(0xFF7C3AED),
                    fontWeight = FontWeight.SemiBold
                )
            }

            HorizontalDivider(modifier = Modifier.padding(horizontal = 14.dp))

            // Entry fee + action button row
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 14.dp, vertical = 10.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    Icon(Icons.Default.Wallet, null,
                        tint = MaterialTheme.colorScheme.onSurfaceVariant,
                        modifier = Modifier.size(14.dp))
                    Text(
                        "Entry fee: ${formatPrice(1.09, currency)}",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }

                Button(
                    onClick = onClick,
                    enabled = !alreadyEntered,
                    shape = RoundedCornerShape(20.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = if (alreadyEntered) Color(0xFF6B7280) else Color(0xFF111827),
                        contentColor = Color.White
                    ),
                    contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp)
                ) {
                    Text(
                        if (alreadyEntered) "Entered" else "Enter Lottery",
                        fontSize = 13.sp,
                        fontWeight = FontWeight.SemiBold
                    )
                }
            }
        }
    }
}

