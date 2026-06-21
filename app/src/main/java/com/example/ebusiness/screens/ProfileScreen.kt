package com.example.ebusiness.screens

// Profil-Screen: Avatar, Kontaktdaten, Credits-Widget, Account-Settings und Navigation.
// Unterscheidet zwischen Fan- und Host-Ansicht (isHost = userType == "host").

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import kotlinx.coroutines.launch
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ebusiness.data.Event
import com.example.ebusiness.data.Ticket
import com.example.ebusiness.data.currencySymbol
import com.example.ebusiness.data.formatPrice
import com.example.ebusiness.data.convertFromEur

/**
 * Profil-Screen mit Avatar, Kontaktinformationen, Credits-Karte und Einstellungs-Links.
 * Hosts sehen Events/Tickets/Zahlungen als Stat-Karten.
 * Fans sehen Home/Active-Tickets/besuchte Standorte.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileScreen(
    paddingValues: PaddingValues,
    isDarkMode: Boolean,
    onToggleDarkMode: () -> Unit,
    onLogout: () -> Unit,
    onPaymentMethods: () -> Unit = {},
    onNavigateToAlerts: () -> Unit = {},
    onCurrencySettings: () -> Unit = {},
    onEditProfile: () -> Unit = {},
    onNavigateToHome: () -> Unit = {},
    onNavigateToTickets: () -> Unit = {},
    onNavigateToTicketsTab: (Int) -> Unit = {},
    onNavigateToOrganize: () -> Unit = {},
    onNavigateToImprint: () -> Unit = {},
    onNavigateToSecondaryMarket: () -> Unit = {},
    onAddCredits: () -> Unit = {},
    onHelpCenter: () -> Unit = {},
    onAppSettings: () -> Unit = {},
    tickets: List<Ticket> = emptyList(),
    avatarUrl: String = "",
    onAvatarChange: (String) -> Unit = {},
    userType: String = "fan",
    credits: Double = 0.0,
    currency: String = "EUR",
    displayName: String = "",
    userEmail: String = "",
    userPhone: String = "",
    userLocation: String = "",
    memberSince: String = "2026",
    paymentMethodCount: Int = 0,
    events: List<Event> = emptyList()
) {
    val sym    = currencySymbol(currency)
    val isHost = userType == "host"

    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()
    fun snack(msg: String) { scope.launch { snackbarHostState.showSnackbar(msg) } }

    var menuExpanded by remember { mutableStateOf(false) }

    Scaffold(
        snackbarHost    = { SnackbarHost(snackbarHostState) },
        containerColor  = MaterialTheme.colorScheme.background
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(bottom = paddingValues.calculateBottomPadding())
        ) {
            // ─── Brand bar ────────────────────────────────────────────────
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(MaterialTheme.colorScheme.surface)
                    .padding(horizontal = 16.dp, vertical = 10.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Surface(shape = CircleShape, color = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.size(38.dp)) {
                        Box(contentAlignment = Alignment.Center) {
                            Icon(Icons.Default.ConfirmationNumber, null,
                                tint = Color.White, modifier = Modifier.size(20.dp))
                        }
                    }
                    Spacer(Modifier.width(8.dp))
                    Column(modifier = Modifier.weight(1f)) {
                        Text("StagePot", fontWeight = FontWeight.Bold,
                            fontSize = 15.sp, color = MaterialTheme.colorScheme.onSurface)
                        Text("Fair Tickets for Real Fans",
                            style = MaterialTheme.typography.labelSmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant)
                    }
                    // Credits chip
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(20.dp))
                            .background(Brush.horizontalGradient(listOf(Color(0xFFFFB300), Color(0xFFFF6D00))))
                            .padding(horizontal = 10.dp, vertical = 5.dp)
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(4.dp)
                        ) {
                            Icon(Icons.Default.Wallet, null,
                                tint = Color.White, modifier = Modifier.size(13.dp))
                            Text(formatPrice(credits, currency),
                                color = Color.White, fontWeight = FontWeight.Bold, fontSize = 12.sp)
                        }
                    }
                    Spacer(Modifier.width(8.dp))
                    Box {
                        IconButton(onClick = { menuExpanded = true }, modifier = Modifier.size(32.dp)) {
                            Icon(Icons.Default.Menu, null,
                                tint = MaterialTheme.colorScheme.onSurface, modifier = Modifier.size(22.dp))
                        }
                        DropdownMenu(expanded = menuExpanded, onDismissRequest = { menuExpanded = false }) {
                            DropdownMenuItem(
                                text = { Text("Events") },
                                leadingIcon = { Icon(Icons.Default.Home, null) },
                                onClick = { menuExpanded = false; onNavigateToHome() }
                            )
                            DropdownMenuItem(
                                text = { Text("My Tickets") },
                                leadingIcon = { Icon(Icons.Default.ConfirmationNumber, null) },
                                onClick = { menuExpanded = false; onNavigateToTickets() }
                            )
                            DropdownMenuItem(
                                text = { Text("Secondary Market") },
                                leadingIcon = { Icon(Icons.Default.Storefront, null) },
                                onClick = { menuExpanded = false; onNavigateToSecondaryMarket() }
                            )
                            HorizontalDivider()
                            DropdownMenuItem(
                                text = { Text("Imprint") },
                                leadingIcon = { Icon(Icons.Default.Info, null) },
                                onClick = { menuExpanded = false; onNavigateToImprint() }
                            )
                            HorizontalDivider()
                            DropdownMenuItem(
                                text = { Text(if (isDarkMode) "Light Mode" else "Dark Mode") },
                                leadingIcon = {
                                    Icon(if (isDarkMode) Icons.Default.LightMode else Icons.Default.DarkMode, null)
                                },
                                onClick = { menuExpanded = false; onToggleDarkMode() }
                            )
                        }
                    }
                }
            }

            // ─── Avatar / name hero ───────────────────────────────────────
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(bottomStart = 28.dp, bottomEnd = 28.dp))
                    .background(Brush.verticalGradient(listOf(Color(0xFF4A8AFF), Color(0xFF6B60F0))))
                    .padding(top = 32.dp, bottom = 28.dp, start = 20.dp, end = 20.dp)
            ) {
                var showAvatarDialog by remember { mutableStateOf(false) }

                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Box(
                        modifier = Modifier.size(96.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Box(
                            modifier = Modifier
                                .size(96.dp)
                                .clip(CircleShape)
                                .background(Color.White),
                            contentAlignment = Alignment.Center
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(86.dp)
                                    .clip(CircleShape)
                                    .background(Color(0xFF8B7FCC)),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(Icons.Default.Person, null,
                                    tint = Color.White, modifier = Modifier.size(52.dp))
                            }
                        }
                        // Camera button overlay
                        Box(
                            modifier = Modifier
                                .size(28.dp)
                                .align(Alignment.BottomEnd)
                                .clip(CircleShape)
                                .background(Color(0xFF111827))
                                .clickable { showAvatarDialog = true },
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(Icons.Default.CameraAlt, null,
                                tint = Color.White, modifier = Modifier.size(15.dp))
                        }
                    }

                if (showAvatarDialog) {
                    AlertDialog(
                        onDismissRequest = { showAvatarDialog = false },
                        icon  = { Icon(Icons.Default.CameraAlt, null) },
                        title = { Text("Profile Photo") },
                        text  = {
                            Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                                Text("Choose how to add your photo:")
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                                ) {
                                    OutlinedButton(
                                        onClick = {
                                            showAvatarDialog = false
                                            onAvatarChange("camera")
                                        },
                                        modifier = Modifier.weight(1f),
                                        shape = RoundedCornerShape(10.dp)
                                    ) {
                                        Icon(Icons.Default.CameraAlt, null,
                                            modifier = Modifier.size(16.dp))
                                        Spacer(Modifier.width(4.dp))
                                        Text("Camera")
                                    }
                                    OutlinedButton(
                                        onClick = {
                                            showAvatarDialog = false
                                            onAvatarChange("gallery")
                                        },
                                        modifier = Modifier.weight(1f),
                                        shape = RoundedCornerShape(10.dp)
                                    ) {
                                        Icon(Icons.Default.Photo, null,
                                            modifier = Modifier.size(16.dp))
                                        Spacer(Modifier.width(4.dp))
                                        Text("Gallery")
                                    }
                                }
                            }
                        },
                        confirmButton = {},
                        dismissButton = {
                            TextButton(onClick = { showAvatarDialog = false }) { Text("Cancel") }
                        }
                    )
                }
                    Spacer(Modifier.height(14.dp))
                    Text(
                        displayName.ifBlank { if (isHost) "Event Host" else "Fan" },
                        fontWeight = FontWeight.Bold,
                        fontSize   = 24.sp,
                        color      = Color.White
                    )
                    Spacer(Modifier.height(4.dp))
                    if (userEmail.isNotBlank()) {
                        Text(userEmail,
                            style = MaterialTheme.typography.bodySmall,
                            color = Color.White.copy(alpha = 0.85f))
                        Spacer(Modifier.height(4.dp))
                    }
                    Surface(shape = RoundedCornerShape(20.dp), color = Color.White) {
                        Text(
                            "Member since $memberSince",
                            modifier = Modifier.padding(horizontal = 16.dp, vertical = 5.dp),
                            style      = MaterialTheme.typography.labelMedium,
                            color      = MaterialTheme.colorScheme.primary,
                            fontWeight = FontWeight.SemiBold
                        )
                    }
                }
            }

            // ─── Scrollable body ─────────────────────────────────────────
            Column(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth()
                    .verticalScroll(rememberScrollState())
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                // Stat cards
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                    if (isHost) {
                        ProfileStatCard(Modifier.weight(1f), Icons.Default.Event,
                            "${events.size}", "Events",  Color(0xFF7C3AED),
                            onClick = { onNavigateToOrganize() })
                        ProfileStatCard(Modifier.weight(1f), Icons.Default.TrendingUp,
                            "${tickets.size}", "Tickets", Color(0xFF16A34A),
                            onClick = { onNavigateToTicketsTab(0) })
                        ProfileStatCard(Modifier.weight(1f), Icons.Default.People,
                            "${paymentMethodCount}", "Payments", Color(0xFFFFB300),
                            onClick = { onPaymentMethods() })
                    } else {
                        val uniqueLocations = tickets.map { it.eventLocation }.toSet().toList()
                        var showLocationsDialog by remember { mutableStateOf(false) }

                        ProfileStatCard(Modifier.weight(1f), Icons.Default.Home,
                            "", "Home", Color(0xFF7C3AED),
                            onClick = { onNavigateToHome() })
                        ProfileStatCard(Modifier.weight(1f), Icons.Default.Favorite,
                            "${tickets.count { it.status == "Active" }}", "Active", Color(0xFFEC4899),
                            onClick = { onNavigateToTicketsTab(0) })
                        ProfileStatCard(Modifier.weight(1f), Icons.Default.LocationOn,
                            "${uniqueLocations.size}", "Locations", Color(0xFF16A34A),
                            onClick = { showLocationsDialog = true })

                        if (showLocationsDialog) {
                            AlertDialog(
                                onDismissRequest = { showLocationsDialog = false },
                                icon  = { Icon(Icons.Default.LocationOn, null,
                                    tint = Color(0xFF16A34A)) },
                                title = { Text("My Locations") },
                                text  = {
                                    Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                                        if (uniqueLocations.isEmpty()) {
                                            Text("No locations yet — buy a ticket to see locations here.",
                                                color = MaterialTheme.colorScheme.onSurfaceVariant)
                                        } else {
                                            uniqueLocations.forEach { location ->
                                                Row(
                                                    verticalAlignment = Alignment.CenterVertically,
                                                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                                                ) {
                                                    Icon(Icons.Default.LocationOn, null,
                                                        tint = Color(0xFF16A34A),
                                                        modifier = Modifier.size(16.dp))
                                                    Text(location,
                                                        style = MaterialTheme.typography.bodyMedium)
                                                }
                                            }
                                        }
                                    }
                                },
                                confirmButton = {
                                    Button(onClick = { showLocationsDialog = false }) { Text("Close") }
                                }
                            )
                        }
                    }
                }

                // Credits card
                Card(shape = RoundedCornerShape(16.dp), onClick = { onAddCredits() }) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(
                                Brush.horizontalGradient(listOf(Color(0xFFFFB300), Color(0xFFFF6D00)))
                            )
                            .padding(18.dp)
                    ) {
                        Column {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                                ) {
                                    Icon(Icons.Default.Wallet, null,
                                        tint = Color.White, modifier = Modifier.size(18.dp))
                                    Text("Credits Balance",
                                        color = Color.White, fontWeight = FontWeight.SemiBold, fontSize = 15.sp)
                                }
                                Surface(shape = RoundedCornerShape(20.dp),
                                    color = Color.White.copy(alpha = 0.25f)) {
                                    Row(
                                        modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
                                        verticalAlignment = Alignment.CenterVertically,
                                        horizontalArrangement = Arrangement.spacedBy(4.dp)
                                    ) {
                                        Icon(Icons.Default.Add, null,
                                            tint = Color.White, modifier = Modifier.size(14.dp))
                                        Text("Add", color = Color.White,
                                            fontSize = 12.sp, fontWeight = FontWeight.SemiBold)
                                    }
                                }
                            }
                            Spacer(Modifier.height(8.dp))
                            Text(formatPrice(credits, currency),
                                fontSize = 32.sp, fontWeight = FontWeight.Bold, color = Color.White)
                            Text("Available for lottery entries",
                                style = MaterialTheme.typography.bodySmall,
                                color = Color.White.copy(alpha = 0.85f))
                            Spacer(Modifier.height(8.dp))
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(Icons.Default.Info, null,
                                    tint = Color.White.copy(alpha = 0.7f), modifier = Modifier.size(12.dp))
                                Spacer(Modifier.width(4.dp))
                                Text("Earn credits from lottery cashback",
                                    style = MaterialTheme.typography.labelSmall,
                                    color = Color.White.copy(alpha = 0.7f))
                            }
                        }
                    }
                }

                // Contact information
                ProfileSection("Contact Information") {
                    ContactInfoItem(Icons.Default.Email, "Email",
                        userEmail.ifBlank { "—" }) { snack("Email copied") }
                    HorizontalDivider(modifier = Modifier.padding(horizontal = 16.dp))
                    ContactInfoItem(Icons.Default.Phone, "Phone",
                        userPhone.ifBlank { "—" }) { snack("Phone copied") }
                    HorizontalDivider(modifier = Modifier.padding(horizontal = 16.dp))
                    ContactInfoItem(Icons.Default.LocationOn, "Location",
                        userLocation.ifBlank { "—" }) { snack("Opening maps — coming soon") }
                }

                // Account settings
                ProfileSection("Account Settings") {
                    ProfItem(Icons.Default.Edit, "Edit Profile") { onEditProfile() }
                    HorizontalDivider(modifier = Modifier.padding(horizontal = 16.dp))
                    ProfItemBadge(Icons.Default.CreditCard, "Payment Methods",
                        badgeCount = paymentMethodCount) { onPaymentMethods() }
                    HorizontalDivider(modifier = Modifier.padding(horizontal = 16.dp))
                    ProfItem(Icons.Default.Notifications, "Notifications") { onNavigateToAlerts() }
                    HorizontalDivider(modifier = Modifier.padding(horizontal = 16.dp))
                    ProfItemBadge(Icons.Default.Verified, "Verification Status", badgeCount = -1) {
                        snack("Account is verified ✓")
                    }
                }

                // Support
                ProfileSection("Support") {
                    ProfItem(Icons.Default.CurrencyExchange, "Currency Settings") { onCurrencySettings() }
                    HorizontalDivider(modifier = Modifier.padding(horizontal = 16.dp))
                    ProfItem(Icons.Default.HelpOutline, "Help Center") { onHelpCenter() }
                    HorizontalDivider(modifier = Modifier.padding(horizontal = 16.dp))
                    ProfItem(Icons.Default.Settings, "App Settings") { onAppSettings() }
                }

                // Logout
                OutlinedButton(
                    onClick = onLogout,
                    modifier = Modifier.fillMaxWidth(),
                    shape    = RoundedCornerShape(12.dp),
                    colors   = ButtonDefaults.outlinedButtonColors(
                        contentColor = MaterialTheme.colorScheme.error
                    ),
                    border = androidx.compose.foundation.BorderStroke(
                        1.dp, MaterialTheme.colorScheme.error
                    )
                ) {
                    Icon(Icons.Default.Logout, null)
                    Spacer(Modifier.width(8.dp))
                    Text("Log Out", fontWeight = FontWeight.SemiBold)
                }

                Text(
                    "Version 1.0.0 (2026)",
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier.align(Alignment.CenterHorizontally)
                )
            }
        }
    }
}

// ─── Private helpers ─────────────────────────────────────────────────────────

/** Klickbare Stat-Karte oben im Profil: Icon, Zahl und Label */
@Composable
private fun ProfileStatCard(
    modifier: Modifier,
    icon: ImageVector,
    value: String,
    label: String,
    iconColor: Color,
    onClick: () -> Unit = {}
) {
    Card(
        modifier  = modifier,
        shape     = RoundedCornerShape(16.dp),
        colors    = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(2.dp),
        onClick   = onClick
    ) {
        Column(
            modifier = Modifier.fillMaxWidth().padding(vertical = 24.dp, horizontal = 8.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            Icon(icon, null, tint = iconColor, modifier = Modifier.size(24.dp))
            Text(value, fontWeight = FontWeight.Bold, fontSize = 20.sp)
            Text(label, style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant)
        }
    }
}

/** Gruppiert mehrere Profil-Einträge unter einem Abschnittstitel in einer Card */
@Composable
private fun ProfileSection(title: String, content: @Composable ColumnScope.() -> Unit) {
    Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
        Text(title,
            style      = MaterialTheme.typography.titleSmall,
            fontWeight = FontWeight.Bold,
            modifier   = Modifier.padding(horizontal = 4.dp, vertical = 2.dp))
        Card(
            shape     = RoundedCornerShape(16.dp),
            colors    = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
            elevation = CardDefaults.cardElevation(2.dp)
        ) {
            Column(modifier = Modifier.fillMaxWidth()) { content() }
        }
    }
}

/** Kontaktinfo-Zeile: Icon + Label/Wert + Pfeil — klickbar (z.B. zum Kopieren) */
@Composable
private fun ContactInfoItem(
    icon: ImageVector, label: String, value: String, onClick: () -> Unit = {}
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .padding(horizontal = 16.dp, vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Icon(icon, null,
            tint = MaterialTheme.colorScheme.onSurfaceVariant, modifier = Modifier.size(18.dp))
        Column(modifier = Modifier.weight(1f)) {
            Text(label, style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant)
            Text(value, style = MaterialTheme.typography.bodyMedium)
        }
        Icon(Icons.AutoMirrored.Filled.ArrowForward, null,
            tint = MaterialTheme.colorScheme.onSurfaceVariant, modifier = Modifier.size(16.dp))
    }
}

/** Einfache Profil-Menüzeile: Icon + Label + Pfeil */
@Composable
private fun ProfItem(icon: ImageVector, label: String, onClick: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .padding(horizontal = 16.dp, vertical = 14.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(icon, null,
            tint = MaterialTheme.colorScheme.onSurfaceVariant, modifier = Modifier.size(20.dp))
        Spacer(Modifier.width(14.dp))
        Text(label, modifier = Modifier.weight(1f), style = MaterialTheme.typography.bodyMedium)
        Icon(Icons.AutoMirrored.Filled.ArrowForward, null,
            tint = MaterialTheme.colorScheme.onSurfaceVariant, modifier = Modifier.size(16.dp))
    }
}

/**
 * Profil-Menüzeile mit optionalem Badge.
 * badgeCount > 0 → Zahl-Badge; badgeCount == -1 → "Verified"-Chip; sonst kein Badge.
 */
@Composable
private fun ProfItemBadge(
    icon: ImageVector, label: String, badgeCount: Int = 0, onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .padding(horizontal = 16.dp, vertical = 14.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(icon, null,
            tint = MaterialTheme.colorScheme.onSurfaceVariant, modifier = Modifier.size(20.dp))
        Spacer(Modifier.width(14.dp))
        Text(label, modifier = Modifier.weight(1f), style = MaterialTheme.typography.bodyMedium)
        if (badgeCount > 0) {
            Badge { Text("$" + badgeCount.toString()) }
            Spacer(Modifier.width(6.dp))
        } else if (badgeCount == -1) {
            Surface(shape = RoundedCornerShape(20.dp), color = Color(0xFF16A34A).copy(alpha = 0.1f)) {
                Text("Verified",
                    modifier   = Modifier.padding(horizontal = 8.dp, vertical = 3.dp),
                    color      = Color(0xFF16A34A),
                    fontSize   = 11.sp,
                    fontWeight = FontWeight.SemiBold)
            }
            Spacer(Modifier.width(6.dp))
        }
        Icon(Icons.AutoMirrored.Filled.ArrowForward, null,
            tint = MaterialTheme.colorScheme.onSurfaceVariant, modifier = Modifier.size(16.dp))
    }
}
