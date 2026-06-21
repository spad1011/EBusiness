package com.example.ebusiness.screens

// Platzhalter-Screen für den Zweitmarkt (Ticket-Weiterverkauf).
// Funktion noch nicht implementiert — zeigt Feature-Preview mit "Coming Soon".

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

/**
 * Zweitmarkt-Screen — aktuell nur als Vorschau (Coming Soon).
 * Zeigt geplante Features: verifizierte Verkäufer, Käuferschutz, faire Preise.
 */
@Composable
fun SecondaryMarketScreen(onBack: () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
    ) {
        // Gradient banner with back arrow and title inside
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(bottomStart = 28.dp, bottomEnd = 28.dp))
                .background(
                    Brush.verticalGradient(
                        listOf(Color(0xFFEC4899), Color(0xFFA855F7))
                    )
                )
                .statusBarsPadding()
                .padding(start = 4.dp, end = 20.dp, top = 4.dp, bottom = 26.dp)
        ) {
            Column {
                IconButton(onClick = onBack) {
                    Icon(
                        Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = "Back",
                        tint = Color.White
                    )
                }
                Spacer(Modifier.height(4.dp))
                Text(
                    "Secondary Market",
                    color = Color.White,
                    fontSize = 22.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(start = 16.dp)
                )
                Spacer(Modifier.height(2.dp))
                Text(
                    "Buy & sell verified tickets",
                    color = Color.White.copy(alpha = 0.8f),
                    fontSize = 13.sp,
                    modifier = Modifier.padding(start = 16.dp)
                )
            }
        }

        // Body content
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp, vertical = 32.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Large icon circle
            Surface(
                shape = CircleShape,
                color = Color(0xFFEC4899).copy(alpha = 0.12f),
                modifier = Modifier.size(120.dp)
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Icon(
                        Icons.Default.TrendingUp,
                        contentDescription = null,
                        tint = Color(0xFFEC4899),
                        modifier = Modifier.size(56.dp)
                    )
                }
            }

            Spacer(Modifier.height(24.dp))

            Text(
                "Coming Soon",
                fontSize = 28.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurface
            )

            Spacer(Modifier.height(10.dp))

            Text(
                "The Secondary Market is currently under development. Soon you'll be able to buy and sell verified tickets safely — at fair prices, without bots.",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                textAlign = TextAlign.Center,
                lineHeight = 22.sp
            )

            Spacer(Modifier.height(32.dp))

            // Feature preview rows
            listOf(
                Icons.Default.Verified   to "Verified Sellers",
                Icons.Default.Shield     to "Buyer Protection",
                Icons.Default.PriceCheck to "Fair Pricing",
                Icons.Default.SwapHoriz  to "Instant Transfer"
            ).forEach { (icon, label) ->
                FeatureRow(icon = icon, label = label)
                Spacer(Modifier.height(10.dp))
            }

            Spacer(Modifier.height(24.dp))

            Button(
                onClick = onBack,
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFFEC4899)
                ),
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    "Back to Events",
                    fontWeight = FontWeight.SemiBold,
                    modifier = Modifier.padding(vertical = 4.dp)
                )
            }
        }
    }
}

/** Einzelne Feature-Zeile mit Icon-Badge und Label für die Coming-Soon-Vorschau */
@Composable
private fun FeatureRow(icon: ImageVector, label: String) {
    Surface(
        shape = RoundedCornerShape(14.dp),
        color = MaterialTheme.colorScheme.surfaceVariant,
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 14.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            Box(
                modifier = Modifier
                    .size(36.dp)
                    .background(Color(0xFFEC4899).copy(alpha = 0.12f), CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    icon, null,
                    tint = Color(0xFFEC4899),
                    modifier = Modifier.size(18.dp)
                )
            }
            Text(
                label,
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = FontWeight.Medium
            )
        }
    }
}
