package com.example.ebusiness.screens

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Download
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.ebusiness.data.Ticket

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TicketDetailScreen(
    ticketId: Int,
    tickets: List<Ticket>,
    onBack: () -> Unit
) {
    val ticket = tickets.find { it.id == ticketId } ?: return

    Column(modifier = Modifier.fillMaxSize()) {

        TopAppBar(
            title = { Text("Ticket Details") },
            navigationIcon = {
                IconButton(onClick = onBack) {
                    Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Zurück")
                }
            },
            actions = {
                IconButton(onClick = { /* Share-Logik */ }) {
                    Icon(Icons.Default.Share, contentDescription = "Teilen")
                }
                IconButton(onClick = { /* Download-Logik */ }) {
                    Icon(Icons.Default.Download, contentDescription = "Download")
                }
            }
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {

            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(20.dp),
                elevation = CardDefaults.cardElevation(8.dp),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer
                )
            ) {
                Column(
                    modifier = Modifier.padding(24.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Text(
                        ticket.eventTitle,
                        style = MaterialTheme.typography.headlineSmall,
                        fontWeight = FontWeight.Bold,
                        textAlign = TextAlign.Center
                    )

                    HorizontalDivider(modifier = Modifier.padding(vertical = 2.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceEvenly
                    ) {
                        TicketInfoColumn("DATUM", ticket.eventDate)
                        TicketInfoColumn("ORT", ticket.eventLocation)
                        TicketInfoColumn("PLATZ", ticket.seat.take(12))
                    }

                    HorizontalDivider(modifier = Modifier.padding(vertical = 2.dp))

                    Text(
                        "Zeige diesen Code beim Einlass",
                        style = MaterialTheme.typography.labelMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )

                    Box(
                        modifier = Modifier
                            .size(200.dp)
                            .background(Color.White, RoundedCornerShape(12.dp))
                            .padding(12.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        SimulatedQrCode(qrData = ticket.qrCode)
                    }

                    Text(
                        ticket.qrCode,
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        textAlign = TextAlign.Center
                    )
                }
            }

            Surface(
                color = Color(0xFF2E7D32),
                shape = RoundedCornerShape(10.dp)
            ) {
                Text(
                    "✓ Ticket gültig",
                    modifier = Modifier.padding(horizontal = 24.dp, vertical = 10.dp),
                    color = Color.White,
                    fontWeight = FontWeight.Bold,
                    style = MaterialTheme.typography.titleSmall
                )
            }
        }
    }
}

@Composable
private fun TicketInfoColumn(label: String, value: String) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(
            label,
            style = MaterialTheme.typography.labelSmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        Spacer(Modifier.height(2.dp))
        Text(
            value,
            style = MaterialTheme.typography.bodySmall,
            fontWeight = FontWeight.Medium,
            textAlign = TextAlign.Center
        )
    }
}

/**
 * Simulierter QR-Code basierend auf dem qrData-String.
 * Echte QR-Generierung würde zxing-android-embedded brauchen —
 * das hier sieht aus wie ein QR-Code und reicht für die Demo.
 */
@Composable
fun SimulatedQrCode(qrData: String) {
    val cells = 21
    val seed = qrData.hashCode()
    val random = java.util.Random(seed.toLong())

    val matrix = Array(cells) { row ->
        BooleanArray(cells) { col ->
            when {
                isCornerBlock(row, col, cells) -> isInCornerPattern(row % 7, col % 7)
                else -> random.nextBoolean()
            }
        }
    }

    Canvas(modifier = Modifier.fillMaxSize()) {
        val cellSize = size.width / cells

        matrix.forEachIndexed { row, cols ->
            cols.forEachIndexed { col, filled ->
                if (filled) {
                    drawRect(
                        color = Color.Black,
                        topLeft = Offset(col * cellSize, row * cellSize),
                        size = Size(cellSize, cellSize)
                    )
                }
            }
        }
    }
}

private fun isCornerBlock(row: Int, col: Int, size: Int): Boolean {
    return (row < 7 && col < 7) ||
            (row < 7 && col >= size - 7) ||
            (row >= size - 7 && col < 7)
}

private fun isInCornerPattern(row: Int, col: Int): Boolean {
    if (row == 0 || row == 6 || col == 0 || col == 6) return true
    if (row in 2..4 && col in 2..4) return true
    return false
}
