package com.example.ebusiness.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Lightbulb
import androidx.compose.material.icons.filled.VerifiedUser
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

data class SavedPaymentMethod(
    val id: Int,
    val name: String,
    val type: String,
    val emoji: String,
    val isVerified: Boolean
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PaymentMethodsScreen(onBack: () -> Unit) {
    val savedMethods = remember {
        mutableStateListOf(
            SavedPaymentMethod(1, "PayPal Business", "paypal", "💳", true),
            SavedPaymentMethod(2, "Business Visa", "card", "💳", true),
        )
    }

    Column(modifier = Modifier.fillMaxSize()) {
        TopAppBar(
            title = {
                Column {
                    Text("Payment Methods", fontWeight = FontWeight.Bold)
                    Text("Manage your payment options",
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.onPrimary.copy(alpha = 0.8f))
                }
            },
            navigationIcon = {
                IconButton(onClick = onBack) {
                    Icon(Icons.AutoMirrored.Filled.ArrowBack, null,
                        tint = MaterialTheme.colorScheme.onPrimary)
                }
            },
            colors = TopAppBarDefaults.topAppBarColors(
                containerColor = MaterialTheme.colorScheme.primary,
                titleContentColor = MaterialTheme.colorScheme.onPrimary
            )
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Card(
                shape = RoundedCornerShape(12.dp),
                colors = CardDefaults.cardColors(
                    containerColor = Color(0xFFDCFCE7)
                )
            ) {
                Row(
                    modifier = Modifier.padding(14.dp),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    Icon(Icons.Default.VerifiedUser, null,
                        tint = Color(0xFF16A34A), modifier = Modifier.size(20.dp))
                    Column {
                        Text("Secure & Encrypted",
                            fontWeight = FontWeight.SemiBold,
                            color = Color(0xFF166534),
                            style = MaterialTheme.typography.bodySmall)
                        Text("All payment information is encrypted and stored securely.",
                            style = MaterialTheme.typography.labelSmall,
                            color = Color(0xFF166534))
                    }
                }
            }

            Text("Saved Payment Methods",
                fontWeight = FontWeight.Bold,
                style = MaterialTheme.typography.titleMedium)

            if (savedMethods.isEmpty()) {
                Card(shape = RoundedCornerShape(12.dp)) {
                    Box(
                        modifier = Modifier.fillMaxWidth().padding(24.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text("No saved payment methods",
                            color = MaterialTheme.colorScheme.onSurfaceVariant)
                    }
                }
            } else {
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    savedMethods.forEach { method ->
                        Card(
                            shape = RoundedCornerShape(12.dp),
                            colors = CardDefaults.cardColors(
                                containerColor = MaterialTheme.colorScheme.surface
                            ),
                            elevation = CardDefaults.cardElevation(2.dp)
                        ) {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(14.dp),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(12.dp)
                            ) {
                                Text(method.emoji, fontSize = 22.sp)
                                Column(modifier = Modifier.weight(1f)) {
                                    Text(method.name, fontWeight = FontWeight.Medium)
                                    if (method.isVerified) {
                                        Row(
                                            verticalAlignment = Alignment.CenterVertically,
                                            horizontalArrangement = Arrangement.spacedBy(4.dp)
                                        ) {
                                            Icon(Icons.Default.CheckCircle, null,
                                                tint = Color(0xFF16A34A),
                                                modifier = Modifier.size(12.dp))
                                            Text("Verified",
                                                style = MaterialTheme.typography.labelSmall,
                                                color = Color(0xFF16A34A))
                                        }
                                    }
                                }
                                IconButton(
                                    onClick = { savedMethods.remove(method) },
                                    modifier = Modifier.size(32.dp)
                                ) {
                                    Icon(Icons.Default.Delete, null,
                                        tint = MaterialTheme.colorScheme.error,
                                        modifier = Modifier.size(18.dp))
                                }
                            }
                        }
                    }
                }
            }

            Text("Add New Payment Method",
                fontWeight = FontWeight.Bold,
                style = MaterialTheme.typography.titleMedium)

            val newMethods = listOf(
                "PayPal"           to "💳",
                "Google Play"      to "🎮",
                "Apple Pay"        to "🍎",
                "Credit/Debit Card" to "💳",
            )

            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                newMethods.chunked(2).forEach { row ->
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        row.forEach { (name, emoji) ->
                            Card(
                                modifier = Modifier
                                    .weight(1f)
                                    .clickable {
                                        savedMethods.add(
                                            SavedPaymentMethod(
                                                savedMethods.size + 10,
                                                name,
                                                "new",
                                                emoji,
                                                false
                                            )
                                        )
                                    },
                                shape = RoundedCornerShape(12.dp),
                                border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline.copy(alpha = 0.3f)),
                                colors = CardDefaults.cardColors(
                                    containerColor = MaterialTheme.colorScheme.surface
                                )
                            ) {
                                Column(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(16.dp),
                                    horizontalAlignment = Alignment.CenterHorizontally,
                                    verticalArrangement = Arrangement.spacedBy(8.dp)
                                ) {
                                    Text(emoji, fontSize = 28.sp)
                                    Text(name,
                                        style = MaterialTheme.typography.bodySmall,
                                        fontWeight = FontWeight.Medium)
                                    Icon(Icons.Default.Add, null,
                                        tint = MaterialTheme.colorScheme.primary,
                                        modifier = Modifier.size(16.dp))
                                }
                            }
                        }
                        if (row.size == 1) Spacer(Modifier.weight(1f))
                    }
                }
            }

            Card(
                shape = RoundedCornerShape(12.dp),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surfaceVariant
                )
            ) {
                Row(
                    modifier = Modifier.padding(12.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Icon(Icons.Default.Lightbulb, null,
                        tint = Color(0xFFFFB300), modifier = Modifier.size(16.dp))
                    Text(
                        "Tip: Adding multiple payment methods gives you flexibility when purchasing tickets or entering lottery draws.",
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }

            Spacer(Modifier.height(16.dp))
        }
    }
}
