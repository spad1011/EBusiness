# StagePot — Ticket & Event Platform

A native Android app built for the eBusiness course at Hochschule Karlsruhe (Prof. Dr. Ingo Stengel). The idea is simple: ticket buying should be fair. No bots, no scalpers — just a lottery system that gives everyone the same shot.

---

## What it does

- **Browse events** — search and filter by category (Music, Sports, Comedy, etc.)
- **Buy tickets** — standard checkout with credits or saved payment methods
- **Lottery system** — enter for €1.09 per ticket, win a full-price ticket or get cashback credits
- **Claim rewards** — lottery results appear as notifications; one tap to claim ticket or cashback
- **Secondary market** — buy and sell verified tickets (coming soon)
- **Notifications** — lottery results, event reminders, new offers
- **Fan / Host mode** — fans browse and buy, hosts create and manage events with analytics
- **Profile** — edit info, manage payment methods, credits balance, view your event locations
- **Currency settings** — switch between EUR, USD, GBP and AUD; all prices convert automatically
- **Dark mode** — full support across all screens

---

## Tech stack

- Kotlin + Jetpack Compose + Material3
- **Room (SQLite)** with KSP — all data is stored locally, no backend/server
- **MVVM** — `AppViewModel` exposes data as `StateFlow`; screens use `collectAsState()`
- Navigation via sealed `Screen` class + `mutableStateOf` in `MainActivity`
- `DatabaseSeeder` fills the DB with demo data on first launch
- Dark mode via `MaterialTheme.colorScheme.*` throughout

---

## Project structure

```
com.example.ebusiness/
├── MainActivity.kt              — navigation between all screens
├── AppViewModel.kt              — all business logic and DB operations
│
├── data/
│   ├── Models.kt                — UI models: Event, Ticket (no Room annotations)
│   └── CurrencyUtils.kt         — currencySymbol(), convertFromEur(), formatPrice()
│
├── entities/                    — Room table definitions
│   ├── UserEntity.kt
│   ├── EventEntity.kt
│   ├── TicketEntity.kt
│   ├── NotificationEntity.kt
│   ├── LotteryEntryEntity.kt
│   ├── PaymentMethodEntity.kt
│   └── AdresseEntity.kt
│
├── repository/
│   ├── AppDatabase.kt           — Room database (version 4)
│   ├── DatabaseSeeder.kt        — seeds DB on first launch
│   ├── Converters.kt            — TypeConverters for Room
│   └── dao/                     — one DAO interface per entity
│
└── screens/
    ├── HomeScreen.kt            — event feed, search, category filter
    ├── TicketsScreen.kt         — Active / Past / Lottery tabs
    ├── TicketDetailScreen.kt    — QR code view + download dialog
    ├── AlertsScreen.kt          — notifications + claim ticket/cashback
    ├── ProfileScreen.kt         — user stats, contact info, locations dialog
    ├── OrganizeScreen.kt        — host dashboard with analytics
    ├── EventDetailScreen.kt     — single event view + buy button
    ├── BuyTicketScreen.kt       — checkout flow
    ├── LotteryScreen.kt         — lottery entry, countdown, stats
    ├── LotteryEventsScreen.kt   — all events with active lottery
    ├── SecondaryMarketScreen.kt — secondary market (placeholder)
    ├── EditProfileScreen.kt     — edit profile info + password
    ├── CurrencySettingsScreen.kt
    ├── PaymentMethodsScreen.kt
    ├── AppSettingsScreen.kt
    ├── LoginScreen.kt           — login + forgot password dialog
    ├── CreateAccountScreen.kt   — register new account → saved to DB
    ├── HelpCenterScreen.kt
    ├── ImprintScreen.kt
    └── StagePotComponents.kt   — shared components (BrandBar, Banner, SearchBar)
```

---

## Currency conversion

All prices are stored internally in **EUR**. When displaying, `formatPrice(amount, currency)` converts to the user's selected currency using fixed exchange rates (ECB approximations for demo purposes):

| Currency | Rate |
|---|---|
| USD | EUR × 1.08 |
| GBP | EUR × 0.85 |
| AUD | EUR × 1.65 |
| CHF | EUR × 0.96 |

---

## Running the app

1. Open in Android Studio
2. Sync Gradle
3. Run on emulator or device (minSdk 26)

On the login screen, use **Quick Test Login** to jump in as fan or host without typing anything. The database is seeded automatically on first launch with demo events, tickets and notifications.

---

## Course context

eBusiness — Sommersemester 2026  
Hochschule Karlsruhe
