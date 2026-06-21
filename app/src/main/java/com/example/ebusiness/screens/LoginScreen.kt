package com.example.ebusiness.screens

// Login-Screen mit Schnell-Login-Buttons (Host / Fan) für den Demo-Betrieb.
// Enthält auch einen "Passwort vergessen"-Dialog mit simuliertem E-Mail-Versand.

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.ConfirmationNumber
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ebusiness.ui.theme.StagePotGradient
import com.example.ebusiness.ui.theme.StagePotPurple

/**
 * Login-Screen — Einstiegspunkt der App.
 * Die "Quick Test Login"-Buttons umgehen die echte Auth und laden direkt den
 * passenden Demo-User aus der DB (Host oder Fan).
 * Der normale "Sign In"-Button prüft E-Mail-Hash + Passwort-Hash gegen die DB.
 */
@Composable
fun LoginScreen(
    onLoginAsHost: () -> Unit,
    onLoginAsFan: () -> Unit,
    onCreateAccount: () -> Unit,
    onGuestMode: () -> Unit,
    onLogin: (String, String) -> Unit = { _, _ -> },
    loginError: String? = null,
    onClearError: () -> Unit = {}
) {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }

    var showForgotDialog by remember { mutableStateOf(false) }
    var resetEmail by remember { mutableStateOf("") }
    var showSentConfirmation by remember { mutableStateOf(false) }

    if (showForgotDialog) {
        AlertDialog(
            onDismissRequest = {
                showForgotDialog = false
                resetEmail = ""
                showSentConfirmation = false
            },
            icon = { Icon(Icons.Default.Email, null) },
            title = { Text("Reset Password") },
            text = {
                if (showSentConfirmation) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Icon(Icons.Default.CheckCircle, null,
                            tint = Color(0xFF16A34A),
                            modifier = Modifier.size(40.dp))
                        Text(
                            "Password reset email sent to\n$resetEmail",
                            textAlign = TextAlign.Center
                        )
                        Text(
                            "Check your inbox and follow the instructions.",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            textAlign = TextAlign.Center
                        )
                    }
                } else {
                    Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                        Text("Enter your email address and we'll send you a link to reset your password.")
                        OutlinedTextField(
                            value = resetEmail,
                            onValueChange = { resetEmail = it },
                            label = { Text("Email address") },
                            leadingIcon = { Icon(Icons.Default.Email, null) },
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(10.dp),
                            singleLine = true
                        )
                    }
                }
            },
            confirmButton = {
                if (showSentConfirmation) {
                    Button(onClick = {
                        showForgotDialog = false
                        resetEmail = ""
                        showSentConfirmation = false
                    }) { Text("Done") }
                } else {
                    Button(
                        onClick = { if (resetEmail.isNotBlank()) showSentConfirmation = true },
                        enabled = resetEmail.isNotBlank()
                    ) { Text("Send Reset Link") }
                }
            },
            dismissButton = {
                if (!showSentConfirmation) {
                    TextButton(onClick = {
                        showForgotDialog = false
                        resetEmail = ""
                    }) { Text("Cancel") }
                }
            }
        )
    }

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
            Spacer(Modifier.height(40.dp))

            Box(
                modifier = Modifier
                    .size(80.dp)
                    .background(Color.White, CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    Icons.Default.ConfirmationNumber,
                    contentDescription = null,
                    tint = Color(0xFF4A8AFF),
                    modifier = Modifier.size(44.dp)
                )
            }

            Spacer(Modifier.height(14.dp))
            Text("StagePot", color = Color.White, fontSize = 28.sp, fontWeight = FontWeight.Bold)
            Text("Fair Tickets for Real Fans", color = Color.White.copy(alpha = 0.8f), fontSize = 14.sp)

            Spacer(Modifier.height(24.dp))

            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
            ) {
                Column(modifier = Modifier.padding(14.dp)) {
                    Text("🧪 Quick Test Login:", color = Color(0xFF4A8AFF), fontWeight = FontWeight.Medium, fontSize = 13.sp)
                    Spacer(Modifier.height(8.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        OutlinedButton(
                            onClick = onLoginAsHost,
                            modifier = Modifier.weight(1f),
                            colors = ButtonDefaults.outlinedButtonColors(contentColor = MaterialTheme.colorScheme.onSurface),
                            border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline)
                        ) { Text("Login as Host", fontSize = 12.sp) }

                        OutlinedButton(
                            onClick = onLoginAsFan,
                            modifier = Modifier.weight(1f),
                            colors = ButtonDefaults.outlinedButtonColors(contentColor = MaterialTheme.colorScheme.onSurface),
                            border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline)
                        ) { Text("Login as Fan", fontSize = 12.sp) }
                    }
                }
            }

            Spacer(Modifier.height(16.dp))

            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
            ) {
                Column(
                    modifier = Modifier.padding(24.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Text("Welcome Back", fontWeight = FontWeight.Bold, fontSize = 22.sp)

                    OutlinedTextField(
                        value = email,
                        onValueChange = { email = it; onClearError() },
                        label = { Text("Email") },
                        placeholder = { Text("your.email@example.com") },
                        leadingIcon = { Icon(Icons.Default.Email, null) },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(10.dp),
                        singleLine = true,
                        isError = loginError != null
                    )

                    OutlinedTextField(
                        value = password,
                        onValueChange = { password = it; onClearError() },
                        label = { Text("Password") },
                        placeholder = { Text("••••••••") },
                        leadingIcon = { Icon(Icons.Default.Lock, null) },
                        visualTransformation = PasswordVisualTransformation(),
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(10.dp),
                        singleLine = true,
                        isError = loginError != null
                    )

                    // Fehlermeldung anzeigen wenn Login fehlschlägt
                    if (loginError != null) {
                        Text(
                            loginError,
                            color = MaterialTheme.colorScheme.error,
                            style = MaterialTheme.typography.bodySmall,
                            modifier = Modifier.fillMaxWidth()
                        )
                    }

                    Button(
                        onClick = { onLogin(email, password) },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(10.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF111827))
                    ) {
                        Text("Sign In", modifier = Modifier.padding(vertical = 4.dp))
                    }

                    TextButton(onClick = { showForgotDialog = true }, modifier = Modifier.fillMaxWidth()) {
                        Text("Forgot password?", color = Color(0xFF4A8AFF))
                    }

                    HorizontalDivider()
                    Text("OR", modifier = Modifier.fillMaxWidth(), textAlign = TextAlign.Center, color = Color.Gray, fontSize = 12.sp)
                    HorizontalDivider()

                    OutlinedButton(
                        onClick = onCreateAccount,
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(10.dp)
                    ) {
                        Text("Create Account")
                    }

                    TextButton(onClick = onGuestMode, modifier = Modifier.fillMaxWidth()) {
                        Text("Continue as Guest", color = Color.Gray)
                    }
                }
            }

            Spacer(Modifier.height(16.dp))
            Text(
                "By continuing, you agree to our Terms of Service and Privacy Policy",
                color = Color.White.copy(alpha = 0.6f),
                fontSize = 11.sp,
                textAlign = TextAlign.Center
            )
            Spacer(Modifier.height(24.dp))
        }
    }
}
