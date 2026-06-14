package com.example.ebusiness.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditProfileScreen(
    onBack: () -> Unit,
    userType: String = "fan"
) {
    val isHost = userType == "host"

    var selectedTab by remember { mutableStateOf(0) } // 0=Profile Info, 1=Password

    val accountTypes = listOf(
        Triple("Private User", "For personal event attendance", Icons.Default.Person),
        Triple("Professional Account", "For business & corporate events", Icons.Default.Work),
        Triple("Event Host/Organizer", "Create and manage events", Icons.Default.Dashboard)
    )
    var selectedAccountType by remember {
        mutableStateOf(if (isHost) 2 else 0)
    }
    var fullName by remember { mutableStateOf(if (isHost) "Event Host Pro" else "Music Fan") }
    var email by remember { mutableStateOf(if (isHost) "test_host@stagepot.com" else "test_fan@stagepot.com") }
    var phone by remember { mutableStateOf(if (isHost) "+1 (555) 987-6543" else "+1 (555) 234-5678") }
    var location by remember { mutableStateOf(if (isHost) "Los Angeles, CA" else "Berlin, Deutschland") }

    var currentPassword by remember { mutableStateOf("") }
    var newPassword by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }
    var showCurrentPw by remember { mutableStateOf(false) }
    var showNewPw by remember { mutableStateOf(false) }
    var showConfirmPw by remember { mutableStateOf(false) }

    Box(modifier = Modifier.fillMaxSize()) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    Brush.verticalGradient(
                        listOf(Color(0xFF4A8AFF), Color(0xFF7C3AED))
                    )
                )
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(Modifier.height(48.dp))

            Box(
                modifier = Modifier
                    .size(80.dp)
                    .clip(CircleShape)
                    .background(Color.White),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    Icons.Default.ConfirmationNumber,
                    contentDescription = null,
                    tint = Color(0xFF4A8AFF),
                    modifier = Modifier.size(44.dp)
                )
            }

            Spacer(Modifier.height(16.dp))

            Text(
                "Edit Profile",
                color = Color.White,
                fontSize = 28.sp,
                fontWeight = FontWeight.Bold
            )
            Spacer(Modifier.height(4.dp))
            Text(
                "Update your account information",
                color = Color.White.copy(alpha = 0.8f),
                fontSize = 14.sp
            )

            Spacer(Modifier.height(28.dp))

            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                shape = RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp, bottomStart = 24.dp, bottomEnd = 24.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                elevation = CardDefaults.cardElevation(0.dp)
            ) {
                Column(
                    modifier = Modifier.padding(horizontal = 20.dp, vertical = 20.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    Row(
                        modifier = Modifier
                            .clickable(
                                indication = null,
                                interactionSource = remember { MutableInteractionSource() }
                            ) { onBack() }
                            .padding(vertical = 4.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, null,
                            modifier = Modifier.size(18.dp),
                            tint = Color(0xFF111827))
                        Text("Back to Profile",
                            fontWeight = FontWeight.Medium,
                            fontSize = 15.sp,
                            color = Color(0xFF111827))
                    }

                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(50.dp))
                            .background(Color(0xFFF3F4F6))
                            .padding(4.dp)
                    ) {
                        Row(modifier = Modifier.fillMaxWidth()) {
                            listOf("Profile Info", "Password").forEachIndexed { index, label ->
                                Box(
                                    modifier = Modifier
                                        .weight(1f)
                                        .clip(RoundedCornerShape(50.dp))
                                        .background(
                                            if (selectedTab == index) Color.White
                                            else Color.Transparent
                                        )
                                        .clickable(
                                            indication = null,
                                            interactionSource = remember { MutableInteractionSource() }
                                        ) { selectedTab = index }
                                        .padding(vertical = 10.dp),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Text(
                                        label,
                                        fontWeight = if (selectedTab == index) FontWeight.SemiBold else FontWeight.Normal,
                                        fontSize = 14.sp,
                                        color = Color(0xFF111827)
                                    )
                                }
                            }
                        }
                    }

                    if (selectedTab == 0) {

                        Text("Account Type",
                            fontWeight = FontWeight.SemiBold,
                            fontSize = 14.sp,
                            color = Color(0xFF111827))

                        Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                            accountTypes.forEachIndexed { index, (name, desc, icon) ->
                                val isSelected = selectedAccountType == index
                                OutlinedCard(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .clickable(
                                            indication = null,
                                            interactionSource = remember { MutableInteractionSource() }
                                        ) { selectedAccountType = index },
                                    shape = RoundedCornerShape(12.dp),
                                    border = androidx.compose.foundation.BorderStroke(
                                        width = if (isSelected) 1.5.dp else 1.dp,
                                        color = if (isSelected) Color(0xFF4A8AFF)
                                                else Color(0xFFE5E7EB)
                                    ),
                                    colors = CardDefaults.outlinedCardColors(
                                        containerColor = Color.White
                                    )
                                ) {
                                    Row(
                                        modifier = Modifier.padding(14.dp),
                                        verticalAlignment = Alignment.CenterVertically,
                                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                                    ) {
                                        Box(
                                            modifier = Modifier.size(18.dp),
                                            contentAlignment = Alignment.Center
                                        ) {
                                            if (isSelected) {
                                                Box(
                                                    modifier = Modifier
                                                        .size(18.dp)
                                                        .clip(CircleShape)
                                                        .background(Color(0xFF111827))
                                                )
                                            }
                                        }
                                        Icon(icon, null,
                                            tint = Color(0xFF6B7280),
                                            modifier = Modifier.size(20.dp))
                                        Column {
                                            Text(name,
                                                fontWeight = FontWeight.SemiBold,
                                                fontSize = 14.sp,
                                                color = Color(0xFF111827))
                                            Text(desc,
                                                fontSize = 12.sp,
                                                color = Color(0xFF6B7280))
                                        }
                                    }
                                }
                            }
                        }

                        HorizontalDivider(color = Color(0xFFF3F4F6))

                        EditField(label = "Full Name *", value = fullName,
                            icon = Icons.Default.Person, onValueChange = { fullName = it })
                        EditField(label = "Email *", value = email,
                            icon = Icons.Default.Email, onValueChange = { email = it })
                        EditField(label = "Phone Number (Optional)", value = phone,
                            icon = Icons.Default.Phone, onValueChange = { phone = it })
                        EditField(label = "Location (Optional)", value = location,
                            icon = Icons.Default.LocationOn, onValueChange = { location = it })

                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(12.dp))
                                .background(Color(0xFF111827))
                                .clickable(
                                    indication = null,
                                    interactionSource = remember { MutableInteractionSource() }
                                ) { onBack() }
                                .padding(vertical = 16.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Text("Save Changes",
                                color = Color.White,
                                fontWeight = FontWeight.SemiBold,
                                fontSize = 15.sp)
                        }

                    } else {

                        Card(
                            shape = RoundedCornerShape(10.dp),
                            colors = CardDefaults.cardColors(containerColor = Color(0xFFEFF6FF))
                        ) {
                            Row(modifier = Modifier.padding(14.dp)) {
                                Text("Security: ",
                                    fontWeight = FontWeight.Bold,
                                    color = Color(0xFF1D4ED8),
                                    style = MaterialTheme.typography.bodyMedium)
                                Text("Choose a strong password with at least 8 characters.",
                                    color = Color(0xFF1D4ED8),
                                    style = MaterialTheme.typography.bodyMedium)
                            }
                        }

                        PasswordField(
                            label = "Current Password *",
                            placeholder = "Enter current password",
                            value = currentPassword,
                            showPassword = showCurrentPw,
                            onToggleShow = { showCurrentPw = !showCurrentPw },
                            onValueChange = { currentPassword = it }
                        )
                        PasswordField(
                            label = "New Password *",
                            placeholder = "Min. 8 characters",
                            value = newPassword,
                            showPassword = showNewPw,
                            onToggleShow = { showNewPw = !showNewPw },
                            onValueChange = { newPassword = it }
                        )
                        PasswordField(
                            label = "Confirm New Password *",
                            placeholder = "Re-enter new password",
                            value = confirmPassword,
                            showPassword = showConfirmPw,
                            onToggleShow = { showConfirmPw = !showConfirmPw },
                            onValueChange = { confirmPassword = it }
                        )

                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(12.dp))
                                .background(Color(0xFF111827))
                                .clickable(
                                    indication = null,
                                    interactionSource = remember { MutableInteractionSource() }
                                ) {}
                                .padding(vertical = 16.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Text("Change Password",
                                color = Color.White,
                                fontWeight = FontWeight.SemiBold,
                                fontSize = 15.sp)
                        }
                    }
                }
            }

            Spacer(Modifier.height(20.dp))

            Row(
                horizontalArrangement = Arrangement.spacedBy(4.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text("Need help? Contact",
                    color = Color.White.copy(alpha = 0.7f),
                    fontSize = 13.sp)
                Text("support@stagepot.com",
                    color = Color.White,
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 13.sp)
            }

            Spacer(Modifier.height(32.dp))
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun EditField(
    label: String,
    value: String,
    icon: ImageVector,
    onValueChange: (String) -> Unit
) {
    Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
        Text(label,
            fontSize = 13.sp,
            fontWeight = FontWeight.Medium,
            color = Color(0xFF374151))
        OutlinedTextField(
            value = value,
            onValueChange = onValueChange,
            modifier = Modifier.fillMaxWidth(),
            singleLine = true,
            leadingIcon = {
                Icon(icon, null,
                    tint = Color(0xFF9CA3AF),
                    modifier = Modifier.size(18.dp))
            },
            shape = RoundedCornerShape(10.dp),
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = Color(0xFF4A8AFF),
                unfocusedBorderColor = Color(0xFFE5E7EB),
                focusedContainerColor = Color(0xFFF9FAFB),
                unfocusedContainerColor = Color(0xFFF9FAFB)
            )
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun PasswordField(
    label: String,
    placeholder: String,
    value: String,
    showPassword: Boolean,
    onToggleShow: () -> Unit,
    onValueChange: (String) -> Unit
) {
    Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
        Text(label,
            fontSize = 13.sp,
            fontWeight = FontWeight.Medium,
            color = Color(0xFF374151))
        OutlinedTextField(
            value = value,
            onValueChange = onValueChange,
            modifier = Modifier.fillMaxWidth(),
            singleLine = true,
            placeholder = { Text(placeholder, color = Color(0xFF9CA3AF)) },
            leadingIcon = {
                Icon(Icons.Default.Lock, null,
                    tint = Color(0xFF9CA3AF),
                    modifier = Modifier.size(18.dp))
            },
            trailingIcon = {
                IconButton(onClick = onToggleShow) {
                    Icon(
                        if (showPassword) Icons.Default.VisibilityOff else Icons.Default.Visibility,
                        null,
                        tint = Color(0xFF9CA3AF),
                        modifier = Modifier.size(18.dp)
                    )
                }
            },
            visualTransformation = if (showPassword) VisualTransformation.None
                                   else PasswordVisualTransformation(),
            shape = RoundedCornerShape(10.dp),
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = Color(0xFF4A8AFF),
                unfocusedBorderColor = Color(0xFFE5E7EB),
                focusedContainerColor = Color(0xFFF9FAFB),
                unfocusedContainerColor = Color(0xFFF9FAFB)
            )
        )
    }
}
