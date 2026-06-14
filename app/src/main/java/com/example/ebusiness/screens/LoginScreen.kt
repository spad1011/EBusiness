package com.example.ebusiness.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
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

@Composable
fun LoginScreen(
    onLoginAsHost: () -> Unit,
    onLoginAsFan: () -> Unit,
    onCreateAccount: () -> Unit,
    onGuestMode: () -> Unit
) {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }

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
                colors = CardDefaults.cardColors(containerColor = Color.White)
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
                            colors = ButtonDefaults.outlinedButtonColors(contentColor = Color(0xFF111827)),
                            border = BorderStroke(1.dp, Color(0xFFD1D5DB))
                        ) { Text("Login as Host", fontSize = 12.sp) }

                        OutlinedButton(
                            onClick = onLoginAsFan,
                            modifier = Modifier.weight(1f),
                            colors = ButtonDefaults.outlinedButtonColors(contentColor = Color(0xFF111827)),
                            border = BorderStroke(1.dp, Color(0xFFD1D5DB))
                        ) { Text("Login as Fan", fontSize = 12.sp) }
                    }
                }
            }

            Spacer(Modifier.height(16.dp))

            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White)
            ) {
                Column(
                    modifier = Modifier.padding(24.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Text("Welcome Back", fontWeight = FontWeight.Bold, fontSize = 22.sp)

                    OutlinedTextField(
                        value = email,
                        onValueChange = { email = it },
                        label = { Text("Email") },
                        placeholder = { Text("your.email@example.com") },
                        leadingIcon = { Icon(Icons.Default.Email, null) },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(10.dp),
                        singleLine = true
                    )

                    OutlinedTextField(
                        value = password,
                        onValueChange = { password = it },
                        label = { Text("Password") },
                        placeholder = { Text("••••••••") },
                        leadingIcon = { Icon(Icons.Default.Lock, null) },
                        visualTransformation = PasswordVisualTransformation(),
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(10.dp),
                        singleLine = true
                    )

                    Button(
                        onClick = onLoginAsFan,
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(10.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF111827))
                    ) {
                        Text("Sign In", modifier = Modifier.padding(vertical = 4.dp))
                    }

                    TextButton(onClick = {}, modifier = Modifier.fillMaxWidth()) {
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
