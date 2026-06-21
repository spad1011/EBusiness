package com.example.ebusiness.screens

// Hilfe-Center: Kontaktoptionen und ausklappbare FAQ-Liste.
// Rein statischer Screen — keine Daten aus der DB.

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.launch

/**
 * Help Center screen — shows FAQ, contact options, and general support info.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HelpCenterScreen(onBack: () -> Unit) {

    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()
    fun snack(msg: String) { scope.launch { snackbarHostState.showSnackbar(msg) } }
    var expandedFaq by remember { mutableStateOf<Int?>(null) }

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) },
        topBar = {
                TopAppBar(
                title = {
                    Column {
                        Text("Help Center", fontWeight = FontWeight.Bold)
                        Text(
                            "Frequently asked questions & support",
                            style = MaterialTheme.typography.labelSmall,
                            color = MaterialTheme.colorScheme.onPrimary.copy(alpha = 0.8f)
                        )
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
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .verticalScroll(rememberScrollState())
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {

            Text("Contact Us", fontWeight = FontWeight.Bold,
                style = MaterialTheme.typography.titleSmall,
                modifier = Modifier.padding(horizontal = 4.dp))

            Card(
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                elevation = CardDefaults.cardElevation(2.dp)
            ) {
                Column(modifier = Modifier.fillMaxWidth()) {
                    HelpContactRow(
                        icon = Icons.Default.Email,
                        title = "Email Support",
                        subtitle = "support@stagepot.com",
                        onClick = { snack("Email: support@stagepot.com") }
                    )
                    HorizontalDivider(modifier = Modifier.padding(horizontal = 16.dp))
                    HelpContactRow(
                        icon = Icons.Default.Phone,
                        title = "Phone Support",
                        subtitle = "Mon–Fri, 9:00 AM–6:00 PM",
                        onClick = { snack("Phone: +1 (555) 123-4567") }
                    )
                    HorizontalDivider(modifier = Modifier.padding(horizontal = 16.dp))
                    HelpContactRow(
                        icon = Icons.Default.Language,
                        title = "Website",
                        subtitle = "www.stagepot.com",
                        onClick = { snack("Website: www.stagepot.com") }
                    )
                }
            }

            Text("FAQ", fontWeight = FontWeight.Bold,
                style = MaterialTheme.typography.titleSmall,
                modifier = Modifier.padding(horizontal = 4.dp))

            val faqs = listOf(
                "How does the lottery work?" to
                    "When tickets for an event are sold out, you can enter the lottery. " +
                    "A random draw selects winners who can then purchase a ticket at the fair price.",
                "How do I buy a ticket?" to
                    "Browse events on the Home screen, tap an event, and press 'Buy Ticket'. " +
                    "You can choose your seat and section before confirming the purchase.",
                "What are Credits?" to
                    "Credits are your in-app balance used for lottery entries. " +
                    "You can add credits via Payment Methods. Unused credits are refunded if you lose a lottery.",
                "Can I transfer my ticket?" to
                    "Ticket transfers are currently not supported. " +
                    "Your ticket is personal and linked to your account.",
                "How do I cancel a ticket?" to
                    "You can request a refund from the Ticket Detail screen up to 48 hours before the event."
            )

            Card(
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                elevation = CardDefaults.cardElevation(2.dp)
            ) {
                Column(modifier = Modifier.fillMaxWidth()) {
                    faqs.forEachIndexed { index, (question, answer) ->
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable {
                                    expandedFaq = if (expandedFaq == index) null else index
                                }
                                .padding(horizontal = 16.dp, vertical = 14.dp)
                        ) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    question,
                                    style = MaterialTheme.typography.bodyMedium,
                                    fontWeight = FontWeight.Medium,
                                    modifier = Modifier.weight(1f)
                                )
                                Icon(
                                    if (expandedFaq == index) Icons.Default.ExpandLess
                                    else Icons.Default.ExpandMore,
                                    contentDescription = null,
                                    tint = MaterialTheme.colorScheme.onSurfaceVariant,
                                    modifier = Modifier.size(20.dp)
                                )
                            }
                            if (expandedFaq == index) {
                                Spacer(Modifier.height(8.dp))
                                Text(
                                    answer,
                                    style = MaterialTheme.typography.bodySmall,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }
                        }
                        if (index < faqs.lastIndex) {
                            HorizontalDivider(modifier = Modifier.padding(horizontal = 16.dp))
                        }
                    }
                }
            }

            Spacer(Modifier.height(8.dp))
            Text(
                "Still need help? Write us at support@stagepot.com",
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.align(Alignment.CenterHorizontally)
            )
            Spacer(Modifier.height(8.dp))
        }
    }
}

/**
 * A single contact row with icon, title, and subtitle.
 */
@Composable
private fun HelpContactRow(
    icon: ImageVector,
    title: String,
    subtitle: String,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .padding(horizontal = 16.dp, vertical = 14.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Icon(icon, null,
            tint = MaterialTheme.colorScheme.primary,
            modifier = Modifier.size(20.dp))
        Column(modifier = Modifier.weight(1f)) {
            Text(title, style = MaterialTheme.typography.bodyMedium, fontWeight = FontWeight.Medium)
            Text(subtitle, style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant)
        }
        Icon(Icons.AutoMirrored.Filled.ArrowForward, null,
            tint = MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = Modifier.size(16.dp))
    }
}
