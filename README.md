# StagePot — Ticket & Event Platform

A native Android app built for the eBusiness course at Hochschule Karlsruhe (Prof. Dr. Ingo Stengel). The idea is simple: ticket buying should be fair. No bots, no scalpers — just a lottery system that gives everyone the same shot.

---

## What it does

- **Browse events** — search and filter by category (Music, Sports, Comedy, etc.)
- **Buy tickets** — standard checkout with saved payment methods
- **Lottery system** — enter for $1.09 per ticket, win a full-price ticket or get 25% cashback in credits
- **Secondary market** — buy and sell verified tickets
- **Notifications** — lottery results, event reminders, new offers
- **Fan / Host mode** — fans browse and buy, hosts create and manage events
- **Profile** — edit info, manage payment methods, currency settings, credits balance

---

## Tech stack

- Kotlin + Jetpack Compose + Material3
- Navigation via sealed `Screen` class + `mutableStateOf` in `MainActivity`
- Mock data only (no backend) — see `data/MockData.kt`
- Dark mode supported

---

## Project structure

```
screens/
  HomeScreen.kt          — event feed, search, promo cards
  TicketsScreen.kt       — active, past and lottery tickets
  AlertsScreen.kt        — notifications
  ProfileScreen.kt       — user profile, settings
  OrganizeScreen.kt      — host dashboard (only visible to hosts)
  EventDetailScreen.kt   — single event view
  BuyTicketScreen.kt     — checkout
  LotteryScreen.kt       — lottery entry flow
  EditProfileScreen.kt   — profile info + password change
  CurrencySettingsScreen.kt
  PaymentMethodsScreen.kt
  StagePotComponents.kt  — shared components (BrandBar, Banner)
data/
  MockData.kt            — sample events and tickets
ui/theme/                — colors, typography, theme
```

---

## Running the app

1. Open in Android Studio
2. Sync Gradle
3. Run on emulator or device (minSdk 34)

On the login screen, use **Quick Test Login** to jump in as fan or host without typing anything.

---

## Course context

eBusiness — Sommersemester 2026  
Hochschule Karlsruhe
