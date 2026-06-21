package com.example.ebusiness.screens

// Veranstalter-Dashboard: nur für User mit userType="host" zugänglich.
// Zeigt Events, Analytics-Übersicht und Einstellungen in einem Tab-Layout.

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ebusiness.data.Event
import com.example.ebusiness.data.currencySymbol
import com.example.ebusiness.data.convertFromEur
import com.example.ebusiness.data.formatPrice
import com.example.ebusiness.entities.AdresseEntity
import com.example.ebusiness.entities.EventEntity

/**
 * Organisator-Screen — nur für Hosts.
 * Fans bekommen eine "Host Access Only"-Meldung und können nicht weiter navigieren.
 * Umsatz wird live aus der Event-Liste berechnet und im Banner angezeigt.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun OrganizeScreen(
    paddingValues: PaddingValues,
    userType: String = "fan",
    events: List<Event> = emptyList(),
    currency: String = "EUR",
    onCreateEvent: (EventEntity, AdresseEntity) -> Unit = { _, _ -> }
) {
    val sym = currencySymbol(currency)

    if (userType != "host") {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(bottom = paddingValues.calculateBottomPadding())
        ) {
            StagePotBrandBar()
            StagePotBanner(title = "Organize", subtitle = "For event organizers")
            Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(12.dp),
                    modifier = Modifier.padding(32.dp)
                ) {
                    Icon(Icons.Default.Lock, null,
                        modifier = Modifier.size(56.dp),
                        tint = MaterialTheme.colorScheme.outlineVariant)
                    Text("Host Access Only",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold)
                    Text("Login as a host to manage your events and access the organizer dashboard.",
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        textAlign = TextAlign.Center,
                        style = MaterialTheme.typography.bodySmall)
                }
            }
        }
        return
    }

    // Umsatz aus allen Event-Preisen summieren und in lesbares Format bringen (K / M)
    val totalRevenue = convertFromEur(events.sumOf { it.price }, currency)
    val revenueLabel = when {
        totalRevenue >= 1_000_000 -> "${sym}${"%.1f".format(totalRevenue / 1_000_000)}M"
        totalRevenue >= 1_000     -> "${sym}${"%.1f".format(totalRevenue / 1_000)}K"
        else                      -> "${sym}${"%.0f".format(totalRevenue)}"
    }

    var selectedTab     by remember { mutableStateOf(0) }
    var showCreateDialog by remember { mutableStateOf(false) }
    val tabs = listOf("Events", "Analytics", "Settings")

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(bottom = paddingValues.calculateBottomPadding())
    ) {
        StagePotBrandBar {
            IconButton(onClick = { showCreateDialog = true }) {
                Icon(Icons.Default.Add, null, tint = MaterialTheme.colorScheme.onSurface)
            }
        }

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(bottomStart = 28.dp, bottomEnd = 28.dp))
                .background(Brush.verticalGradient(listOf(Color(0xFF4A8AFF), Color(0xFF6B60F0))))
                .padding(start = 20.dp, end = 20.dp, top = 20.dp, bottom = 24.dp)
        ) {
            Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.Top
                ) {
                    Column {
                        Text("Organizer Dashboard",
                            color = Color.White, fontSize = 20.sp, fontWeight = FontWeight.Bold)
                        Text("Manage your events",
                            color = Color.White.copy(alpha = 0.8f), fontSize = 13.sp)
                    }
                    Surface(shape = RoundedCornerShape(20.dp), color = Color.White.copy(alpha = 0.2f)) {
                        Row(
                            modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(4.dp)
                        ) {
                            Icon(Icons.Default.Verified, null,
                                tint = Color.White, modifier = Modifier.size(13.dp))
                            Text("Verified",
                                color = Color.White, fontSize = 12.sp, fontWeight = FontWeight.SemiBold)
                        }
                    }
                }
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                    OrgBannerStat(Modifier.weight(1f), Icons.Default.AttachMoney, "Total Revenue", revenueLabel)
                    OrgBannerStat(Modifier.weight(1f), Icons.Default.ConfirmationNumber,
                        "Events", "${events.size}")
                }
            }
        }

        LazyColumn(
            modifier = Modifier.weight(1f).fillMaxWidth(),
            contentPadding = PaddingValues(horizontal = 16.dp, vertical = 14.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            item {
                Button(
                    onClick  = { showCreateDialog = true },
                    modifier = Modifier.fillMaxWidth(),
                    shape    = RoundedCornerShape(12.dp),
                    colors   = ButtonDefaults.buttonColors(containerColor = Color(0xFF111827)),
                    contentPadding = PaddingValues(vertical = 14.dp)
                ) {
                    Icon(Icons.Default.Add, null, modifier = Modifier.size(18.dp))
                    Spacer(Modifier.width(8.dp))
                    Text("Create New Event", fontWeight = FontWeight.SemiBold, fontSize = 15.sp)
                }
            }

            item {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(12.dp))
                        .background(MaterialTheme.colorScheme.surfaceVariant)
                        .padding(4.dp)
                ) {
                    tabs.forEachIndexed { index, tab ->
                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .clip(RoundedCornerShape(9.dp))
                                .background(
                                    if (selectedTab == index) MaterialTheme.colorScheme.surface
                                    else Color.Transparent
                                )
                                .clickable { selectedTab = index }
                                .padding(vertical = 9.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                tab,
                                fontSize   = 14.sp,
                                fontWeight = if (selectedTab == index) FontWeight.SemiBold else FontWeight.Normal,
                                color      = if (selectedTab == index)
                                    MaterialTheme.colorScheme.onSurface
                                else
                                    MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }
                }
            }

            when (selectedTab) {
                0 -> {
                    if (events.isEmpty()) {
                        item {
                            Box(
                                modifier = Modifier.fillMaxWidth().padding(vertical = 48.dp),
                                contentAlignment = Alignment.Center
                            ) {
                                Text("No events yet — create your first event!",
                                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                                    textAlign = TextAlign.Center)
                            }
                        }
                    } else {
                        items(events) { event ->
                            OrgEventCard(event = event, sym = sym, currency = currency)
                        }
                    }
                }
                1 -> item {
                    OrgAnalyticsPlaceholder(sym = sym, currency = currency, events = events)
                }
                2 -> item {
                    OrgSettingsPlaceholder()
                }
            }
        }
    }

    if (showCreateDialog) {
        AlertDialog(
            onDismissRequest = { showCreateDialog = false },
            icon    = { Icon(Icons.Default.Event, null) },
            title   = { Text("Create New Event") },
            text    = {
                Text("The full event creation form is coming soon. You will be able to set title, date, location, category, price and ticket capacity.")
            },
            confirmButton = {
                Button(onClick = { showCreateDialog = false }) { Text("OK") }
            }
        )
    }
}

/** Kleine Stat-Kachel im Organizer-Banner (Umsatz / Event-Anzahl) */
@Composable
private fun OrgBannerStat(modifier: Modifier, icon: ImageVector, label: String, value: String) {
    Surface(modifier = modifier, shape = RoundedCornerShape(12.dp), color = Color.White.copy(alpha = 0.15f)) {
        Column(modifier = Modifier.padding(12.dp), verticalArrangement = Arrangement.spacedBy(4.dp)) {
            Icon(icon, null, tint = Color.White, modifier = Modifier.size(18.dp))
            Text(value, color = Color.White, fontWeight = FontWeight.Bold, fontSize = 16.sp)
            Text(label, color = Color.White.copy(alpha = 0.8f), fontSize = 11.sp)
        }
    }
}

/** Event-Karte im Organizer-Tab: Titel, Datum, verbleibende Tickets und Status-Badge */
@Composable
private fun OrgEventCard(event: Event, sym: String, currency: String = "EUR") {
    Card(shape = RoundedCornerShape(14.dp), elevation = CardDefaults.cardElevation(2.dp)) {
        Row(
            modifier = Modifier.fillMaxWidth().padding(14.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(52.dp)
                    .clip(RoundedCornerShape(10.dp))
                    .background(Color(event.imageColor)),
                contentAlignment = Alignment.Center
            ) {
                Icon(Icons.Default.Event, null, tint = Color.White, modifier = Modifier.size(26.dp))
            }
            Column(modifier = Modifier.weight(1f)) {
                Text(event.title, fontWeight = FontWeight.Bold,
                    style = MaterialTheme.typography.bodyMedium)
                Text(event.date, style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant)
                Text("${event.ticketsLeft} tickets remaining  •  ${formatPrice(event.price, currency)}",
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant)
            }
            Surface(
                shape = RoundedCornerShape(20.dp),
                color = MaterialTheme.colorScheme.primaryContainer
            ) {
                Text(
                    if (event.ticketsLeft > 0) "Upcoming" else "Sold Out",
                    modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp),
                    style      = MaterialTheme.typography.labelSmall,
                    fontWeight = FontWeight.SemiBold,
                    color      = MaterialTheme.colorScheme.primary
                )
            }
        }
    }
}

/** Analytics-Placeholder: zeigt Basis-Kennzahlen aus der Event-Liste — kein externes Tracking */
@Composable
private fun OrgAnalyticsPlaceholder(sym: String, currency: String = "EUR", events: List<Event>) {
    Card(shape = RoundedCornerShape(14.dp), elevation = CardDefaults.cardElevation(2.dp)) {
        Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
            Text("Analytics Overview", fontWeight = FontWeight.Bold,
                style = MaterialTheme.typography.titleMedium)
            HorizontalDivider()
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                Text("Total Events", color = MaterialTheme.colorScheme.onSurfaceVariant)
                Text("${events.size}", fontWeight = FontWeight.SemiBold)
            }
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                Text("Avg. Ticket Price", color = MaterialTheme.colorScheme.onSurfaceVariant)
                Text(
                    if (events.isEmpty()) formatPrice(0.0, currency)
                    else formatPrice(events.map { it.price }.average(), currency),
                    fontWeight = FontWeight.SemiBold
                )
            }
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                Text("Lottery Events", color = MaterialTheme.colorScheme.onSurfaceVariant)
                Text("${events.count { it.hasLottery }}", fontWeight = FontWeight.SemiBold)
            }
            Text("Detailed analytics — coming soon",
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant)
        }
    }
}

@Composable
private fun OrgSettingsPlaceholder() {
    Card(shape = RoundedCornerShape(14.dp), elevation = CardDefaults.cardElevation(2.dp)) {
        Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
            Text("Organizer Settings", fontWeight = FontWeight.Bold,
                style = MaterialTheme.typography.titleMedium)
            HorizontalDivider()
            Text("Payout account, notification preferences, and venue settings — coming soon.",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant)
        }
    }
}
