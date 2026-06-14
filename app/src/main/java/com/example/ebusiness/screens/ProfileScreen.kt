package com.example.ebusiness.screens

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
    userType: String = "fan"   // "fan" oder "host"
) {
    val isHost = userType == "host"
    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()
    fun snack(msg: String) { scope.launch { snackbarHostState.showSnackbar(msg) } }

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) },
        containerColor = MaterialTheme.colorScheme.background
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(bottom = paddingValues.calculateBottomPadding())
        ) {
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
                    Surface(
                        shape = CircleShape,
                        color = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.size(38.dp)
                    ) {
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
                            Text(
                                if (isHost) "\$50.00" else "\$25.00",
                                color = Color.White, fontWeight = FontWeight.Bold, fontSize = 12.sp)
                        }
                    }
                    Spacer(Modifier.width(8.dp))
                    IconButton(
                        onClick = onNavigateToAlerts,
                        modifier = Modifier.size(32.dp)
                    ) {
                        Icon(Icons.Default.NotificationsNone, null,
                            tint = MaterialTheme.colorScheme.onSurface,
                            modifier = Modifier.size(22.dp))
                    }
                    Spacer(Modifier.width(8.dp))
                    IconButton(
                        onClick = { snack("Menu – coming soon") },
                        modifier = Modifier.size(32.dp)
                    ) {
                        Icon(Icons.Default.Menu, null,
                            tint = MaterialTheme.colorScheme.onSurface, modifier = Modifier.size(22.dp))
                    }
                }
            }

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(bottomStart = 28.dp, bottomEnd = 28.dp))
                    .background(
                        Brush.verticalGradient(
                            colors = listOf(
                                Color(0xFF4A8AFF),
                                Color(0xFF6B60F0)
                            )
                        )
                    )
                    .padding(top = 32.dp, bottom = 28.dp, start = 20.dp, end = 20.dp)
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier.fillMaxWidth()
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
                            Icon(
                                Icons.Default.Person,
                                contentDescription = null,
                                tint = Color.White,
                                modifier = Modifier.size(52.dp)
                            )
                        }
                    }
                    Spacer(Modifier.height(14.dp))
                    Text(
                        if (isHost) "Event Host Pro" else "Music Fan",
                        fontWeight = FontWeight.Bold,
                        fontSize = 24.sp,
                        color = Color.White
                    )
                    Spacer(Modifier.height(4.dp))
                    Text(
                        if (isHost) "test_host@stagepot.com" else "test_fan@stagepot.com",
                        style = MaterialTheme.typography.bodySmall,
                        color = Color.White.copy(alpha = 0.85f)
                    )
                    Spacer(Modifier.height(12.dp))
                    Surface(
                        shape = RoundedCornerShape(20.dp),
                        color = Color.White
                    ) {
                        Text(
                            "Member since 2024",
                            modifier = Modifier.padding(horizontal = 16.dp, vertical = 5.dp),
                            style = MaterialTheme.typography.labelMedium,
                            color = MaterialTheme.colorScheme.primary,
                            fontWeight = FontWeight.SemiBold
                        )
                    }
                }
            }

            Column(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth()
                    .verticalScroll(rememberScrollState())
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    if (isHost) {
                        StatCard(Modifier.weight(1f), Icons.Default.Event,       "8",     "Events",   Color(0xFF7C3AED), onClick = { snack("My Events – coming soon") })
                        StatCard(Modifier.weight(1f), Icons.Default.TrendingUp,  "\$45K", "Revenue",  Color(0xFF16A34A), onClick = { snack("Revenue Analytics – coming soon") })
                        StatCard(Modifier.weight(1f), Icons.Default.People,      "12",    "Attended", Color(0xFFFFB300), onClick = { snack("Attendees – coming soon") })
                    } else {
                        StatCard(Modifier.weight(1f), Icons.Default.ConfirmationNumber, "12", "Tickets",   Color(0xFF7C3AED), onClick = { snack("My Tickets – coming soon") })
                        StatCard(Modifier.weight(1f), Icons.Default.Favorite,           "5",  "Favorites", Color(0xFFEC4899), onClick = { snack("Favorites – coming soon") })
                        StatCard(Modifier.weight(1f), Icons.Default.LocationOn,         "3",  "Cities",    Color(0xFF16A34A), onClick = { snack("Cities visited – coming soon") })
                    }
                }

                Card(
                    shape = RoundedCornerShape(16.dp),
                    onClick = { snack("Credits – coming soon") }
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(
                                Brush.horizontalGradient(
                                    colors = listOf(Color(0xFFFFB300), Color(0xFFFF6D00))
                                )
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
                                    Icon(Icons.Default.Wallet, null, tint = Color.White, modifier = Modifier.size(18.dp))
                                    Text("Credits Balance", color = Color.White, fontWeight = FontWeight.SemiBold, fontSize = 15.sp)
                                }
                                Surface(
                                    shape = RoundedCornerShape(20.dp),
                                    color = Color.White.copy(alpha = 0.25f)
                                ) {
                                    Row(
                                        modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
                                        verticalAlignment = Alignment.CenterVertically,
                                        horizontalArrangement = Arrangement.spacedBy(4.dp)
                                    ) {
                                        Icon(Icons.Default.Add, null,
                                            tint = Color.White,
                                            modifier = Modifier.size(14.dp))
                                        Text("Add", color = Color.White,
                                            fontSize = 12.sp, fontWeight = FontWeight.SemiBold)
                                    }
                                }
                            }
                            Spacer(Modifier.height(8.dp))
                            Text(
                                if (isHost) "\$50.00" else "\$25.00",
                                fontSize = 32.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color.White
                            )
                            Text(
                                "Available for lottery entries",
                                style = MaterialTheme.typography.bodySmall,
                                color = Color.White.copy(alpha = 0.85f)
                            )
                            Spacer(Modifier.height(8.dp))
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(Icons.Default.Info, null,
                                    tint = Color.White.copy(alpha = 0.7f),
                                    modifier = Modifier.size(12.dp))
                                Spacer(Modifier.width(4.dp))
                                Text(
                                    "Earn credits from lottery cashback",
                                    style = MaterialTheme.typography.labelSmall,
                                    color = Color.White.copy(alpha = 0.7f)
                                )
                            }
                        }
                    }
                }

                ProfileSection(title = "Contact Information") {
                    ContactItem(icon = Icons.Default.Email, label = "Email",
                        value = if (isHost) "test_host@stagepot.com" else "test_fan@stagepot.com",
                        onClick = { snack("Email copied to clipboard") })
                    HorizontalDivider(modifier = Modifier.padding(horizontal = 16.dp))
                    ContactItem(icon = Icons.Default.Phone, label = "Phone",
                        value = if (isHost) "+1 (555) 987-6543" else "+1 (555) 234-5678",
                        onClick = { snack("Phone number copied to clipboard") })
                    HorizontalDivider(modifier = Modifier.padding(horizontal = 16.dp))
                    ContactItem(icon = Icons.Default.LocationOn, label = "Location",
                        value = if (isHost) "Los Angeles, CA" else "Berlin, Deutschland",
                        onClick = { snack("Opening maps – coming soon") })
                }

                ProfileSection(title = "Account Settings") {
                    ProfileItem(Icons.Default.Edit, "Edit Profile") { onEditProfile() }
                    HorizontalDivider(modifier = Modifier.padding(horizontal = 16.dp))
                    ProfileItemWithBadge(Icons.Default.CreditCard, "Payment Methods", badgeCount = 2) { onPaymentMethods() }
                    HorizontalDivider(modifier = Modifier.padding(horizontal = 16.dp))
                    ProfileItem(Icons.Default.Notifications, "Notifications") { onNavigateToAlerts() }
                    HorizontalDivider(modifier = Modifier.padding(horizontal = 16.dp))
                    ProfileItem(Icons.Default.BarChart, "Analytics & Reports") { snack("Analytics – kommt bald") }
                    HorizontalDivider(modifier = Modifier.padding(horizontal = 16.dp))
                    ProfileItemWithBadge(Icons.Default.Verified, "Verification Status", badgeCount = -1) { snack("Account ist verifiziert ✓") }
                }

                ProfileSection(title = "Support") {
                    ProfileItem(Icons.Default.CurrencyExchange, "Currency Settings") { onCurrencySettings() }
                    HorizontalDivider(modifier = Modifier.padding(horizontal = 16.dp))
                    ProfileItem(Icons.Default.HelpOutline, "Help Center") { snack("Hilfe: support@stagepot.com") }
                    HorizontalDivider(modifier = Modifier.padding(horizontal = 16.dp))
                    ProfileItem(Icons.Default.Settings, "App Settings") { snack("App-Einstellungen – kommt bald") }
                }

                OutlinedButton(
                    onClick = onLogout,
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.outlinedButtonColors(
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


@Composable
private fun StatCard(
    modifier: Modifier = Modifier,
    icon: ImageVector,
    value: String,
    label: String,
    iconColor: Color,
    onClick: () -> Unit = {}
) {
    Card(
        modifier = modifier,
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(2.dp),
        onClick = onClick
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 24.dp, horizontal = 8.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            Icon(icon, null, tint = iconColor, modifier = Modifier.size(24.dp))
            Text(value, fontWeight = FontWeight.Bold, fontSize = 20.sp)
            Text(label,
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant)
        }
    }
}

@Composable
private fun StatItem(icon: ImageVector, value: String, label: String) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Icon(icon, null,
            tint = MaterialTheme.colorScheme.primary,
            modifier = Modifier.size(22.dp))
        Spacer(Modifier.height(4.dp))
        Text(value, fontWeight = FontWeight.Bold, fontSize = 18.sp)
        Text(label, style = MaterialTheme.typography.labelSmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant)
    }
}

@Composable
private fun ProfileSection(title: String, content: @Composable ColumnScope.() -> Unit) {
    Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
        Text(
            title,
            style = MaterialTheme.typography.titleSmall,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(horizontal = 4.dp, vertical = 2.dp)
        )
        Card(
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
            elevation = CardDefaults.cardElevation(2.dp)
        ) {
            Column(modifier = Modifier.fillMaxWidth()) { content() }
        }
    }
}

@Composable
private fun ContactItem(icon: ImageVector, label: String, value: String, onClick: () -> Unit = {}) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .padding(horizontal = 16.dp, vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Icon(icon, null,
            tint = MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = Modifier.size(18.dp))
        Column(modifier = Modifier.weight(1f)) {
            Text(label, style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant)
            Text(value, style = MaterialTheme.typography.bodyMedium)
        }
        Icon(Icons.AutoMirrored.Filled.ArrowForward, null,
            tint = MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = Modifier.size(16.dp))
    }
}

@Composable
private fun ProfileItem(icon: ImageVector, label: String, onClick: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .padding(horizontal = 16.dp, vertical = 14.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(icon, null,
            tint = MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = Modifier.size(20.dp))
        Spacer(Modifier.width(14.dp))
        Text(label, modifier = Modifier.weight(1f),
            style = MaterialTheme.typography.bodyMedium)
        Icon(Icons.AutoMirrored.Filled.ArrowForward, null,
            tint = MaterialTheme.colorScheme.onSurfaceVariant,
                       modifier = Modifier.size(16.dp))
    }
}

@Composable
private fun ProfileItemWithBadge(
    icon: ImageVector,
    label: String,
    badgeCount: Int = 0,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .padding(horizontal = 16.dp, vertical = 14.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(icon, null,
            tint = MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = Modifier.size(20.dp))
        Spacer(Modifier.width(14.dp))
        Text(label, modifier = Modifier.weight(1f),
            style = MaterialTheme.typography.bodyMedium)
        if (badgeCount > 0) {
            Badge { Text("$badgeCount") }
            Spacer(Modifier.width(6.dp))
        } else if (badgeCount == -1) {
            Surface(
                shape = RoundedCornerShape(20.dp),
                color = Color(0xFF16A34A).copy(alpha = 0.1f)
            ) {
                Text("Verified",
                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp),
                    color = Color(0xFF16A34A),
                    fontSize = 11.sp,
                    fontWeight = FontWeight.SemiBold)
            }
            Spacer(Modifier.width(6.dp))
        }
        Icon(Icons.AutoMirrored.Filled.ArrowForward, null,
            tint = MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = Modifier.size(16.dp))
    }
}
