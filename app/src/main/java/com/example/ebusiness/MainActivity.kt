package com.example.ebusiness

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ConfirmationNumber
import androidx.compose.material.icons.filled.Event
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.*
import androidx.compose.runtime.*
import com.example.ebusiness.data.MockData
import com.example.ebusiness.data.Ticket
import com.example.ebusiness.screens.*
import com.example.ebusiness.ui.theme.EBusinessTheme

sealed class Screen {
    object Login : Screen()
    object CreateAccount : Screen()
    object Home : Screen()
    object Tickets : Screen()
    object Alerts : Screen()
    object Organize : Screen()
    object Profile : Screen()
    object PaymentMethods : Screen()
    object CurrencySettings : Screen()
    object EditProfile : Screen()
    object Imprint : Screen()
    data class EventDetail(val eventId: Int) : Screen()
    data class BuyTicket(val eventId: Int) : Screen()
    data class Lottery(val eventId: Int) : Screen()
    data class TicketDetail(val ticketId: Int) : Screen()
}

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            StagePotApp()
        }
    }
}

@Composable
fun StagePotApp() {
    val systemDark = isSystemInDarkTheme()
    var darkMode by remember { mutableStateOf(systemDark) }

    EBusinessTheme(darkTheme = darkMode) {
        val tickets = remember { mutableStateListOf<Ticket>().apply { addAll(MockData.initialTickets) } }
        var currentScreen by remember { mutableStateOf<Screen>(Screen.Login) }
        var userType by remember { mutableStateOf("fan") }  // "fan" oder "host"
        val alertCount = 1

        val bottomScreens = buildSet {
            add(Screen.Home); add(Screen.Tickets); add(Screen.Alerts); add(Screen.Profile)
            if (userType == "host") add(Screen.Organize)
        }
        val showBottomBar = bottomScreens.any { it::class == currentScreen::class }

        Scaffold(
            bottomBar = {
                          if (showBottomBar) {
                    NavigationBar {
                        NavigationBarItem(
                            selected = currentScreen is Screen.Home,
                            onClick = { currentScreen = Screen.Home },
                            icon = { Icon(Icons.Default.Home, null) },
                            label = { Text("Home") }
                        )
                        NavigationBarItem(
                            selected = currentScreen is Screen.Tickets,
                            onClick = { currentScreen = Screen.Tickets },
                            icon = {
                                BadgedBox(badge = {
                                    if (tickets.isNotEmpty()) Badge { Text("${tickets.size}") }
                                }) { Icon(Icons.Default.ConfirmationNumber, null) }
                            },
                            label = { Text("Tickets") }
                        )
                        NavigationBarItem(
                            selected = currentScreen is Screen.Alerts,
                            onClick = { currentScreen = Screen.Alerts },
                            icon = {
                                BadgedBox(badge = {
                                    if (alertCount > 0) Badge { Text("$alertCount") }
                                }) { Icon(Icons.Default.Notifications, null) }
                            },
                            label = { Text("Alerts") }
                        )
                        if (userType == "host") {
                            NavigationBarItem(
                                selected = currentScreen is Screen.Organize,
                                onClick = { currentScreen = Screen.Organize },
                                icon = { Icon(Icons.Default.Event, null) },
                                label = { Text("Organize") }
                            )
                        }
                        NavigationBarItem(
                            selected = currentScreen is Screen.Profile,
                            onClick = { currentScreen = Screen.Profile },
                            icon = { Icon(Icons.Default.Person, null) },
                            label = { Text("Profile") }
                        )
                    }
                }
            }
        ) { padding ->
            val screen = currentScreen
            when (screen) {
                is Screen.Login -> LoginScreen(
                    onLoginAsFan = { userType = "fan"; currentScreen = Screen.Home },
                    onLoginAsHost = { userType = "host"; currentScreen = Screen.Home },
                    onCreateAccount = { currentScreen = Screen.CreateAccount },
                    onGuestMode = { userType = "fan"; currentScreen = Screen.Home }
                )
                is Screen.CreateAccount -> CreateAccountScreen(
                    onBack = { currentScreen = Screen.Login },
                    onSuccess = { currentScreen = Screen.Home }
                )
                is Screen.Home -> HomeScreen(
                    paddingValues = padding,
                    onEventClick = { eventId -> currentScreen = Screen.EventDetail(eventId) },
                    onNavigateToTickets = { currentScreen = Screen.Tickets },
                    onNavigateToProfile = { currentScreen = Screen.Profile },
                    onNavigateToAlerts = { currentScreen = Screen.Alerts },
                    onNavigateToImprint = { currentScreen = Screen.Imprint },
                    isDarkMode = darkMode,
                    onToggleDarkMode = { darkMode = !darkMode }
                )
                is Screen.Tickets -> TicketsScreen(
                    paddingValues = padding,
                    tickets = tickets,
                    onTicketClick = { ticketId -> currentScreen = Screen.TicketDetail(ticketId) }
                )
                is Screen.Alerts -> AlertsScreen(paddingValues = padding)
                is Screen.Organize -> OrganizeScreen(paddingValues = padding, userType = userType)
                is Screen.Profile -> ProfileScreen(
                    paddingValues = padding,
                    isDarkMode = darkMode,
                    onToggleDarkMode = { darkMode = !darkMode },
                    onLogout = { currentScreen = Screen.Login },
                    onPaymentMethods = { currentScreen = Screen.PaymentMethods },
                    onNavigateToAlerts = { currentScreen = Screen.Alerts },
                    onCurrencySettings = { currentScreen = Screen.CurrencySettings },
                    onEditProfile = { currentScreen = Screen.EditProfile },
                    onNavigateToHome = { currentScreen = Screen.Home },
                    onNavigateToTickets = { currentScreen = Screen.Tickets },
                    onNavigateToImprint = { currentScreen = Screen.Imprint },
                    userType = userType
                )
                is Screen.PaymentMethods -> PaymentMethodsScreen(
                    onBack = { currentScreen = Screen.Profile }
                )
                is Screen.CurrencySettings -> CurrencySettingsScreen(
                    onBack = { currentScreen = Screen.Profile },
                    onNavigateToAlerts = { currentScreen = Screen.Alerts }
                )
                is Screen.EditProfile -> EditProfileScreen(
                    onBack = { currentScreen = Screen.Profile },
                    userType = userType
                )
                is Screen.Imprint -> ImprintScreen(
                    onBack = { currentScreen = Screen.Home }
                )
                is Screen.EventDetail -> EventDetailScreen(
                    eventId = screen.eventId,
                    onBack = { currentScreen = Screen.Home },
                    onBuyTicket = { eventId -> currentScreen = Screen.BuyTicket(eventId) },
                    onLottery = { eventId -> currentScreen = Screen.Lottery(eventId) }
                )
                is Screen.BuyTicket -> BuyTicketScreen(
                    eventId = screen.eventId,
                    onBack = { currentScreen = Screen.EventDetail(screen.eventId) },
                    onConfirm = { newTicket ->
                        tickets.add(newTicket)
                        currentScreen = Screen.Tickets
                    }
                )
                is Screen.Lottery -> LotteryScreen(
                    eventId = screen.eventId,
                    onBack = { currentScreen = Screen.EventDetail(screen.eventId) },
                    onSuccess = { currentScreen = Screen.Alerts }
                )
                is Screen.TicketDetail -> TicketDetailScreen(
                    ticketId = screen.ticketId,
                    tickets = tickets,
                    onBack = { currentScreen = Screen.Tickets }
                )
            }
        }
    }
}
