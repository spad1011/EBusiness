package com.example.ebusiness.screens

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

data class AlertItem(
    val id: Int,
    val icon: ImageVector,
    val iconColor: Color,
    val title: String,
    val message: String,
    val time: String,
    val isRead: Boolean,
    val actionLabel: String? = null,
    val actionColor: Color? = null,
    val actionIcon: ImageVector? = null,
    val hasViewEvent: Boolean = false
)

@Composable
fun AlertsScreen(paddingValues: PaddingValues) {
    val alerts = remember {
        mutableStateListOf(
            AlertItem(
                id = 1,
                icon = Icons.Default.CheckCircle,
                iconColor = Color(0xFF16A34A),
                title = "Lottery Win! 🎉",
                message = "Congratulations! You won the lottery ticket for Summer Music Festival 2026",
                time = "2d ago",
                isRead = false,
                actionLabel = "Claim Ticket",
                actionColor = Color(0xFF111827),
                actionIcon = Icons.Default.ConfirmationNumber,
                hasViewEvent = true
            ),
            AlertItem(
                id = 2,
                icon = Icons.Default.Cancel,
                iconColor = Color(0xFFEA580C),
                title = "Lottery Result",
                message = "Unfortunately you didn't win the lottery for NBA Finals Game 5. Get your 25% cashback now!",
                time = "2d ago",
                isRead = false,
                actionLabel = "Claim \$0.75 Cashback",
                actionColor = Color(0xFFEA580C),
                actionIcon = Icons.Default.CardGiftcard,
                hasViewEvent = true
            ),
            AlertItem(
                id = 3,
                icon = Icons.Default.AccessTime,
                iconColor = Color(0xFF2563EB),
                title = "Event Reminder",
                message = "Jazz Night Live is tomorrow! Don't forget to bring your ticket.",
                time = "2d ago",
                isRead = true
            ),
            AlertItem(
                id = 4,
                icon = Icons.Default.LocalOffer,
                iconColor = Color(0xFFFFB300),
                title = "New Offer",
                message = "Special prices for Techno Festival 48h – today only!",
                time = "5d ago",
                isRead = true
            ),
            AlertItem(
                id = 5,
                icon = Icons.Default.Stars,
                iconColor = Color(0xFF7C3AED),
                title = "Credits Earned",
                message = "You received \$5.00 credits through lottery cashback.",
                time = "1w ago",
                isRead = true
            ),
        )
    }

    val newAlerts    = alerts.filter { !it.isRead }
    val earlierAlerts = alerts.filter { it.isRead }
    val unreadCount  = newAlerts.size

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(bottom = paddingValues.calculateBottomPadding())
    ) {
        StagePotBrandBar()

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(bottomStart = 28.dp, bottomEnd = 28.dp))
                .background(
                    Brush.verticalGradient(
                        colors = listOf(Color(0xFF4A8AFF), Color(0xFF6B60F0))
                    )
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
                TextButton(onClick = { alerts.replaceAll { it.copy(isRead = true) } }) {
                    Text("Mark all as read",
                        color = MaterialTheme.colorScheme.primary,
                        fontSize = 13.sp)
                }
            }
        } else {
            Spacer(Modifier.height(8.dp))
        }

        if (alerts.isEmpty()) {
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
                        Text("New",
                            fontWeight = FontWeight.Bold,
                            fontSize = 15.sp,
                            modifier = Modifier.padding(bottom = 2.dp))
                    }
                    items(newAlerts, key = { it.id }) { alert ->
                        AlertCard(
                            alert = alert,
                            onDismiss = { alerts.remove(alert) },
                            onAction = { alerts[alerts.indexOfFirst { it.id == alert.id }] = alert.copy(isRead = true) }
                        )
                    }
                    if (earlierAlerts.isNotEmpty()) {
                        item { Spacer(Modifier.height(6.dp)) }
                    }
                }

                if (earlierAlerts.isNotEmpty()) {
                    item {
                        Text("Earlier",
                            fontWeight = FontWeight.Bold,
                            fontSize = 15.sp,
                            modifier = Modifier.padding(bottom = 2.dp))
                    }
                    items(earlierAlerts, key = { it.id }) { alert ->
                        AlertCard(
                            alert = alert,
                            onDismiss = { alerts.remove(alert) },
                            onAction = {}
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun AlertCard(
    alert: AlertItem,
    onDismiss: () -> Unit,
    onAction: () -> Unit
) {
    Card(
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(
            containerColor = if (alert.isRead)
                MaterialTheme.colorScheme.surface
            else
                alert.iconColor.copy(alpha = 0.07f)
        ),
        elevation = CardDefaults.cardElevation(0.dp)
    ) {
        Row(modifier = Modifier.height(IntrinsicSize.Min)) {
            if (!alert.isRead) {
                Box(
                    modifier = Modifier
                        .width(4.dp)
                        .fillMaxHeight()
                        .background(alert.iconColor)
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
                        alert.icon,
                        contentDescription = null,
                        tint = alert.iconColor,
                        modifier = Modifier.size(28.dp)
                    )

                    Column(modifier = Modifier.weight(1f)) {
                        Text(alert.title,
                            fontWeight = FontWeight.Bold,
                            style = MaterialTheme.typography.bodyMedium)
                        Spacer(Modifier.height(3.dp))
                        Text(alert.message,
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant)
                    }

                    Column(horizontalAlignment = Alignment.End) {
                        Text(alert.time,
                            style = MaterialTheme.typography.labelSmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant)
                        if (alert.isRead) {
                            Spacer(Modifier.height(4.dp))
                            IconButton(onClick = onDismiss, modifier = Modifier.size(20.dp)) {
                                Icon(Icons.Default.Delete, null,
                                    tint = MaterialTheme.colorScheme.onSurfaceVariant,
                                    modifier = Modifier.size(15.dp))
                            }
                        }
                    }
                }

                if (!alert.isRead && alert.actionLabel != null) {
                    val btnColor = alert.actionColor ?: MaterialTheme.colorScheme.primary
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Row(
                            modifier = Modifier
                                .weight(1f)
                                .clip(RoundedCornerShape(50.dp))
                                .background(btnColor)
                                .clickable(
                                    indication = null,
                                    interactionSource = remember { MutableInteractionSource() }
                                ) { onAction() }
                                .padding(horizontal = 16.dp, vertical = 12.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.Center
                        ) {
                            if (alert.actionIcon != null) {
                                Icon(alert.actionIcon, null,
                                    tint = Color.White,
                                    modifier = Modifier.size(15.dp))
                                Spacer(Modifier.width(6.dp))
                            }
                            Text(alert.actionLabel,
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
                    if (alert.hasViewEvent) {
                        TextButton(
                            onClick = onAction,
                            contentPadding = PaddingValues(0.dp)
                        ) {
                            Text("View Event →",
                                color = MaterialTheme.colorScheme.primary,
                                fontSize = 13.sp)
                        }
                    }
                }
            }
        }
    }
}
