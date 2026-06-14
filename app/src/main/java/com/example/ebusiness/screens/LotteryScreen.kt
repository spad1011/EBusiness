package com.example.ebusiness.screens

import androidx.compose.foundation.background
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
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ebusiness.data.MockData

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LotteryScreen(
    eventId: Int,
    onBack: () -> Unit,
    onSuccess: () -> Unit
) {
    val event = MockData.events.find { it.id == eventId } ?: return

    var entries by remember { mutableStateOf(1) }
    var selectedPayment by remember { mutableStateOf(0) } // 0 = Card/PayPal, 1 = Credits
    var showConfirmDialog by remember { mutableStateOf(false) }

    val pricePerEntry = 1.09
    val totalPrice = pricePerEntry * entries

    Column(modifier = Modifier.fillMaxSize()) {

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(
                    Brush.horizontalGradient(
                        listOf(Color(0xFFFFB300), Color(0xFFFF6D00))
                    )
                )
                .padding(top = 40.dp, bottom = 20.dp, start = 16.dp, end = 16.dp)
        ) {
            IconButton(
                onClick = onBack,
                modifier = Modifier.align(Alignment.TopStart)
            ) {
                Icon(Icons.AutoMirrored.Filled.ArrowBack, null, tint = Color.White)
            }
            Column(
                modifier = Modifier.padding(top = 48.dp),
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Icon(Icons.Default.Casino, null,
                        tint = Color.White, modifier = Modifier.size(28.dp))
                    Text("Ticket Lottery",
                        fontWeight = FontWeight.Bold, fontSize = 22.sp, color = Color.White)
                }
                Text(event.title,
                    style = MaterialTheme.typography.bodyMedium,
                    color = Color.White.copy(alpha = 0.85f))
            }
        }

        Column(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
                .verticalScroll(rememberScrollState())
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {

            Card(
                shape = RoundedCornerShape(14.dp),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.5f)
                )
            ) {
                Column(modifier = Modifier.padding(14.dp),
                    verticalArrangement = Arrangement.spacedBy(4.dp)) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        Icon(Icons.Default.Info, null,
                            tint = MaterialTheme.colorScheme.primary,
                            modifier = Modifier.size(16.dp))
                        Text("How it works:",
                            fontWeight = FontWeight.Bold,
                            style = MaterialTheme.typography.bodyMedium)
                    }
                    val rules = listOf(
                        "Each lottery entry costs \$1.09",
                        "Win: Get a full-price ticket",
                        "Lose: Receive 25% cashback as credits",
                        "Credits can be used for future lottery entries"
                    )
                    rules.forEach {
                        Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                            Text("•", color = MaterialTheme.colorScheme.primary,
                                fontWeight = FontWeight.Bold)
                            Text(it, style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant)
                        }
                    }
                }
            }

            Card(
                shape = RoundedCornerShape(14.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                elevation = CardDefaults.cardElevation(2.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        Icon(Icons.Default.Timer, null,
                            tint = Color(0xFFFFB300),
                            modifier = Modifier.size(16.dp))
                        Text("Time Remaining",
                            fontWeight = FontWeight.SemiBold,
                            style = MaterialTheme.typography.bodyMedium)
                        Spacer(Modifier.weight(1f))
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(6.dp))
                                .background(Brush.horizontalGradient(listOf(Color(0xFFFFB300), Color(0xFFFF6D00))))
                                .padding(horizontal = 8.dp, vertical = 2.dp)
                        ) {
                            Text("Ends Soon",
                                style = MaterialTheme.typography.labelSmall,
                                color = Color.White,
                                fontWeight = FontWeight.Bold)
                        }
                    }
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        TimeBox(modifier = Modifier.weight(1f), value = "13", label = "Days")
                        TimeBox(modifier = Modifier.weight(1f), value = "7", label = "Hours")
                    }
                }
            }

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                StatCard(
                    modifier = Modifier.weight(1f),
                    icon = Icons.Default.Group,
                    value = "12.847",
                    label = "Total Entries"
                )
                StatCard(
                    modifier = Modifier.weight(1f),
                    icon = Icons.Default.TrendingUp,
                    value = "0.01%",
                    label = "Win Chance",
                    valueColor = Color(0xFF22C55E)
                )
            }

            Card(
                shape = RoundedCornerShape(14.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                elevation = CardDefaults.cardElevation(2.dp)
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    Text("Enter Lottery",
                        fontWeight = FontWeight.Bold,
                        style = MaterialTheme.typography.titleMedium)

                    Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                        Text("Number of Entries",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant)
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            OutlinedTextField(
                                value = entries.toString(),
                                onValueChange = {},
                                modifier = Modifier.weight(1f),
                                readOnly = true,
                                shape = RoundedCornerShape(8.dp),
                                singleLine = true
                            )
                            Column(modifier = Modifier.padding(start = 4.dp)) {
                                IconButton(
                                    onClick = { if (entries < 99) entries++ },
                                    modifier = Modifier.size(28.dp)
                                ) {
                                    Icon(Icons.Default.KeyboardArrowUp, null,
                                        modifier = Modifier.size(20.dp))
                                }
                                IconButton(
                                    onClick = { if (entries > 1) entries-- },
                                    modifier = Modifier.size(28.dp)
                                ) {
                                    Icon(Icons.Default.KeyboardArrowDown, null,
                                        modifier = Modifier.size(20.dp))
                                }
                            }
                        }
                        Text(
                            "\$%.2f per entry × $entries = \$%.2f".format(pricePerEntry, totalPrice),
                            style = MaterialTheme.typography.labelSmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }

                    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                        Text("Payment Method",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant)
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            listOf("Card/PayPal", "Credits").forEachIndexed { idx, label ->
                                OutlinedButton(
                                    onClick = { selectedPayment = idx },
                                    modifier = Modifier.weight(1f),
                                    shape = RoundedCornerShape(8.dp),
                                    colors = ButtonDefaults.outlinedButtonColors(
                                        containerColor = if (selectedPayment == idx)
                                            MaterialTheme.colorScheme.primaryContainer
                                        else Color.Transparent,
                                        contentColor = if (selectedPayment == idx)
                                            MaterialTheme.colorScheme.primary
                                        else MaterialTheme.colorScheme.onSurface
                                    )
                                ) { Text(label) }
                            }
                        }

                        Card(
                            shape = RoundedCornerShape(10.dp),
                            colors = CardDefaults.cardColors(
                                containerColor = MaterialTheme.colorScheme.surfaceVariant
                            )
                        ) {
                            Row(
                                modifier = Modifier.fillMaxWidth().padding(14.dp),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(10.dp)
                            ) {
                                Icon(
                                    if (selectedPayment == 0) Icons.Default.CreditCard
                                    else Icons.Default.Wallet,
                                    null,
                                    tint = MaterialTheme.colorScheme.primary
                                )
                                Text(
                                    if (selectedPayment == 0) "Pay with Card/PayPal"
                                    else "Pay with Credits (\$25.00 available)",
                                    style = MaterialTheme.typography.bodySmall
                                )
                            }
                        }

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text("Total:", fontWeight = FontWeight.Bold)
                            Text(
                                "\$%.2f".format(totalPrice),
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.primary
                            )
                        }
                    }

                    Button(
                        onClick = { showConfirmDialog = true },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(10.dp)
                    ) {
                        Icon(Icons.Default.Casino, null)
                        Spacer(Modifier.width(8.dp))
                        Text("Enter Lottery — \$%.2f".format(totalPrice),
                            modifier = Modifier.padding(vertical = 4.dp))
                    }
                }
            }
            Spacer(Modifier.height(8.dp))
        }
    }

    if (showConfirmDialog) {
        AlertDialog(
            onDismissRequest = { showConfirmDialog = false },
            title = { Text("Confirm Lottery Entry") },
            text = {
                Text(
                    "$entries entry/entries for \$%.2f\n\n".format(totalPrice) +
                    "Win → Ticket confirmed ✅\n" +
                    "Lose → 25% cashback as credits 💰"
                )
            },
            confirmButton = {
                Button(onClick = { showConfirmDialog = false; onSuccess() }) {
                    Text("Confirm!")
                }
            },
            dismissButton = {
                TextButton(onClick = { showConfirmDialog = false }) { Text("Cancel") }
            }
        )
    }
}

@Composable
private fun TimeBox(modifier: Modifier = Modifier, value: String, label: String) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(10.dp),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surfaceVariant
            )
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 16.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    value,
                    fontWeight = FontWeight.Bold,
                    fontSize = 32.sp
                )
            }
        }
        Spacer(Modifier.height(6.dp))
        Text(label, style = MaterialTheme.typography.labelMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant)
    }
}

@Composable
private fun StatCard(
    modifier: Modifier = Modifier,
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    value: String,
    label: String,
    valueColor: Color = Color.Unspecified
) {
    Card(
        modifier = modifier,
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(2.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 20.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Icon(icon, null,
                tint = MaterialTheme.colorScheme.primary,
                modifier = Modifier.size(24.dp))
            Text(value,
                fontWeight = FontWeight.Bold,
                fontSize = 26.sp,
                color = if (valueColor == Color.Unspecified)
                    MaterialTheme.colorScheme.onSurface else valueColor)
            Text(label,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant)
        }
    }
}
