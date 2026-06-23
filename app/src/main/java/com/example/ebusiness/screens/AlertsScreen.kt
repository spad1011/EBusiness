package com.example.ebusiness.screens

// Zeigt alle Benachrichtigungen des eingeloggten Users — Lotterie-Ergebnisse,
// Erinnerungen, Angebote und Credits-Meldungen.

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
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ebusiness.data.currencySymbol
import com.example.ebusiness.data.formatPrice
import com.example.ebusiness.entities.NotificationEntity
import java.time.LocalDate
import java.time.temporal.ChronoUnit

/** Passendes Icon je nach Notification-Typ */
private fun iconForType(type: String): ImageVector = when (type) {
    "lottery_win"     -> Icons.Default.CheckCircle
    "lottery_lose"    -> Icons.Default.Cancel
    "lottery_pending" -> Icons.Default.HourglassEmpty
    "reminder"        -> Icons.Default.AccessTime
    "offer"           -> Icons.Default.LocalOffer
    "credits"         -> Icons.Default.Stars
    else              -> Icons.Default.Notifications
}

/** Akzentfarbe für das Icon — jeder Typ hat seine eigene Farbe */
private fun colorForType(type: String): Color = when (type) {
    "lottery_win"     -> Color(0xFF16A34A)
    "lottery_lose"    -> Color(0xFFEA580C)
    "lottery_pending" -> Color(0xFF7C3AED)
    "reminder"        -> Color(0xFF2563EB)
    "offer"           -> Color(0xFFFFB300)
    "credits"         -> Color(0xFF7C3AED)
    else              -> Color(0xFF6B7280)
}

/** Hintergrundfarbe des Action-Buttons — nur lottery_lose hat Orange */
private fun actionColorForType(type: String): Color = when (type) {
    "lottery_lose" -> Color(0xFFEA580C)
    else           -> Color(0xFF111827)
}

/** Icon für den Action-Button — null wenn keine Aktion vorhanden */
private fun actionIconForType(type: String): ImageVector? = when (type) {
    "lottery_win"  -> Icons.Default.ConfirmationNumber
    "lottery_lose" -> Icons.Default.CardGiftcard
    else           -> null
}

/**
 * Wandelt ein ISO-Datum (createdAt) in einen lesbaren Zeitstempel um.
 * Beispiel: "2026-06-19" → "2d ago"
 */
private fun timeLabel(createdAt: String): String = try {
    val days = ChronoUnit.DAYS.between(LocalDate.parse(createdAt), LocalDate.now())
    when {
        days == 0L -> "Today"
        days == 1L -> "1d ago"
        days < 7L  -> "${days}d ago"
        days < 30L -> "${days / 7}w ago"
        else       -> "${days / 30}mo ago"
    }
} catch (e: Exception) { "" }

/**
 * Haupt-Screen für Benachrichtigungen.
 * Teilt Notifications in "New" (ungelesen) und "Earlier" (gelesen) auf.
 * Lotterie-Gewinner können hier ihr Ticket direkt einlösen.
 */
@Composable
fun AlertsScreen(
    paddingValues: PaddingValues,
    notifications: List<NotificationEntity> = emptyList(),
    credits: Double = 0.0,
    currency: String = "EUR",
    isDarkMode: Boolean = false,
    onToggleDarkMode: () -> Unit = {},
    onNavigateToHome: () -> Unit = {},
    onNavigateToTickets: () -> Unit = {},
    onNavigateToProfile: () -> Unit = {},
    onNavigateToImprint: () -> Unit = {},
    onNavigateToSecondaryMarket: () -> Unit = {},
    onMarkRead: (Int) -> Unit = {},
    onMarkAllRead: () -> Unit = {},
    onDismiss: (Int) -> Unit = {},
    onClaimTicket: (notificationId: Int, eventId: Int) -> Unit = { _, _ -> },
    onClaimCashback: (notificationId: Int, amount: Double) -> Unit = { _, _ -> }
) {
    val newAlerts     = notifications.filter { !it.isRead }
    val earlierAlerts = notifications.filter { it.isRead }
    val unreadCount   = newAlerts.size
    var menuExpanded  by remember { mutableStateOf(false) }

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
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    Icon(Icons.Default.Wallet, null,
                        tint = Color.White, modifier = Modifier.size(13.dp))
                    Text(
                        formatPrice(credits, currency),
                        color = Color.White, fontWeight = FontWeight.Bold, fontSize = 12.sp
                    )
                }
            }
            Spacer(Modifier.width(4.dp))
            Box {
                IconButton(onClick = { menuExpanded = true }) {
                    Icon(Icons.Default.Menu, null,
                        tint = MaterialTheme.colorScheme.onSurface)
                }
                DropdownMenu(expanded = menuExpanded, onDismissRequest = { menuExpanded = false }) {
                    DropdownMenuItem(
                        text = { Text("Home") },
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
                        text = { Text("Profile") },
                        leadingIcon = { Icon(Icons.Default.Person, null) },
                        onClick = { menuExpanded = false; onNavigateToProfile() }
                    )
                    DropdownMenuItem(
                        text = { Text("Imprint") },
                        leadingIcon = { Icon(Icons.Default.Info, null) },
                        onClick = { menuExpanded = false; onNavigateToImprint() }
                    )
                    HorizontalDivider()
                    DropdownMenuItem(
                        text = { Text(if (isDarkMode) "Light Mode" else "Dark Mode") },
                        leadingIcon = {
                            Icon(
                                if (isDarkMode) Icons.Default.LightMode else Icons.Default.DarkMode,
                                null
                            )
                        },
                        onClick = { menuExpanded = false; onToggleDarkMode() }
                    )
                }
            }
        }

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(bottomStart = 28.dp, bottomEnd = 28.dp))
                .background(
                    Brush.verticalGradient(colors = listOf(Color(0xFF4A8AFF), Color(0xFF6B60F0)))
                )
                .padding(start = 20.dp, end = 20.dp, top = 18.dp, bottom = 22.dp)
        ) {
            Column {
                Text("Notifications",
                    color = Color.White, fontSize = 22.sp, fontWeight = FontWeight.Bold)
                Spacer(Modifier.height(2.dp))
                Text("Stay updated on your events",
                    color = Color.White.copy(alpha = 0.8f), fontSize = 13.sp)
            }
            if (unreadCount > 0) {
                Surface(
                    modifier = Modifier.align(Alignment.TopEnd),
                    shape = RoundedCornerShape(20.dp),
                    color = Color.White.copy(alpha = 0.25f)
                ) {
                    Text(
                        "$unreadCount new",
                        modifier = Modifier.padding(horizontal = 12.dp, vertical = 4.dp),
                        color = Color.White,
                        fontWeight = FontWeight.SemiBold,
                        fontSize = 12.sp
                    )
                }
            }
        }

        if (unreadCount > 0) {
            Row(
                modifier = Modifier.fillMaxWidth().padding(end = 16.dp, top = 4.dp),
                horizontalArrangement = Arrangement.End
            ) {
                TextButton(onClick = onMarkAllRead) {
                    Text("Mark all as read",
                        color = MaterialTheme.colorScheme.primary, fontSize = 13.sp)
                }
            }
        } else {
            Spacer(Modifier.height(8.dp))
        }

        if (notifications.isEmpty()) {
            Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Icon(Icons.Default.NotificationsNone, null,
                        modifier = Modifier.size(64.dp),
                        tint = MaterialTheme.colorScheme.onSurfaceVariant)
                    Spacer(Modifier.height(12.dp))
                    Text("No notifications",
                        color = MaterialTheme.colorScheme.onSurfaceVariant)
                }
            }
        } else {
            LazyColumn(
                modifier = Modifier.weight(1f).fillMaxWidth(),
                contentPadding = PaddingValues(horizontal = 16.dp, vertical = 4.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                if (newAlerts.isNotEmpty()) {
                    item {
                        Text("New", fontWeight = FontWeight.Bold, fontSize = 15.sp,
                            modifier = Modifier.padding(bottom = 2.dp))
                    }
                    items(newAlerts, key = { it.id }) { n ->
                        NotificationCard(
                            notification = n,
                            currency     = currency,
                            onDismiss    = { onDismiss(n.id) },
                            onAction     = {
                                when (n.type) {
                                    "lottery_win"  -> n.eventId?.let { onClaimTicket(n.id, it) }
                                        ?: onMarkRead(n.id)
                                    "lottery_lose" -> n.actionAmount?.let { onClaimCashback(n.id, it) }
                                        ?: onMarkRead(n.id)
                                    else           -> onMarkRead(n.id)
                                }
                            }
                        )
                    }
                    if (earlierAlerts.isNotEmpty()) {
                        item { Spacer(Modifier.height(6.dp)) }
                    }
                }
                if (earlierAlerts.isNotEmpty()) {
                    item {
                        Text("Earlier", fontWeight = FontWeight.Bold, fontSize = 15.sp,
                            modifier = Modifier.padding(bottom = 2.dp))
                    }
                    items(earlierAlerts, key = { it.id }) { n ->
                        NotificationCard(
                            notification = n,
                            onDismiss    = { onDismiss(n.id) },
                            onAction     = {}
                        )
                    }
                }
            }
        }
    }
}

/**
 * Eine einzelne Notification-Karte.
 * Ungelesene Karten haben einen farbigen linken Balken und einen Action-Button.
 * Der Cashback-Button-Text wird dynamisch aus actionAmount + currency gebaut,
 * damit er immer die richtige Währung zeigt.
 */
@Composable
private fun NotificationCard(
    notification: NotificationEntity,
    currency: String = "EUR",
    onDismiss: () -> Unit,
    onAction: () -> Unit
) {
    val iconColor   = colorForType(notification.type)
    val actionColor = actionColorForType(notification.type)
    val actionIcon  = actionIconForType(notification.type)

    Card(
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(
            containerColor = if (notification.isRead)
                MaterialTheme.colorScheme.surface
            else
                iconColor.copy(alpha = 0.07f)
        ),
        elevation = CardDefaults.cardElevation(0.dp)
    ) {
        Row(modifier = Modifier.height(IntrinsicSize.Min)) {
            if (!notification.isRead) {
                Box(
                    modifier = Modifier
                        .width(4.dp)
                        .fillMaxHeight()
                        .background(iconColor)
                )
            }
            Column(
                modifier = Modifier
                    .weight(1f)
                    .padding(horizontal = 14.dp, vertical = 12.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Row(
                    verticalAlignment = Alignment.Top,
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    Icon(
                        iconForType(notification.type), null,
                        tint = iconColor,
                        modifier = Modifier.size(28.dp)
                    )
                    Column(modifier = Modifier.weight(1f)) {
                        Text(notification.title,
                            fontWeight = FontWeight.Bold,
                            style = MaterialTheme.typography.bodyMedium)
                        Spacer(Modifier.height(3.dp))
                        Text(notification.message,
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant)
                    }
                    Column(horizontalAlignment = Alignment.End) {
                        Text(timeLabel(notification.createdAt),
                            style = MaterialTheme.typography.labelSmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant)
                        if (notification.isRead) {
                            Spacer(Modifier.height(4.dp))
                            IconButton(onClick = onDismiss, modifier = Modifier.size(20.dp)) {
                                Icon(Icons.Default.Delete, null,
                                    tint = MaterialTheme.colorScheme.onSurfaceVariant,
                                    modifier = Modifier.size(15.dp))
                            }
                        }
                    }
                }

                if (!notification.isRead && notification.actionLabel != null) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Row(
                            modifier = Modifier
                                .weight(1f)
                                .clip(RoundedCornerShape(50.dp))
                                .background(actionColor)
                                .clickable(
                                    indication = null,
                                    interactionSource = remember { MutableInteractionSource() }
                                ) { onAction() }
                                .padding(horizontal = 16.dp, vertical = 12.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.Center
                        ) {
                            if (actionIcon != null) {
                                Icon(actionIcon, null,
                                    tint = Color.White, modifier = Modifier.size(15.dp))
                                Spacer(Modifier.width(6.dp))
                            }
                            // Für lottery_lose: Betrag in gewählter Währung anzeigen
                            val buttonLabel = if (notification.type == "lottery_lose" && notification.actionAmount != null)
                                "Claim ${formatPrice(notification.actionAmount, currency)} Cashback"
                            else
                                notification.actionLabel ?: ""
                            Text(buttonLabel,
                                fontSize = 13.sp,
                                fontWeight = FontWeight.SemiBold,
                                color = Color.White)
                        }
                        IconButton(onClick = onDismiss, modifier = Modifier.size(36.dp)) {
                            Icon(Icons.Default.Delete, null,
                                tint = MaterialTheme.colorScheme.onSurfaceVariant,
                                modifier = Modifier.size(18.dp))
                        }
                    }
                }
            }
        }
    }
}
