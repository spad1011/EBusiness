package com.example.ebusiness

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ConfirmationNumber
import androidx.compose.material.icons.filled.Event
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.ebusiness.screens.*
import com.example.ebusiness.ui.theme.EBusinessTheme

sealed class Screen {
    object Login : Screen()
    object CreateAccount : Screen()
    object Home : Screen()
    data class Tickets(val tab: Int = 0) : Screen()
    object Alerts : Screen()
    object Organize : Screen()
    object Profile : Screen()
    object PaymentMethods : Screen()
    object CurrencySettings : Screen()
    object EditProfile : Screen()
    object Imprint : Screen()
    object HelpCenter : Screen()
    object AppSettings : Screen()
    object SecondaryMarket : Screen()
    object LotteryEvents : Screen()
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
        val vm: AppViewModel = viewModel()

        val events         by vm.events.collectAsState()
        val tickets        by vm.tickets.collectAsState()
        val selectedEvent  by vm.selectedEvent.collectAsState()
        val lotteryEntries by vm.lotteryEntries.collectAsState()
        val currentUser    by vm.currentUser.collectAsState()
        val notifications  by vm.notifications.collectAsState()
        val paymentMethods by vm.paymentMethods.collectAsState()
        val loginError     by vm.loginError.collectAsState()

        var currentScreen by remember { mutableStateOf<Screen>(Screen.Login) }
        var userType      by remember { mutableStateOf("fan") }

        val unreadCount = notifications.count { !it.isRead }
        val currency    = currentUser?.currency ?: "EUR"

        val bottomScreens = buildSet {
            add(Screen.Home); add(Screen.Tickets()); add(Screen.Alerts); add(Screen.Profile)
            if (userType == "host") add(Screen.Organize)
        }
        val showBottomBar = bottomScreens.any { it::class == currentScreen::class }

        LaunchedEffect(currentScreen) {
            when (val s = currentScreen) {
                is Screen.EventDetail -> vm.loadEventById(s.eventId)
                is Screen.BuyTicket  -> vm.loadEventById(s.eventId)
                else -> Unit
            }
        }

        if (events.isEmpty() && currentScreen is Screen.Home) {
            Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator()
            }
            return@EBusinessTheme
        }

        Scaffold(
            bottomBar = {
                if (showBottomBar) {
                    NavigationBar {
                        NavigationBarItem(
                            selected = currentScreen is Screen.Home,
                            onClick  = { currentScreen = Screen.Home },
                            icon     = { Icon(Icons.Default.Home, null) },
                            label    = { Text("Home") }
                        )
                        NavigationBarItem(
                            selected = currentScreen is Screen.Tickets,
                            onClick  = { currentScreen = Screen.Tickets() },
                            icon = {
                                BadgedBox(badge = {
                                    if (tickets.isNotEmpty()) Badge { Text("${tickets.size}") }
                                }) { Icon(Icons.Default.ConfirmationNumber, null) }
                            },
                            label = { Text("Tickets") }
                        )
                        NavigationBarItem(
                            selected = currentScreen is Screen.Alerts,
                            onClick  = { currentScreen = Screen.Alerts },
                            icon = {
                                BadgedBox(badge = {
                                    if (unreadCount > 0) Badge { Text("$unreadCount") }
                                }) { Icon(Icons.Default.Notifications, null) }
                            },
                            label = { Text("Alerts") }
                        )
                        if (userType == "host") {
                            NavigationBarItem(
                                selected = currentScreen is Screen.Organize,
                                onClick  = { currentScreen = Screen.Organize },
                                icon     = { Icon(Icons.Default.Event, null) },
                                label    = { Text("Organize") }
                            )
                        }
                        NavigationBarItem(
                            selected = currentScreen is Screen.Profile,
                            onClick  = { currentScreen = Screen.Profile },
                            icon     = { Icon(Icons.Default.Person, null) },
                            label    = { Text("Profile") }
                        )
                    }
                }
            }
        ) { padding ->
            val screen = currentScreen
            when (screen) {
                is Screen.Login -> LoginScreen(
                    onLoginAsFan = {
                        userType = "fan"; vm.loginAs("fan"); currentScreen = Screen.Home
                    },
                    onLoginAsHost = {
                        userType = "host"; vm.loginAs("host"); currentScreen = Screen.Home
                    },
                    onCreateAccount = { currentScreen = Screen.CreateAccount },
                    onGuestMode = {
                        userType = "fan"; vm.loginAs("fan"); currentScreen = Screen.Home
                    },
                    onLogin = { email, password ->
                        vm.login(email, password) { uType ->
                            userType = uType
                            currentScreen = Screen.Home
                        }
                    },
                    loginError   = loginError,
                    onClearError = { vm.clearLoginError() }
                )
                is Screen.CreateAccount -> CreateAccountScreen(
                    onBack    = { currentScreen = Screen.Login },
                    onSuccess = { displayName, email, phone, location, uType, pwd ->
                        userType = uType
                        vm.createUser(displayName, email, phone, location, uType, pwd) {
                            currentScreen = Screen.Home
                        }
                    }
                )
                is Screen.Home -> HomeScreen(
                    paddingValues               = padding,
                    allEvents                   = events,
                    onEventClick                = { eventId -> currentScreen = Screen.EventDetail(eventId) },
                    onNavigateToTickets         = { currentScreen = Screen.Tickets() },
                    onNavigateToProfile         = { currentScreen = Screen.Profile },
                    onNavigateToAlerts          = { currentScreen = Screen.Alerts },
                    onNavigateToImprint         = { currentScreen = Screen.Imprint },
                    onNavigateToSecondaryMarket = { currentScreen = Screen.SecondaryMarket },
                    onNavigateToLotteryEvents   = { currentScreen = Screen.LotteryEvents },
                    isDarkMode                  = darkMode,
                    onToggleDarkMode            = { darkMode = !darkMode },
                    credits                     = currentUser?.credits ?: 0.0,
                    currency                    = currency
                )
                is Screen.Tickets -> TicketsScreen(
                    paddingValues               = padding,
                    tickets                     = tickets,
                    initialTab                  = screen.tab,
                    onTicketClick               = { ticketId -> currentScreen = Screen.TicketDetail(ticketId) },
                    credits                     = currentUser?.credits ?: 0.0,
                    currency                    = currency,
                    isDarkMode                  = darkMode,
                    onToggleDarkMode            = { darkMode = !darkMode },
                    onNavigateToHome            = { currentScreen = Screen.Home },
                    onNavigateToProfile         = { currentScreen = Screen.Profile },
                    onNavigateToImprint         = { currentScreen = Screen.Imprint },
                    onNavigateToSecondaryMarket = { currentScreen = Screen.SecondaryMarket },
                    userType                    = userType
                )
                is Screen.Alerts -> AlertsScreen(
                    paddingValues               = padding,
                    notifications               = notifications,
                    credits                     = currentUser?.credits ?: 0.0,
                    currency                    = currency,
                    isDarkMode                  = darkMode,
                    onToggleDarkMode            = { darkMode = !darkMode },
                    onNavigateToHome            = { currentScreen = Screen.Home },
                    onNavigateToTickets         = { currentScreen = Screen.Tickets() },
                    onNavigateToProfile         = { currentScreen = Screen.Profile },
                    onNavigateToImprint         = { currentScreen = Screen.Imprint },
                    onNavigateToSecondaryMarket = { currentScreen = Screen.SecondaryMarket },
                    onMarkRead                  = { id -> vm.markNotificationRead(id) },
                    onMarkAllRead               = { vm.markAllNotificationsRead() },
                    onDismiss                   = { id -> vm.dismissNotification(id) },
                    onClaimTicket               = { notifId, eventId -> vm.claimLotteryTicket(notifId, eventId) },
                    onClaimCashback             = { notifId, amount -> vm.claimCashback(notifId, amount) }
                )
                is Screen.Organize -> OrganizeScreen(
                    paddingValues = padding,
                    userType      = userType,
                    events        = events,
                    currency      = currency,
                    onCreateEvent = { event, adresse -> vm.createEvent(event, adresse) }
                )
                is Screen.Profile -> ProfileScreen(
                    paddingValues               = padding,
                    isDarkMode                  = darkMode,
                    onToggleDarkMode            = { darkMode = !darkMode },
                    onLogout                    = { currentScreen = Screen.Login },
                    onPaymentMethods            = { currentScreen = Screen.PaymentMethods },
                    onNavigateToAlerts          = { currentScreen = Screen.Alerts },
                    onCurrencySettings          = { currentScreen = Screen.CurrencySettings },
                    onEditProfile               = { currentScreen = Screen.EditProfile },
                    onNavigateToHome            = { currentScreen = Screen.Home },
                    onNavigateToTickets         = { currentScreen = Screen.Tickets() },
                    onNavigateToTicketsTab      = { tab -> currentScreen = Screen.Tickets(tab) },
                    onNavigateToOrganize        = { currentScreen = Screen.Organize },
                    onNavigateToImprint         = { currentScreen = Screen.Imprint },
                    onNavigateToSecondaryMarket = { currentScreen = Screen.SecondaryMarket },
                    onAddCredits                = { currentScreen = Screen.PaymentMethods },
                    onHelpCenter                = { currentScreen = Screen.HelpCenter },
                    onAppSettings               = { currentScreen = Screen.AppSettings },
                    tickets                     = tickets,
                    avatarUrl                   = currentUser?.avatarUrl ?: "",
                    onAvatarChange              = { url -> vm.updateAvatar(url) },
                    userType                    = userType,
                    credits                     = currentUser?.credits ?: 0.0,
                    currency                    = currency,
                    displayName                 = currentUser?.displayName ?: "",
                    userEmail                   = currentUser?.email ?: "",
                    userPhone                   = currentUser?.phone ?: "",
                    userLocation                = currentUser?.location ?: "",
                    memberSince                 = currentUser?.memberSince ?: "2026",
                    paymentMethodCount          = paymentMethods.size,
                    events                      = events
                )
                is Screen.PaymentMethods -> PaymentMethodsScreen(
                    onBack         = { currentScreen = Screen.Profile },
                    savedMethods   = paymentMethods,
                    onAddMethod    = { name, type, emoji -> vm.addPaymentMethod(name, type, emoji) },
                    onRemoveMethod = { id -> vm.removePaymentMethod(id) }
                )
                is Screen.CurrencySettings -> CurrencySettingsScreen(
                    onBack             = { currentScreen = Screen.Profile },
                    onNavigateToAlerts = { currentScreen = Screen.Alerts },
                    credits            = currentUser?.credits ?: 0.0,
                    currency           = currency,
                    onSave             = { code -> vm.updateCurrency(code) },
                    unreadCount        = unreadCount
                )
                is Screen.EditProfile -> EditProfileScreen(
                    onBack             = { currentScreen = Screen.Profile },
                    userType           = userType,
                    initialDisplayName = currentUser?.displayName ?: "",
                    initialPhone       = currentUser?.phone ?: "",
                    initialLocation    = currentUser?.location ?: "",
                    onSaveProfile      = { name, phone, loc -> vm.updateProfile(name, phone, loc) }
                )
                is Screen.Imprint -> ImprintScreen(
                    onBack = { currentScreen = Screen.Home }
                )
                is Screen.HelpCenter -> HelpCenterScreen(
                    onBack = { currentScreen = Screen.Profile }
                )
                is Screen.AppSettings -> AppSettingsScreen(
                    onBack           = { currentScreen = Screen.Profile },
                    isDarkMode       = darkMode,
                    onToggleDarkMode = { darkMode = !darkMode }
                )
                is Screen.SecondaryMarket -> SecondaryMarketScreen(
                    onBack = { currentScreen = Screen.Home }
                )
                is Screen.LotteryEvents -> LotteryEventsScreen(
                    events         = events,
                    lotteryEntries = lotteryEntries,
                    onBack         = { currentScreen = Screen.Home },
                    onEnterLottery = { eventId -> currentScreen = Screen.Lottery(eventId) },
                    currency       = currency
                )
                is Screen.EventDetail -> EventDetailScreen(
                    event                 = selectedEvent,
                    onBack                = { currentScreen = Screen.Home },
                    onBuyTicket           = { eventId -> currentScreen = Screen.BuyTicket(eventId) },
                    onLottery             = { eventId -> currentScreen = Screen.Lottery(eventId) },
                    lotteryAlreadyEntered = vm.isInLottery(screen.eventId),
                    currency              = currency
                )
                is Screen.BuyTicket -> BuyTicketScreen(
                    event          = selectedEvent,
                    paymentMethods = paymentMethods,
                    credits        = currentUser?.credits ?: 0.0,
                    currency       = currency,
                    onBack         = { currentScreen = Screen.EventDetail(screen.eventId) },
                    onConfirm      = { seat, section ->
                        vm.buyTicket(
                            eventId = screen.eventId,
                            seat    = seat,
                            section = section,
                            price   = selectedEvent?.price ?: 0.0,
                            onDone  = { currentScreen = Screen.Tickets() }
                        )
                    }
                )
                is Screen.Lottery -> {
                    val lotteryCount by produceState(initialValue = 0, screen.eventId) {
                        value = vm.getLotteryEntryCount(screen.eventId)
                    }
                    LotteryScreen(
                        eventId             = screen.eventId,
                        event               = selectedEvent,
                        onBack              = { currentScreen = Screen.EventDetail(screen.eventId) },
                        onSuccess           = {
                            vm.enterLottery(screen.eventId)
                            currentScreen = Screen.Alerts
                        },
                        credits             = currentUser?.credits ?: 0.0,
                        currency            = currency,
                        totalLotteryEntries = lotteryCount
                    )
                }
                is Screen.TicketDetail -> TicketDetailScreen(
                    ticketId = screen.ticketId,
                    tickets  = tickets,
                    onBack   = { currentScreen = Screen.Tickets() }
                )
            }
        }
    }
}
