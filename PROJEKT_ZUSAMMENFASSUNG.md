# StagePot – Projektzusammenfassung für neuen Chat

## Projektübersicht
Android-App **StagePot** ("Fair Tickets for Real Fans") — ein Ticket-Marktplatz mit Lotterie-System.
- **Sprache**: Kotlin + Jetpack Compose + Material3
- **Datenbank**: Android Room (SQLite) mit KSP
- **Architektur**: MVVM — `AppViewModel` + `StateFlow` + `DatabaseSeeder`
- **Package**: `com.example.ebusiness`
- **Pfad**: `C:\Users\aliar\workspace\eBusiness\EBusiness`

---

## Dateistruktur (alle wichtigen Dateien)

```
app/src/main/java/com/example/ebusiness/
├── MainActivity.kt               — Navigation (sealed class Screen), StagePotApp Composable
├── AppViewModel.kt               — ViewModel: events, tickets, lotteryEntries, currentUser
├── data/MockData.kt              — UI-Modelle: data.Event, data.Ticket (kein Room-Entity)
│
├── entities/
│   ├── UserEntity.kt             — id, name, email, avatarUrl, credits, userType
│   ├── EventEntity.kt            — id, title, date, dateStart/dateEnd (LocalDate), location, ...
│   ├── TicketEntity.kt           — id, eventId, userId, seat, section, price, status, qrCode
│   ├── AdresseEntity.kt          — id, street, city, country, ...
│   └── LotteryEntryEntity.kt     — id, eventId, userId, status (Pending/Won/Lost)
│
├── repository/
│   ├── AppDatabase.kt            — Room-Datenbank, singleton
│   ├── DatabaseSeeder.kt         — Seed: 12 Adressen, 42 Events, 7 Tickets, 3 Lottery-Einträge
│   ├── Converters.kt             — LocalDate ↔ String TypeConverter
│   └── dao/
│       ├── EventDAO.kt
│       ├── TicketDAO.kt
│       ├── UserDAO.kt            — updateAvatarUrl(), addCredits(), deductCredits()
│       ├── LotteryDAO.kt
│       └── AdresseDAO.kt
│
├── screens/
│   ├── StagePotComponents.kt     — StagePotBrandBar(applyStatusBarPadding, navigationIcon, actions)
│   │                               StagePotBanner(title, subtitle, searchQuery?)
│   ├── MainActivity.kt           — sealed class Screen mit allen Routen
│   ├── HomeScreen.kt             — Events-Liste, Suche, Kategorien, Secondary Market + Lottery Buttons
│   ├── TicketsScreen.kt          — Tabs: Active/Past/Lottery, Download-Button mit Dialog + MediaStore
│   ├── AlertsScreen.kt           — Benachrichtigungen (hardcoded), markieren als gelesen
│   ├── ProfileScreen.kt          — Avatar (Coil/PickVisualMedia), Stat-Cards, Credits-Card
│   ├── PaymentMethodsScreen.kt   — Gespeicherte Zahlungsmethoden, neue hinzufügen
│   ├── HelpCenterScreen.kt       — FAQ-Liste, Kontakt
│   ├── AppSettingsScreen.kt      — Dark Mode, Benachrichtigungen, Sicherheit, Datenschutz
│   ├── SecondaryMarketScreen.kt  — "Coming Soon" Screen
│   ├── LotteryEventsScreen.kt    — Liste aller Events mit hasLottery=true
│   ├── EventDetailScreen.kt      — Event-Details, Ticket kaufen / Lotterie
│   ├── BuyTicketScreen.kt        — Sitz & Sektion wählen, bezahlen
│   ├── LotteryScreen.kt          — Lotterie-Teilnahme für ein Event
│   ├── TicketDetailScreen.kt     — Ticket-Detailansicht mit QR-Code
│   ├── EditProfileScreen.kt
│   ├── CurrencySettingsScreen.kt
│   ├── ImprintScreen.kt
│   ├── OrganizeScreen.kt         — Nur für Host-User
│   ├── LoginScreen.kt
│   └── CreateAccountScreen.kt
```

---

## Navigation (sealed class Screen)

```kotlin
sealed class Screen {
    object Login : Screen()
    object CreateAccount : Screen()
    object Home : Screen()
    data class Tickets(val tab: Int = 0) : Screen()   // tab: 0=Active, 1=Past, 2=Lottery
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
```

---

## Datenbank (Room)

**UserEntity** — Fan-Account: `credits = 50.0` (Willkommensbonus), `name = ""`, `email = ""` (leer — Datenschutz!)
**EventEntity** — 42 echte Events (Frankfurt, Berlin, London, NYC, Sydney, Melbourne, Paris), alle ab Juli 2026
**TicketEntity** — 7 Tickets (Active/Past/Lottery) referenzieren echte Events
**LotteryEntryEntity** — 3 Einträge: Ariana Grande (Pending), Hamilton (Won), Orelsan (Lost)

**AppViewModel** filtert Events: `cutoff = LocalDate.of(2026, 7, 1)` → nur zukünftige Events

---

## Wichtige Design-Entscheidungen

### StagePotBrandBar
Alle 4 Haupt-Screens (Home, Tickets, Alerts, Profile) haben **identische Brand Bar**:
- 38dp lila Kreis (`colorScheme.primary`) + Ticket-Icon
- "StagePot" 15sp fett / "Fair Tickets for Real Fans" labelSmall
- Orange Credits-Chip: `Wallet`-Icon + echter Betrag aus DB
- Hamburger-Menü mit: Events, My Tickets, Secondary Market → Coming Soon, Profile, Imprint, Dark Mode Toggle
- **Keine Glocke** im Brand Bar
- `applyStatusBarPadding = false` bei Screens mit eigenem Scaffold (Profile, Tickets)

### Screens mit eigenem Scaffold
- `TicketsScreen` — für SnackbarHost (Download-Feedback)
- `ProfileScreen` — für SnackbarHost

### Gradient-Banner
Alle Screens haben einen abgerundeten Gradient-Banner (`bottomStart/End = 28.dp`):
- Home/Tickets/Alerts: Blau `(0xFF4A8AFF → 0xFF6B60F0)`
- Secondary Market: Pink `(0xFFEC4899 → 0xFFA855F7)`
- Payment Methods: `colorScheme.primary` Gradient

### SecondaryMarket & LotteryEvents Screens
- Kein StagePotBrandBar — Zurück-Pfeil **im** Gradient-Banner (oben links)
- `statusBarsPadding()` innerhalb des Banners

---

## Features implementiert

| Feature | Status |
|---|---|
| Room DB + KSP + Seeder | ✅ |
| 42 echte Events weltweit | ✅ |
| Events vor Juli 2026 ausblenden | ✅ |
| Profilbild (Gallery Picker + Coil AsyncImage) | ✅ |
| Credits-Balance aus DB (kein Hardcode) | ✅ |
| Download Ticket → Bestätigungsdialog → MediaStore Downloads/ | ✅ |
| Secondary Market → Coming Soon Screen | ✅ |
| Lottery Events → echte Liste mit hasLottery-Filter | ✅ |
| Stat-Cards (Tickets/Active/Locations) mit echten Daten + Navigation | ✅ |
| Help Center Screen | ✅ |
| App Settings Screen | ✅ |
| Payment Methods Screen mit Gradient-Banner | ✅ |
| Hamburger-Menü auf allen 4 Haupt-Screens | ✅ |
| Dark Mode Toggle überall | ✅ |
| Tab-Navigation Tickets (initialTab param) | ✅ |

---

## Sicherheitsregel (WICHTIG)
Der echte Name "ali" und die E-Mail "ali.arslan3375@gmail.com" dürfen **nirgendwo** im Code oder in der DB erscheinen. `UserEntity.name` und `UserEntity.email` bleiben immer leere Strings `""`.

---

## Abhängigkeiten (libs.versions.toml)
```toml
room = "2.7.1"
ksp = "2.3.9"
coil = "2.7.0"
```
```toml
room-runtime = { module = "androidx.room:room-runtime", version.ref = "room" }
room-ktx = { module = "androidx.room:room-ktx", version.ref = "room" }
room-compiler = { module = "androidx.room:room-compiler", version.ref = "room" }
coil-compose = { module = "io.coil-kt:coil-compose", version.ref = "coil" }
```
