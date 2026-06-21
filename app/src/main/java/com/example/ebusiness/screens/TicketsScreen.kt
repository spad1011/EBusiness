package com.example.ebusiness.screens

// Ticket-Übersicht mit drei Tabs: Active, Past, Lottery.
// Filtert clientseitig nach ticket.status — Daten kommen als Flow aus dem ViewModel.

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ebusiness.data.Ticket
import com.example.ebusiness.data.currencySymbol
import com.example.ebusiness.data.formatPrice

/**
 * Ticket-Liste mit Tab-Navigation (Active / Past / Lottery).
 * initialTab wird vom ProfileScreen gesetzt wenn man von dort direkt navigiert.
 */
@Composable
fun TicketsScreen(
    paddingValues: PaddingValues,
    tickets: List<Ticket>,
    initialTab: Int = 0,
    onTicketClick: (Int) -> Unit,
    credits: Double = 0.0,
    currency: String = "EUR",
    isDarkMode: Boolean = false,
    onToggleDarkMode: () -> Unit = {},
    onNavigateToHome: () -> Unit = {},
    onNavigateToProfile: () -> Unit = {},
    onNavigateToImprint: () -> Unit = {},
    onNavigateToSecondaryMarket: () -> Unit = {},
    userType: String = "fan"
) {
    var selectedTab by remember { mutableStateOf(initialTab) }
    var menuExpanded by remember { mutableStateOf(false) }
    val tabs = listOf("Active", "Past", "Lottery")

    // Status-basierter Filter: Tab-Index entspricht direkt dem Status-String
    val filteredTickets = tickets.filter { ticket ->
        when (selectedTab) {
            0 -> ticket.status == "Active"
            1 -> ticket.status == "Past"
            2 -> ticket.status == "Lottery"
            else -> true
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(bottom = paddingValues.calculateBottomPadding())
    ) {
        StagePotBrandBar {
            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(20.dp))
                    .background(Brush.horizontalGradient(listOf(Color(0xFFFFB300), Color(0xFFFF6D00))))
                    .padding(horizontal = 10.dp, vertical = 5.dp)
            ) {
                Row(verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                    Icon(Icons.Default.Wallet, null,
                        tint = Color.White, modifier = Modifier.size(13.dp))
                    Text(formatPrice(credits, currency),
                        color = Color.White, fontWeight = FontWeight.Bold, fontSize = 12.sp)
                }
            }
            Spacer(Modifier.width(4.dp))
            Box {
                IconButton(onClick = { menuExpanded = true }) {
                    Icon(Icons.Default.Menu, null, tint = MaterialTheme.colorScheme.onSurface)
                }
                DropdownMenu(expanded = menuExpanded, onDismissRequest = { menuExpanded = false }) {
                    DropdownMenuItem(text = { Text("Home") },
                        leadingIcon = { Icon(Icons.Default.Home, null) },
                        onClick = { menuExpanded = false; onNavigateToHome() })
                    HorizontalDivider()
                    DropdownMenuItem(text = { Text("Profile") },
                        leadingIcon = { Icon(Icons.Default.Person, null) },
                        onClick = { menuExpanded = false; onNavigateToProfile() })
                    DropdownMenuItem(text = { Text("Imprint") },
                        leadingIcon = { Icon(Icons.Default.Info, null) },
                        onClick = { menuExpanded = false; onNavigateToImprint() })
                    HorizontalDivider()
                    DropdownMenuItem(
                        text = { Text(if (isDarkMode) "Light Mode" else "Dark Mode") },
                        leadingIcon = { Icon(if (isDarkMode) Icons.Default.LightMode else Icons.Default.DarkMode, null) },
                        onClick = { menuExpanded = false; onToggleDarkMode() })
                }
            }
        }

        StagePotBanner(
            title = "My Tickets",
            subtitle = "Manage and access your tickets"
        )

        // Tab row
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 10.dp),
            horizontalArrangement = Arrangement.spacedBy(0.dp)
        ) {
            tabs.forEachIndexed { index, tab ->
                val selected = selectedTab == index
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .clip(RoundedCornerShape(8.dp))
                        .clickable(
                            indication = null,
                            interactionSource = remember { MutableInteractionSource() }
                        ) { selectedTab = index }
                        .padding(vertical = 9.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(
                            tab,
                            fontSize = 14.sp,
                            fontWeight = if (selected) FontWeight.SemiBold else FontWeight.Normal,
                            color = if (selected) MaterialTheme.colorScheme.onSurface
                                    else MaterialTheme.colorScheme.onSurfaceVariant
                        )
                        Spacer(Modifier.height(4.dp))
                        Box(
                            Modifier
                                .width(if (selected) 24.dp else 0.dp)
                                .height(2.dp)
                                .background(Color(0xFF4A8AFF), RoundedCornerShape(1.dp))
                        )
                    }
                }
            }
        }
        HorizontalDivider()

        if (filteredTickets.isEmpty()) {
            Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Icon(Icons.Default.ConfirmationNumber, null,
                        modifier = Modifier.size(72.dp),
                        tint = MaterialTheme.colorScheme.outlineVariant)
                    Text("No tickets yet",
                        style = MaterialTheme.typography.titleMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant)
                    Text("Buy your first ticket under Events!",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant)
                }
            }
        } else {
            LazyColumn(
                modifier = Modifier.weight(1f).fillMaxWidth(),
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(14.dp)
            ) {
                items(filteredTickets, key = { it.id }) { ticket ->
                    TicketCard(ticket = ticket, currency = currency, onClick = { onTicketClick(ticket.id) })
                }
            }
        }
    }
}

/**
 * Ticket-Karte in der Liste: farbiger Gradient-Header, Sitzplatz-/Preis-Infos
 * und Buttons für QR-Code-Ansicht und Download.
 * Vergangene Tickets (status != "Active") bekommen einen grauen Gradient.
 */
@Composable
private fun TicketCard(ticket: Ticket, currency: String = "EUR", onClick: () -> Unit) {
    val isActive = ticket.status == "Active"
    val gradientColors = if (isActive)
        listOf(Color(0xFF4A8AFF), Color(0xFF6B60F0))
    else
        listOf(Color(0xFF9CA3AF), Color(0xFF6B7280))

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 3.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
    ) {
        Column {
            // Gradient header
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(110.dp)
                    .background(Brush.horizontalGradient(gradientColors))
                    .padding(16.dp)
            ) {
                Column(modifier = Modifier.align(Alignment.TopStart)) {
                    Text(ticket.eventTitle, color = Color.White,
                        fontWeight = FontWeight.Bold, fontSize = 17.sp,
                        lineHeight = 22.sp)
                    Spacer(Modifier.height(6.dp))
                    Row(horizontalArrangement = Arrangement.spacedBy(14.dp)) {
                        Row(verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                            Icon(Icons.Default.CalendarToday, null,
                                tint = Color.White.copy(alpha = 0.85f),
                                modifier = Modifier.size(12.dp))
                            Text(ticket.eventDate, color = Color.White.copy(alpha = 0.85f),
                                fontSize = 12.sp)
                        }
                        Row(verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                            Icon(Icons.Default.LocationOn, null,
                                tint = Color.White.copy(alpha = 0.85f),
                                modifier = Modifier.size(12.dp))
                            Text(ticket.eventLocation, color = Color.White.copy(alpha = 0.85f),
                                fontSize = 12.sp)
                        }
                    }
                }
                // White "Active" badge top-right
                Surface(
                    modifier = Modifier.align(Alignment.TopEnd),
                    shape = RoundedCornerShape(20.dp),
                    color = Color.White
                ) {
                    Text(ticket.status,
                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp),
                        color = Color(0xFF4A8AFF),
                        fontSize = 11.sp,
                        fontWeight = FontWeight.SemiBold)
                }
            }

            // Section / Seat / Price
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 14.dp),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                TicketInfoCell("Section", ticket.section)
                VerticalDivider(modifier = Modifier.height(36.dp))
                TicketInfoCell("Seat", ticket.seat)
                VerticalDivider(modifier = Modifier.height(36.dp))
                TicketInfoCell("Price", formatPrice(ticket.price, currency))
            }

            HorizontalDivider(modifier = Modifier.padding(horizontal = 14.dp))

            // QR button (full width)
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 14.dp, vertical = 10.dp)
                    .clip(RoundedCornerShape(10.dp))
                    .background(Color(0xFF111827))
                    .clickable(
                        indication = null,
                        interactionSource = remember { MutableInteractionSource() }
                    ) { onClick() }
                    .padding(vertical = 12.dp),
                contentAlignment = Alignment.Center
            ) {
                Row(verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    Icon(Icons.Default.QrCodeScanner, null,
                        tint = Color.White, modifier = Modifier.size(18.dp))
                    Text("Show QR Code", color = Color.White, fontSize = 14.sp,
                        fontWeight = FontWeight.SemiBold)
                }
            }

            // Download row
            var showDownloadDialog by remember { mutableStateOf(false) }

            OutlinedButton(
                onClick = { showDownloadDialog = true },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 14.dp, end = 14.dp, bottom = 12.dp),
                shape = RoundedCornerShape(10.dp),
                contentPadding = PaddingValues(horizontal = 8.dp, vertical = 10.dp)
            ) {
                Icon(Icons.Default.FileDownload, null, modifier = Modifier.size(16.dp))
                Spacer(Modifier.width(6.dp))
                Text("Download Ticket", fontSize = 13.sp)
            }

            if (showDownloadDialog) {
                AlertDialog(
                    onDismissRequest = { showDownloadDialog = false },
                    icon  = { Icon(Icons.Default.FileDownload, null) },
                    title = { Text("Download Ticket") },
                    text  = { Text("Download this ticket as a PDF to your device?") },
                    confirmButton = {
                        Button(onClick = { showDownloadDialog = false }) { Text("OK") }
                    },
                    dismissButton = {
                        TextButton(onClick = { showDownloadDialog = false }) { Text("Cancel") }
                    }
                )
            }
        }
    }
}

/** Einzelne Info-Zelle in der Ticket-Karte (Section / Seat / Price) */
@Composable
private fun TicketInfoCell(label: String, value: String) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(label,
            style = MaterialTheme.typography.labelSmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant)
        Spacer(Modifier.height(2.dp))
        Text(value, fontWeight = FontWeight.Bold, fontSize = 15.sp)
    }
}
