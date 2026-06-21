package com.example.ebusiness.screens

// Wiederverwendbare UI-Komponenten die in mehreren Screens vorkommen:
// StagePotBrandBar (weiße Kopfzeile) und StagePotBanner (blau-violetter Hero-Bereich).

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ConfirmationNumber
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

/**
 * White StagePot brand bar: purple circle logo + name/subtitle + trailing actions.
 * Place at the very top of each main screen (handles status bar inset).
 */
@Composable
fun StagePotBrandBar(
    navigationIcon: @Composable (() -> Unit)? = null,
    // false when the parent Scaffold already applies top inset (avoids double gap)
    applyStatusBarPadding: Boolean = true,
    actions: @Composable () -> Unit = {}
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(MaterialTheme.colorScheme.surface)
            .then(if (applyStatusBarPadding) Modifier.statusBarsPadding() else Modifier)
            .padding(horizontal = 16.dp, vertical = 10.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Optional back / navigation icon on the far left
            if (navigationIcon != null) {
                navigationIcon()
            }
            Surface(
                shape = CircleShape,
                color = MaterialTheme.colorScheme.primary,
                modifier = Modifier.size(38.dp)
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Icon(
                        Icons.Default.ConfirmationNumber,
                        contentDescription = null,
                        tint = Color.White,
                        modifier = Modifier.size(20.dp)
                    )
                }
            }
            Spacer(Modifier.width(8.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    "StagePot",
                    fontWeight = FontWeight.Bold,
                    fontSize = 15.sp,
                    color = MaterialTheme.colorScheme.onSurface
                )
                Text(
                    "Fair Tickets for Real Fans",
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
            // Caller-supplied action icons (credits chip, bell, menu, etc.)
            actions()
        }
    }
}

/**
 * Blue–violet gradient banner with rounded bottom corners.
 * Displays a title, subtitle, and optionally a white search bar.
 * Pass [onSearchChange] = null to hide the search bar.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StagePotBanner(
    title: String,
    subtitle: String,
    searchQuery: String = "",
    onSearchChange: ((String) -> Unit)? = null
) {
    val gradient = Brush.verticalGradient(
        colors = listOf(Color(0xFF4A8AFF), Color(0xFF6B60F0))
    )

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(bottomStart = 28.dp, bottomEnd = 28.dp))
            .background(gradient)
            .padding(start = 20.dp, end = 20.dp, top = 18.dp, bottom = 26.dp)
    ) {
        Column {
            Text(
                title,
                color = Color.White,
                fontSize = 22.sp,
                fontWeight = FontWeight.Bold
            )
            Spacer(Modifier.height(2.dp))
            Text(
                subtitle,
                color = Color.White.copy(alpha = 0.8f),
                fontSize = 13.sp
            )
            if (onSearchChange != null) {
                Spacer(Modifier.height(14.dp))
                TextField(
                    value = searchQuery,
                    onValueChange = onSearchChange,
                    placeholder = {
                        Text("Search events or city...", color = Color.Gray)
                    },
                    leadingIcon = {
                        Icon(Icons.Default.Search, contentDescription = null, tint = Color.Gray)
                    },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(14.dp),
                    singleLine = true,
                    colors = TextFieldDefaults.colors(
                        focusedContainerColor = MaterialTheme.colorScheme.surface,
                        unfocusedContainerColor = MaterialTheme.colorScheme.surface,
                        focusedIndicatorColor = Color.Transparent,
                        unfocusedIndicatorColor = Color.Transparent,
                        disabledIndicatorColor = Color.Transparent,
                        focusedTextColor = MaterialTheme.colorScheme.onSurface,
                        unfocusedTextColor = MaterialTheme.colorScheme.onSurface,
                        cursorColor = MaterialTheme.colorScheme.onSurface
                    )
                )
            }
        }
    }
}
