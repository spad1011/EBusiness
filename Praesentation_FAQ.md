# StagePot — Technische FAQ für die Präsentation

---

## Architektur & Technologie

**Welche Technologien habt ihr verwendet?**
Kotlin + Jetpack Compose + Material3 für die UI. Room (SQLite) als lokale Datenbank mit KSP-Codegenerierung. Das Architekturmuster ist MVVM — AppViewModel hält alle Daten als StateFlow, die Screens reagieren automatisch auf Änderungen.

**Warum kein Backend / keine API?**
Für den Kurs-Scope haben wir uns bewusst auf eine vollständig lokale Architektur konzentriert. Room als SQLite-Wrapper bildet alle Datenbankoperationen ab, die in einer echten App auch gegen ein Backend laufen würden — der Austausch wäre durch das MVVM-Pattern einfach möglich.

**Was ist ein StateFlow?**
Ein StateFlow ist ein Kotlin-Datenstream. Wenn sich Daten in der DB ändern, fließen die neuen Werte automatisch zu allen Screens — ohne dass man manuell "refresh" aufrufen muss. Das verhindert inkonsistente UI-Zustände.

**Was ist der DatabaseSeeder?**
Ein Kotlin-Objekt, das beim ersten App-Start prüft ob die DB leer ist, und sie dann mit Demo-Events, Tickets, Usern und Notifications befüllt. Ohne ihn wäre die App beim ersten Start komplett leer.

---

## Datenbank

**Wie ist die Datenbank aufgebaut?**
7 Tabellen: `users`, `events`, `adresses`, `tickets`, `lottery_entries`, `notifications`, `payment_methods`. Jede Tabelle hat ein zugehöriges DAO (Data Access Object) mit Room-Queries.

**Was passiert wenn sich das DB-Schema ändert?**
Wir verwenden `fallbackToDestructiveMigration()` — die DB wird gelöscht und vom Seeder neu aufgebaut. In einer produktiven App würde man stattdessen explizite Migrations-Skripte schreiben.

**Warum wird die DB-Version hochgezählt?**
Room prüft beim Start ob die gespeicherte Version mit der Code-Version übereinstimmt. Bei Unterschied greift die Migration — in unserem Fall: neu aufbauen.

---

## Features

**Wie funktioniert die Lotterie?**
Der User kauft Eintritte (1,09 EUR/Stück) — die Credits werden sofort abgezogen. Das Ergebnis erscheint als Notification: Gewinner bekommen "Claim Ticket", Verlierer bekommen Cashback gutgeschrieben. Beide Aktionen schreiben direkt in die DB.

**Wie funktioniert die Währungsumrechnung?**
Alle Preise sind intern in EUR gespeichert. Bei der Anzeige rechnet `formatPrice()` mit festen ECB-Näherungskursen um (z.B. EUR × 1,08 = USD). Der User wählt seine Währung in den Einstellungen — die Änderung wird in `UserEntity.currency` gespeichert und alle Screens aktualisieren sich automatisch.

**Wie funktioniert der Login?**
Es gibt zwei Wege: "Quick Test Login" lädt direkt den Demo-Fan oder Demo-Host aus der DB (für Testzwecke). Der normale "Sign In" prüft E-Mail und Passwort gegen SHA-256-Hashes in der DB — keine Klartextpasswörter gespeichert.

**Warum sind Name und E-Mail im UserEntity leer?**
Sicherheitsentscheidung: Wir speichern keine personenbezogenen Daten im Klartext. Die Login-E-Mail wird nur als SHA-256-Hash gespeichert (`loginEmailHash`), das Passwort ebenfalls (`passwordHash`).

**Was ist der Unterschied zwischen Fan und Host?**
"fan" sieht Home, Tickets, Alerts und Profil. "host" bekommt zusätzlich den Organize-Tab mit Event-Verwaltung und Analytics. Der userType wird in `UserEntity` gespeichert und steuert die Navigation.

---

## UI & Design

**Warum Jetpack Compose statt XML-Layouts?**
Compose ist der moderne Android-Standard (seit 2021 stabil). Deklarative UI — man beschreibt wie die UI aussehen soll, nicht wie man sie aufbaut. Weniger Code, bessere Dark-Mode-Unterstützung, einfachere State-Verwaltung.

**Wie funktioniert Dark Mode?**
Alle Farben verwenden `MaterialTheme.colorScheme.*` statt hardcodierter Hex-Werte. Material3 schaltet automatisch zwischen Hell- und Dunkel-Palette um — kein zusätzlicher Code pro Screen nötig.

**Passt sich die App an verschiedene Bildschirmgrößen an?**
Ja. Jetpack Compose arbeitet mit `dp`-Einheiten (dichteunabhängige Pixel) und flexiblen Layouts (`fillMaxWidth`, `weight`). Die App läuft auf Pixel 3 bis Pixel 9, Fold und Tablets ohne Code-Änderungen.

---

## Mögliche Erweiterungen (falls gefragt)

- **Echtes Backend**: Spring Boot / Firebase als API — ViewModel-Logik bleibt gleich, nur Repository-Schicht tauschen
- **Push Notifications**: Firebase Cloud Messaging statt lokaler Notification-Tabelle
- **Echter QR-Code**: zxing-android-embedded Bibliothek einbinden (aktuell simuliert)
- **Sekundärmarkt**: Datenbankstruktur ist vorbereitet, UI ist Platzhalter
- **Echte Zahlungsabwicklung**: Stripe oder Google Pay SDK integrieren
