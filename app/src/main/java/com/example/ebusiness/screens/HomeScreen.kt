package com.example.ebusiness.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ebusiness.data.Event
import com.example.ebusiness.data.MockData

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    paddingValues: PaddingValues,
    onEventClick: (Int) -> Unit,
    onNavigateToTickets: () -> Unit = {},
    onNavigateToProfile: () -> Unit = {},
    onNavigateToAlerts: () -> Unit = {},
    onNavigateToImprint: () -> Unit = {},
    isDarkMode: Boolean = false,
    onToggleDarkMode: () -> Unit = {}
) {
    var searchQuery by remember { mutableStateOf("") }
    var selectedCategory by remember { mutableStateOf("All") }
    var menuExpanded by remember { mutableStateOf(false) }

    val categories = listOf("All", "Music", "Sports", "Comedy", "Food", "Conference", "Theater")

    val filteredEvents = MockData.events.filter { event ->
        val matchesSearch = event.title.contains(searchQuery, ignoreCase = true) ||
                event.location.contains(searchQuery, ignoreCase = true)
        val matchesCategory = selectedCategory == "All" || event.category == selectedCategory
        matchesSearch && matchesCategory
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(bottom = paddingValues.calculateBottomPadding())
    ) {
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
                    Icon(Icons.Default.Paid, null,
                        tint = Color.White, modifier = Modifier.size(13.dp))
                    Text("\$50.00", color = Color.White, fontSize = 12.sp,
                        fontWeight = FontWeight.Bold)
                }
            }
            Spacer(Modifier.width(4.dp))
            BadgedBox(
                badge = { Badge { Text("2") } },
                modifier = Modifier.clickable(
                    indication = null,
                    interactionSource = remember { MutableInteractionSource() }
                ) { onNavigateToAlerts() }
            ) {
                Icon(
                    Icons.Default.Notifications, null,
                    tint = MaterialTheme.colorScheme.onSurface,
                    modifier = Modifier.size(24.dp)
                )
            }
            Spacer(Modifier.width(4.dp))
            Box {
                IconButton(onClick = { menuExpanded = true }) {
                    Icon(Icons.Default.Menu, "Menu",
                        tint = MaterialTheme.colorScheme.onSurface)
                }
                DropdownMenu(
                    expanded = menuExpanded,
                    onDismissRequest = { menuExpanded = false }
                ) {
                    DropdownMenuItem(
                        text = { Text("Events") },
                        leadingIcon = { Icon(Icons.Default.Home, null) },
                        onClick = { menuExpanded = false }
                    )
                    DropdownMenuItem(
                        text = { Text("My Tickets") },
                        leadingIcon = { Icon(Icons.Default.ConfirmationNumber, null) },
                        onClick = { menuExpanded = false; onNavigateToTickets() }
                    )
                    DropdownMenuItem(
                        text = { Text("Secondary Market") },
                        leadingIcon = { Icon(Icons.Default.Storefront, null) },
                        onClick = { menuExpanded = false }
                    )
                    HorizontalDivider()
                    DropdownMenuItem(
                        text = { Text("Profile") },
                        leadingIcon = { Icon(Icons.Default.Person, null) },
                        onClick = { menuExpanded = false; onNavigateToProfile() }
                    )
                    DropdownMenuItem(
                        text = { Text("Imprint") },
                        leadingIcon = { Icon(Icons.Default.Info, null) },
                        onClick = { menuExpanded = false; onNavigateToImprint() }
                    )
                    HorizontalDivider()
                    DropdownMenuItem(
                        text = { Text(if (isDarkMode) "Light Mode" else "Dark Mode") },
                        leadingIcon = {
                            Icon(
                                if (isDarkMode) Icons.Default.LightMode else Icons.Default.DarkMode,
                                null
                            )
                        },
                        onClick = { menuExpanded = false; onToggleDarkMode() }
                    )
                }
            }
        }

        StagePotBanner(
            title = "Discover Events",
            subtitle = "Find your next experience",
            searchQuery = searchQuery,
            onSearchChange = { searchQuery = it }
        )

        LazyColumn(
            modifier = Modifier.weight(1f).fillMaxWidth(),
            contentPadding = PaddingValues(bottom = 16.dp)
        ) {
            item {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .horizontalScroll(rememberScrollState())
                        .padding(horizontal = 16.dp, vertical = 12.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    categories.forEach { category ->
                        val selected = selectedCategory == category
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(50.dp))
                                .background(
                                    if (selected) Color(0xFF111827) else Color.Transparent
                                )
                                .border(
                                    width = 1.dp,
                                    color = if (selected) Color.Transparent
                                            else MaterialTheme.colorScheme.outline.copy(alpha = 0.4f),
                                    shape = RoundedCornerShape(50.dp)
                                )
                                .clickable(
                                    indication = null,
                                    interactionSource = remember { MutableInteractionSource() }
                                ) { selectedCategory = category }
                                .padding(horizontal = 14.dp, vertical = 7.dp)
                        ) {
                            Text(
                                category,
                                color = if (selected) Color.White
                                        else MaterialTheme.colorScheme.onSurface,
                                fontSize = 13.sp,
                                fontWeight = if (selected) FontWeight.SemiBold
                                             else FontWeight.Normal
                            )
                        }
                    }
                }
            }

            item {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .horizontalScroll(rememberScrollState())
                        .padding(horizontal = 16.dp),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .width(280.dp)
                            .height(76.dp)
                            .clip(RoundedCornerShape(16.dp))
                            .background(
                                Brush.horizontalGradient(
                                    listOf(Color(0xFFEC4899), Color(0xFFA855F7))
                                )
                            )
                            .clickable(
                                indication = null,
                                interactionSource = remember { MutableInteractionSource() }
                            ) {}
                            .padding(horizontal = 16.dp, vertical = 10.dp)
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(38.dp)
                                    .background(Color.White.copy(alpha = 0.2f), CircleShape),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(Icons.Default.TrendingUp, null,
                                    tint = Color.White, modifier = Modifier.size(20.dp))
                            }
                            Column {
                                Text("Secondary Market",
                                    color = Color.White, fontWeight = FontWeight.Bold,
                                    fontSize = 14.sp)
                                Text("Buy & sell verified tickets",
                                    color = Color.White.copy(alpha = 0.85f), fontSize = 12.sp)
                            }
                        }
                    }
                    Box(
                        modifier = Modifier
                            .width(280.dp)
                            .height(76.dp)
                            .clip(RoundedCornerShape(16.dp))
                            .background(
                                Brush.horizontalGradient(
                                    listOf(Color(0xFF4A8AFF), Color(0xFF6B60F0))
                                )
                            )
                            .padding(horizontal = 16.dp, vertical = 10.dp)
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(38.dp)
                                    .background(Color.White.copy(alpha = 0.2f), CircleShape),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(Icons.Default.Stars, null,
                                    tint = Color.White, modifier = Modifier.size(20.dp))
                            }
                            Column {
                                Text("Lottery Events",
                                    color = Color.White, fontWeight = FontWeight.Bold,
                                    fontSize = 14.sp)
                                Text("Fair access to sold-out shows",
                                    color = Color.White.copy(alpha = 0.85f), fontSize = 12.sp)
                            }
                        }
                    }
                }
                Spacer(Modifier.height(16.dp))
            }

            item {
                Text(
                    "Upcoming Events",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 4.dp)
                )
                Spacer(Modifier.height(4.dp))
            }

            if (filteredEvents.isEmpty()) {
                item {
                    Box(
                        modifier = Modifier.fillMaxWidth().padding(vertical = 48.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text("No events found",
                            style = MaterialTheme.typography.bodyLarge,
                            color = MaterialTheme.colorScheme.onSurfaceVariant)
                    }
                }
            } else {
                items(filteredEvents) { event ->
                    Box(modifier = Modifier.padding(horizontal = 16.dp, vertical = 6.dp)) {
                        EventCard(event = event, onClick = { onEventClick(event.id) })
                    }
                }
            }
        }
    }
}

@Composable
fun EventCard(event: Event, onClick: () -> Unit) {
    Card(
        modifier = Modifier.fillMaxWidth().clickable { onClick() },
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(180.dp)
                    .background(Color(event.imageColor))
            ) {
                if (event.hasLottery) {
                    Box(
                        modifier = Modifier
                            .align(Alignment.TopEnd)
                            .padding(10.dp)
                            .clip(RoundedCornerShape(8.dp))
                            .background(Brush.horizontalGradient(listOf(Color(0xFFFFB300), Color(0xFFFF6D00))))
                            .padding(horizontal = 8.dp, vertical = 4.dp)
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(4.dp)
                        ) {
                            Icon(Icons.Default.ConfirmationNumber, null,
                                tint = Color.White, modifier = Modifier.size(12.dp))
                            Text("Lottery", color = Color.White, fontSize = 11.sp,
                                fontWeight = FontWeight.SemiBold)
                        }
                    }
                }
            }

            Column(
                modifier = Modifier.padding(14.dp),
                verticalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.Top
                ) {
                    Text(
                        event.title,
                        fontWeight = FontWeight.Bold,
                        fontSize = 17.sp,
                        modifier = Modifier.weight(1f, fill = false),
                        lineHeight = 22.sp
                    )
                    Spacer(Modifier.width(8.dp))
                    Text(
                        "$${event.price}",
                        color = Color(0xFF4A8AFF),
                        fontWeight = FontWeight.Bold,
                        fontSize = 17.sp
                    )
                }

                Surface(
                    shape = RoundedCornerShape(6.dp),
                    color = MaterialTheme.colorScheme.surfaceVariant
                ) {
                    Text(
                        event.category,
                        fontSize = 11.sp,
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp),
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(5.dp)
                ) {
                    Icon(Icons.Default.CalendarToday, null,
                        modifier = Modifier.size(13.dp),
                        tint = MaterialTheme.colorScheme.onSurfaceVariant)
                    Text(event.date, fontSize = 12.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant)
                }

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(5.dp)
                ) {
                    Icon(Icons.Default.LocationOn, null,
                        modifier = Modifier.size(13.dp),
                        tint = MaterialTheme.colorScheme.onSurfaceVariant)
                    Text(event.location, fontSize = 12.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant)
                }

                HorizontalDivider(modifier = Modifier.padding(vertical = 2.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        Text("by ${event.organizer}", fontSize = 11.sp,
                            color = MaterialTheme.colorScheme.onSurfaceVariant)
                        Icon(Icons.Default.Verified, null,
                            modifier = Modifier.size(13.dp),
                            tint = Color(0xFF4A8AFF))
                    }
                    Text("${event.ticketsLeft} left", fontSize = 11.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant)
                }
            }
        }
    }
}
