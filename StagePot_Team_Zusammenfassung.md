# StagePot — Team-Zusammenfassung

> **Plattform:** Android (Kotlin + Jetpack Compose + Material3)
> **Zweck:** Event-Ticketing-App mit fairer Lotterie, Sekundärmarkt und Nutzer-Profil

---

## Was ist StagePot?

StagePot ist eine Android-App, über die Nutzer Tickets für Events kaufen können. Das Besondere daran ist das **Lotterie-System**: Bei beliebten Events kann man sich für eine Lotterie anmelden statt direkt zu kaufen — wer gewinnt, bekommt das Ticket, wer verliert, bekommt Credits zurück. Außerdem gibt es einen **Sekundärmarkt**, auf dem Tickets weiterverkauft werden können.

---

## Architektur auf einen Blick

```
UI (Screens)
    ↕
AppViewModel  ←→  Room-Datenbank (SQLite)
                      ↕
                 DatabaseSeeder (füllt DB beim ersten Start)
```

- **Screens** zeigen nur an, was das ViewModel liefert — sie fragen die DB nie direkt ab
- **ViewModel** hält alle Daten als `StateFlow` — Screens aktualisieren sich automatisch wenn sich etwas ändert
- **Room** ist die lokale SQLite-Datenbank — alles wird lokal gespeichert (kein Backend/Server)

---

## Screens & was sie machen

| Screen | Beschreibung |
|---|---|
| **LoginScreen** | Anmeldung + "Forgot Password"-Dialog |
| **CreateAccountScreen** | Neues Konto erstellen → wird in DB gespeichert |
| **HomeScreen** | Event-Liste mit Suche & Kategorie-Filter |
| **EventDetailScreen** | Detailansicht eines Events + Ticket-Kauf-Button |
| **BuyTicketScreen** | Zahlungsabwicklung (Credits oder Karte) |
| **TicketsScreen** | Meine Tickets — drei Tabs: Active / Past / Lottery |
| **TicketDetailScreen** | QR-Code-Ansicht + Download-Dialog |
| **AlertsScreen** | Benachrichtigungen: Lottery-Gewinn → Ticket claimen, Verlust → Credits claimen |
| **ProfileScreen** | Nutzerprofil mit Stats (Events, Tickets, Payments, Locations) |
| **EditProfileScreen** | Profil bearbeiten |
| **OrganizeScreen** | Nur für Event-Hosts: eigene Events + Analytics |
| **LotteryEventsScreen** | Alle Events mit aktivem Lotterie-System |
| **LotteryScreen** | Detail-Lotterie-Ansicht: Eintritte kaufen, Countdown, Statistiken |
| **SecondaryMarketScreen** | Sekundärmarkt (Platzhalter — Feature kommt) |
| **CurrencySettingsScreen** | Währung wählen (EUR, USD, GBP, AUD) |
| **AppSettingsScreen** | Dark Mode, Sprache usw. |
| **PaymentMethodsScreen** | Gespeicherte Zahlungsmethoden |
| **HelpCenterScreen** | FAQ & Kontakt |
| **ImprintScreen** | Impressum |

---

## Datenbank — Entities (Tabellen)

| Entity | Inhalt |
|---|---|
| `UserEntity` | Nutzerprofil: Name, E-Mail, Telefon, Ort, Typ (fan/host), Credits, Währung |
| `EventEntity` | Events: Titel, Ort, Datum, Preis (immer in EUR!), Kategorie, Lotterie-Flag |
| `TicketEntity` | Tickets: welches Event, Sitzplatz, QR-Code, Status (Active/Past/Lottery) |
| `NotificationEntity` | Alerts/Benachrichtigungen mit Typ, Aktion und optionalen Feldern für Lottery |
| `LotteryEntryEntity` | Lotterie-Einträge: welcher Nutzer hat sich für welches Event angemeldet |
| `AdresseEntity` | Adressen für Nutzer/Events |

---

## Wichtige Konzepte

### Währungsumrechnung
Alle Preise werden **intern in EUR gespeichert**. Bei der Anzeige wird per `formatPrice()` in die gewählte Währung umgerechnet. Kurse sind fest hinterlegt (Demo-Werte, ECB-Näherung):

- EUR → USD: ×1.08
- EUR → GBP: ×0.85
- EUR → AUD: ×1.65

### Lotterie-Flow
1. Nutzer kauft Eintritte für eine Lotterie (1.09 EUR/Stück)
2. Nach dem Countdown: Gewinner bekommt eine Notification mit "Claim Ticket"
3. Verlierer bekommen "Claim Cashback" — Credits werden dem Guthaben gutgeschrieben
4. Der Claim-Button im AlertsScreen legt das Ticket/Credits direkt in der DB an

### Credits-System
- Credits = App-eigenes Guthaben in der gewählten Währung
- Werden durch Lotterie-Cashback verdient
- Können beim Ticket-Kauf als Zahlungsmethode verwendet werden

### Dark Mode
Alle Farben verwenden `MaterialTheme.colorScheme.*` — wechselt automatisch zwischen hell und dunkel ohne zusätzlichen Code pro Screen.

---

## Datei-Struktur

```
com.example.ebusiness/
├── MainActivity.kt          → Navigation zwischen allen Screens
├── AppViewModel.kt          → Zentrale Logik + alle DB-Operationen
│
├── data/
│   ├── Models.kt            → UI-Modelle: Event, Ticket (keine Room-Annotationen)
│   └── CurrencyUtils.kt     → currencySymbol(), convertFromEur(), formatPrice()
│
├── entities/                → Room-Tabellen (UserEntity, EventEntity, TicketEntity, ...)
├── repository/
│   ├── AppDatabase.kt       → Room-Datenbank (Version 4)
│   ├── DatabaseSeeder.kt    → Füllt DB beim ersten App-Start mit Demo-Daten
│   ├── Converters.kt        → TypeConverter für Listen in Room
│   └── dao/                 → DAOs: je eine Interface-Datei pro Entity
│
└── screens/                 → Alle Composable-Screens + StagePotComponents.kt
```

---

## Sicherheitsregel (wichtig!)

`UserEntity.name` und `UserEntity.email` bleiben immer **leere Strings `""`** in der DB.
Echte Nutzerdaten werden nicht gespeichert — das ist bewusst so für den Demo-Betrieb.

---

## Geräte-Kompatibilität

Die App läuft auf **allen Android-Geräten ab API 26** (Android 8+). Da Jetpack Compose `dp`-Einheiten und flexible Layouts verwendet, passt sich die UI automatisch an verschiedene Bildschirmgrößen an — Pixel 3 bis Pixel 9, Fold und Tablet funktionieren ohne Code-Änderungen.

---

*Zuletzt aktualisiert: Juni 2026*
