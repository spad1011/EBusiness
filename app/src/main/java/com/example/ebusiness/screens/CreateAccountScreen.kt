package com.example.ebusiness.screens

// Registrierungsflow in zwei Schritten:
// 1. Accountdaten (Name, E-Mail, Passwort, Standort)
// 2. Zahlungsmethoden (optional, kann später hinzugefügt werden)

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ebusiness.ui.theme.StagePotGradient
import com.example.ebusiness.ui.theme.StagePotPurple

/**
 * Registrierungsscreen — führt den User in zwei Schritten durch die Kontoerstellung.
 * Am Ende wird onSuccess mit den eingegebenen Daten aufgerufen; das ViewModel
 * legt den User dann in der DB an und loggt ihn direkt ein.
 */
@Composable
fun CreateAccountScreen(
    onBack: () -> Unit,
    onSuccess: (displayName: String, email: String, phone: String, location: String, userType: String) -> Unit
) {
    var step by remember { mutableIntStateOf(1) }
    var selectedAccountType by remember { mutableStateOf("Private User") }
    var fullName by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var phone by remember { mutableStateOf("") }
    var location by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }
    var selectedPayments by remember { mutableStateOf(setOf<String>()) }

    val accountTypes = listOf(
        Triple("Private User", Icons.Default.Person, "For personal event attendance"),
        Triple("Professional Account", Icons.Default.Business, "For business & corporate events"),
        Triple("Event Host/Organizer", Icons.Default.Star, "Create and manage events")
    )
    val paymentMethods = listOf("PayPal", "Google Play", "Apple Pay", "Credit/Debit Card")

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(brush = StagePotGradient)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .statusBarsPadding()
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(Modifier.height(30.dp))

            Box(
                modifier = Modifier
                    .size(64.dp)
                    .background(Color.White, CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    Icons.Default.ConfirmationNumber,
                    contentDescription = null,
                    tint = StagePotPurple,
                    modifier = Modifier.size(36.dp)
                )
            }

            Spacer(Modifier.height(10.dp))
            Text("Create Account", color = Color.White, fontSize = 24.sp, fontWeight = FontWeight.Bold)
            Text("Join StagePot today", color = Color.White.copy(alpha = 0.8f), fontSize = 13.sp)

            Spacer(Modifier.height(20.dp))

            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
            ) {
                Column(
                    modifier = Modifier.padding(24.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    if (step == 1) {
                        TextButton(
                            onClick = onBack,
                            modifier = Modifier.align(Alignment.Start)
                        ) {
                            Icon(Icons.AutoMirrored.Filled.ArrowBack, null, modifier = Modifier.size(16.dp))
                            Spacer(Modifier.width(4.dp))
                            Text("Back to Login")
                        }

                        Text("Account Information", fontWeight = FontWeight.Bold, fontSize = 18.sp)

                        Text("Account Type", fontWeight = FontWeight.Medium, fontSize = 13.sp, color = Color.Gray)

                        accountTypes.forEach { (type, icon, desc) ->
                            AccountTypeCard(
                                type = type,
                                icon = icon,
                                desc = desc,
                                selected = selectedAccountType == type,
                                onClick = { selectedAccountType = type }
                            )
                        }

                        OutlinedTextField(
                            value = fullName, onValueChange = { fullName = it },
                            label = { Text("Full Name *") },
                            placeholder = { Text("John Doe") },
                            leadingIcon = { Icon(Icons.Default.Person, null) },
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(10.dp), singleLine = true
                        )
                        OutlinedTextField(
                            value = email, onValueChange = { email = it },
                            label = { Text("Email *") },
                            placeholder = { Text("your.email@example.com") },
                            leadingIcon = { Icon(Icons.Default.Email, null) },
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(10.dp), singleLine = true
                        )
                        OutlinedTextField(
                            value = phone, onValueChange = { phone = it },
                            label = { Text("Phone Number (Optional)") },
                            placeholder = { Text("+1 (555) 123-4567") },
                            leadingIcon = { Icon(Icons.Default.Phone, null) },
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(10.dp), singleLine = true
                        )
                        OutlinedTextField(
                            value = location, onValueChange = { location = it },
                            label = { Text("Location (Optional)") },
                            placeholder = { Text("San Francisco, CA") },
                            leadingIcon = { Icon(Icons.Default.LocationOn, null) },
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(10.dp), singleLine = true
                        )
                        OutlinedTextField(
                            value = password, onValueChange = { password = it },
                            label = { Text("Password *") },
                            placeholder = { Text("Min. 8 characters") },
                            leadingIcon = { Icon(Icons.Default.Lock, null) },
                            visualTransformation = PasswordVisualTransformation(),
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(10.dp), singleLine = true
                        )
                        OutlinedTextField(
                            value = confirmPassword, onValueChange = { confirmPassword = it },
                            label = { Text("Confirm Password *") },
                            placeholder = { Text("Re-enter password") },
                            leadingIcon = { Icon(Icons.Default.Lock, null) },
                            visualTransformation = PasswordVisualTransformation(),
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(10.dp), singleLine = true
                        )

                        Button(
                            onClick = { step = 2 },
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(10.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = Color.Black)
                        ) {
                            Text("Continue to Payment Setup", modifier = Modifier.padding(vertical = 4.dp))
                        }

                    } else {
                        TextButton(
                            onClick = { step = 1 },
                            modifier = Modifier.align(Alignment.Start)
                        ) {
                            Icon(Icons.AutoMirrored.Filled.ArrowBack, null, modifier = Modifier.size(16.dp))
                            Spacer(Modifier.width(4.dp))
                            Text("Back")
                        }

                        Text("Payment Methods", fontWeight = FontWeight.Bold, fontSize = 18.sp)
                        Text(
                            "Link your payment methods (optional - you can add these later)",
                            fontSize = 13.sp, color = Color.Gray
                        )

                        Text("Add Payment Method", fontWeight = FontWeight.Medium, fontSize = 13.sp)

                        Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                paymentMethods.take(2).forEach { method ->
                                    PaymentMethodCard(
                                        method = method,
                                        selected = method in selectedPayments,
                                        onClick = {
                                            selectedPayments = if (method in selectedPayments)
                                                selectedPayments - method else selectedPayments + method
                                        },
                                        modifier = Modifier.weight(1f)
                                    )
                                }
                            }
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                paymentMethods.drop(2).forEach { method ->
                                    PaymentMethodCard(
                                        method = method,
                                        selected = method in selectedPayments,
                                        onClick = {
                                            selectedPayments = if (method in selectedPayments)
                                                selectedPayments - method else selectedPayments + method
                                        },
                                        modifier = Modifier.weight(1f)
                                    )
                                }
                            }
                        }

                        Button(
                            onClick = {
                                val mappedType = when (selectedAccountType) {
                                    "Event Host/Organizer" -> "host"
                                    else -> "fan"
                                }
                                onSuccess(fullName, email, phone, location, mappedType)
                            },
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(10.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = Color.Black)
                        ) {
                            Text("Create Account", modifier = Modifier.padding(vertical = 4.dp))
                        }

                        TextButton(
                            onClick = {
                                onSuccess(fullName, email, phone, location, "fan")
                            },
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text("Skip for Now", color = Color.Gray)
                        }

                        HorizontalDivider()

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.Center
                        ) {
                            Text("Already have an account? ", color = Color.Gray, fontSize = 13.sp)
                            Text(
                                "Sign In",
                                color = StagePotPurple,
                                fontWeight = FontWeight.Bold,
                                fontSize = 13.sp,
                                modifier = Modifier.clickable { onBack() }
                            )
                        }
                    }
                }
            }
            Spacer(Modifier.height(32.dp))
        }
    }
}

/** Auswahlkarte für den Account-Typ (Private User / Professional / Host) */
@Composable
private fun AccountTypeCard(
    type: String,
    icon: ImageVector,
    desc: String,
    selected: Boolean,
    onClick: () -> Unit
) {
    OutlinedCard(
        modifier = Modifier.fillMaxWidth().clickable { onClick() },
        shape = RoundedCornerShape(10.dp),
        border = BorderStroke(
            if (selected) 2.dp else 1.dp,
            if (selected) StagePotPurple else Color.LightGray
        ),
        colors = CardDefaults.outlinedCardColors(
            containerColor = if (selected) StagePotPurple.copy(alpha = 0.1f) else MaterialTheme.colorScheme.surface
        )
    ) {
        Row(
            modifier = Modifier.padding(12.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            RadioButton(
                selected = selected,
                onClick = onClick,
                colors = RadioButtonDefaults.colors(selectedColor = StagePotPurple)
            )
            Icon(icon, null, tint = StagePotPurple, modifier = Modifier.size(20.dp))
            Column {
                Text(type, fontWeight = FontWeight.Medium, fontSize = 14.sp)
                Text(desc, fontSize = 11.sp, color = Color.Gray)
            }
        }
    }
}

/** Kleine Kachel für eine Zahlungsmethode im Registrierungsschritt 2 */
@Composable
private fun PaymentMethodCard(
    method: String,
    selected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val emoji = when (method) {
        "PayPal" -> "💳"
        "Google Play" -> "🎮"
        "Apple Pay" -> "🍎"
        else -> "💰"
    }

    OutlinedCard(
        modifier = modifier.clickable { onClick() },
        shape = RoundedCornerShape(10.dp),
        border = BorderStroke(
            if (selected) 2.dp else 1.dp,
            if (selected) StagePotPurple else Color.LightGray
        ),
        colors = CardDefaults.outlinedCardColors(
            containerColor = if (selected) StagePotPurple.copy(alpha = 0.1f) else MaterialTheme.colorScheme.surface
        )
    ) {
        Column(
            modifier = Modifier.fillMaxWidth().padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            Text(emoji, fontSize = 26.sp)
            Text(method, fontSize = 11.sp, fontWeight = FontWeight.Medium, textAlign = TextAlign.Center)
        }
    }
}
