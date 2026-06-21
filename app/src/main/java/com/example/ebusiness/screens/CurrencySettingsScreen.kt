package com.example.ebusiness.screens

// Währungsauswahl: EUR, USD, GBP, AUD.
// Die Wahl wird im ViewModel gespeichert und in UserEntity.currency persistiert.

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.CurrencyExchange
import androidx.compose.material.icons.filled.EuroSymbol
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Wallet
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
import com.example.ebusiness.data.currencySymbol
import com.example.ebusiness.data.formatPrice

/** Hilfsklasse für die Währungsauswahl-Liste im Screen */
data class Currency(
    val code: String,   // "EUR", "USD", "GBP", "AUD"
    val name: String,
    val symbol: String,
    val icon: ImageVector
)

/**
 * Währungseinstellungen — User wählt seine bevorzugte Anzeigewährung.
 * Beim Speichern wird onSave aufgerufen und die Änderung in die DB geschrieben.
 */
@Composable
fun CurrencySettingsScreen(
    onBack: () -> Unit,
    onNavigateToAlerts: () -> Unit = {},
    credits: Double = 0.0,
    currency: String = "EUR",
    onSave: (String) -> Unit = {},
    unreadCount: Int = 0
) {
    val currencies = listOf(
        Currency("EUR", "Euro",           "€",   Icons.Default.EuroSymbol),
        Currency("USD", "US Dollar",      "$",   Icons.Default.CurrencyExchange),
        Currency("GBP", "British Pound",  "£",   Icons.Default.CurrencyExchange),
        Currency("AUD", "Australian Dollar", "A$", Icons.Default.CurrencyExchange),
    )

    // Auswahl aus DB-Wert initialisieren — kein Hardcoding
    var selectedCode by remember(currency) { mutableStateOf(currency) }

    Column(modifier = Modifier.fillMaxSize()) {

        StagePotBrandBar {
            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(20.dp))
                    .background(Brush.horizontalGradient(listOf(Color(0xFFFFB300), Color(0xFFFF6D00))))
                    .padding(horizontal = 10.dp, vertical = 5.dp)
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    Icon(Icons.Default.Wallet, null,
                        tint = Color.White, modifier = Modifier.size(13.dp))
                    Text(
                        formatPrice(credits, currency),
                        color = Color.White, fontSize = 12.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
            Spacer(Modifier.width(4.dp))
            IconButton(onClick = {}) {
                Icon(Icons.Default.Menu, null,
                    tint = MaterialTheme.colorScheme.onSurface)
            }
        }

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(
                    Brush.verticalGradient(listOf(Color(0xFF4A8AFF), Color(0xFF6B60F0)))
                )
                .padding(start = 8.dp, end = 20.dp, top = 12.dp, bottom = 24.dp)
        ) {
            IconButton(onClick = onBack) {
                Icon(Icons.AutoMirrored.Filled.ArrowBack, null, tint = Color.White)
            }
            Column(
                modifier = Modifier.padding(start = 20.dp, top = 48.dp)
            ) {
                Text("Currency Settings",
                    color = Color.White, fontSize = 26.sp, fontWeight = FontWeight.Bold)
                Spacer(Modifier.height(4.dp))
                Text("Choose your preferred currency",
                    color = Color.White.copy(alpha = 0.85f), fontSize = 14.sp)
            }
        }

        Column(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Spacer(Modifier.height(4.dp))

            Card(
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                elevation = CardDefaults.cardElevation(2.dp)
            ) {
                Column(modifier = Modifier.fillMaxWidth()) {
                    currencies.forEachIndexed { index, cur ->
                        val isSelected = selectedCode == cur.code
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable(
                                    indication = null,
                                    interactionSource = remember { MutableInteractionSource() }
                                ) { selectedCode = cur.code }
                                .padding(horizontal = 16.dp, vertical = 18.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(14.dp)
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(20.dp)
                                    .clip(CircleShape)
                                    .border(
                                        width = if (isSelected) 6.dp else 2.dp,
                                        color = if (isSelected) Color(0xFF4A8AFF)
                                                else MaterialTheme.colorScheme.outline.copy(alpha = 0.4f),
                                        shape = CircleShape
                                    )
                            )

                            Icon(
                                cur.icon, null,
                                tint = MaterialTheme.colorScheme.onSurfaceVariant,
                                modifier = Modifier.size(22.dp)
                            )

                            Column(modifier = Modifier.weight(1f)) {
                                Text(cur.name,
                                    fontWeight = FontWeight.SemiBold,
                                    style = MaterialTheme.typography.bodyLarge)
                                Text(cur.symbol,
                                    style = MaterialTheme.typography.bodySmall,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant)
                            }

                            if (isSelected) {
                                Text(
                                    "Selected",
                                    color = Color(0xFF4A8AFF),
                                    fontSize = 14.sp,
                                    fontWeight = FontWeight.SemiBold
                                )
                            }
                        }

                        if (index < currencies.lastIndex) {
                            HorizontalDivider(modifier = Modifier.padding(horizontal = 16.dp))
                        }
                    }
                }
            }

            Card(
                shape = RoundedCornerShape(12.dp),
                colors = CardDefaults.cardColors(containerColor = Color(0xFFEFF6FF))
            ) {
                Row(
                    modifier = Modifier.padding(16.dp),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    Text(
                        "Note: ",
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF1D4ED8),
                        style = MaterialTheme.typography.bodyMedium
                    )
                    Text(
                        "All prices and credits will be displayed in your selected currency.",
                        color = Color(0xFF1D4ED8),
                        style = MaterialTheme.typography.bodyMedium
                    )
                }
            }

            Spacer(Modifier.weight(1f))

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(12.dp))
                    .background(Color(0xFF111827))
                    .clickable(
                        indication = null,
                        interactionSource = remember { MutableInteractionSource() }
                    ) {
                        onSave(selectedCode)
                        onBack()
                    }
                    .padding(vertical = 18.dp),
                contentAlignment = Alignment.Center
            ) {
                Text("Done",
                    color = Color.White,
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 16.sp)
            }

            Spacer(Modifier.height(8.dp))
        }
    }
}
