package com.example.ebusiness.screens

// Impressum-Screen mit Firmendaten, Kontaktinfos und rechtlichen Hinweisen.
// Rein statischer Screen — keine Daten aus der DB.

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
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

/**
 * Impressum-Screen — gesetzlich vorgeschriebene Angaben über StagePot Inc.
 * Aufgeteilt in Sektionen via ImprintSection-Composable.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ImprintScreen(onBack: () -> Unit) {
    Column(modifier = Modifier.fillMaxSize()) {
        TopAppBar(
            title = {
                Column {
                    Text("Imprint", fontWeight = FontWeight.Bold)
                    Text(
                        "Legal information and company details",
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

        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            ImprintSection(
                icon = Icons.Default.Business,
                title = "Company Information"
            ) {
                Text("StagePot Inc.", fontWeight = FontWeight.SemiBold)
                Text(
                    "Fair Tickets for Real Fans",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }

            HorizontalDivider()

            ImprintSection(
                icon = Icons.Default.LocationOn,
                title = "Registered Office"
            ) {
                Text("123 Event Street")
                Text("San Francisco, CA 94102")
                Text("United States")
            }

            HorizontalDivider()

            ImprintSection(
                icon = Icons.Default.ContactMail,
                title = "Contact Information"
            ) {
                ImprintContactRow(Icons.Default.Email, "Email", "info@stagepot.com")
                Spacer(Modifier.height(8.dp))
                ImprintContactRow(Icons.Default.Phone, "Phone", "+1 (555) 123-4567")
                Spacer(Modifier.height(8.dp))
                ImprintContactRow(Icons.Default.Language, "Website", "www.stagepot.com")
            }

            HorizontalDivider()

            ImprintSection(
                icon = Icons.Default.People,
                title = "Managing Directors"
            ) {
                Text("Jane Smith, CEO")
                Text("John Doe, CTO")
            }

            HorizontalDivider()

            ImprintSection(
                icon = Icons.Default.Gavel,
                title = "Legal"
            ) {
                Text(
                    "StagePot Inc. is registered in the State of California, USA. " +
                    "All ticket transactions are subject to our Terms of Service and Privacy Policy.",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }

            HorizontalDivider()

            ImprintSection(
                icon = Icons.Default.Info,
                title = "Disclaimer"
            ) {
                Text(
                    "Despite careful content control, we assume no liability for the content of " +
                    "external links. The operators of the linked pages are solely responsible for their content.",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }

            Spacer(Modifier.height(16.dp))
            Text(
                "© 2026 StagePot Inc. All rights reserved.",
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.align(Alignment.CenterHorizontally)
            )
            Spacer(Modifier.height(8.dp))
        }
    }
}

/** Eine Impressums-Sektion mit Icon, Titel und beliebigem Inhalt darunter */
@Composable
private fun ImprintSection(
    icon: ImageVector,
    title: String,
    content: @Composable ColumnScope.() -> Unit
) {
    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Icon(icon, null,
                tint = MaterialTheme.colorScheme.primary,
                modifier = Modifier.size(18.dp))
            Text(title, fontWeight = FontWeight.Bold,
                style = MaterialTheme.typography.titleSmall)
        }
        Column(
            modifier = Modifier.padding(start = 26.dp),
            verticalArrangement = Arrangement.spacedBy(2.dp),
            content = content
        )
    }
}

/** Einzelne Kontaktzeile mit kleinem Icon, Label und klickbarem Wert */
@Composable
private fun ImprintContactRow(icon: ImageVector, label: String, value: String) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Icon(icon, null,
            tint = MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = Modifier.size(14.dp))
        Column {
            Text(label,
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant)
            Text(value,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.primary,
                fontWeight = FontWeight.Medium)
        }
    }
}
