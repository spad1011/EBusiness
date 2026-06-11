package com.example.ebusiness

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.PanoramaVertical
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            TicketAndEventsApp()
        }
    }
}

@Composable
fun TicketAndEventsApp() {
    var selectedPage by remember { mutableIntStateOf(0) }

    Scaffold(
        bottomBar = {
            NavigationBar {
                NavigationBarItem(
                    selected = selectedPage == 0,
                    onClick = { selectedPage = 0 },
                    icon = {
                        Icon(
                            imageVector = Icons.Default.Home,
                            contentDescription = "Events"
                        )
                    },
                    label = { Text("Home") }
                )

                NavigationBarItem(
                    selected = selectedPage == 1,
                    onClick = { selectedPage = 1 },
                    icon = {
                        Icon(
                            imageVector = Icons.Default.PanoramaVertical,
                            contentDescription = "Tickets"
                        )
                    },
                    label = { Text("Tickets") }
                )
            }
        }
    ) { paddingValues ->

        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues),
            contentAlignment = Alignment.Center
        ) {
            when (selectedPage) {
                0 -> HomeScreen()
                1 -> Tickets()
            }
        }
    }
}

@Composable
fun HomeScreen() {
    Text(
        text = "Hier kommen Events hin",
        style = MaterialTheme.typography.headlineMedium
    )
}

@Composable
fun Tickets() {
    Text(
        text = "Hier kommen die eigenen Tickets hin",
        style = MaterialTheme.typography.headlineMedium
    )
}