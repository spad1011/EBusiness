package com.example.ebusiness

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.ebusiness.data.Event
import com.example.ebusiness.data.Ticket
import com.example.ebusiness.data.sha256
import com.example.ebusiness.entities.*
import com.example.ebusiness.repository.AppDatabase
import com.example.ebusiness.repository.DatabaseSeeder
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.format.DateTimeFormatter

/**
 * Das zentrale ViewModel der App.
 *
 * Hier läuft alles zusammen: Events aus der DB laden, Tickets kaufen,
 * Lotterie betreten, User-Login umschalten — alles läuft über diesen ViewModel.
 * Die Screens selbst wissen nichts von der Datenbank, sie kriegen nur
 * fertige Listen und rufen Funktionen auf.
 */
class AppViewModel(application: Application) : AndroidViewModel(application) {

    // Datenbankzugang — einmal initialisiert, dann wiederverwendet
    private val db         = AppDatabase.getInstance(application)
    private val eventDao   = db.eventDao()
    private val ticketDao  = db.ticketDao()
    private val userDao    = db.userDao()
    private val lotteryDao = db.lotteryDao()

    // Welcher User gerade eingeloggt ist (1 = Fan, 2 = Host)
    private val _currentUserId = MutableStateFlow(1)

    // ── Öffentliche State-Flows (die Screens hören darauf) ────────────────────

    // Alle Events für den HomeScreen
    private val _events = MutableStateFlow<List<Event>>(emptyList())
    val events = _events.asStateFlow()

    // Das einzelne Event das gerade angeschaut wird (EventDetail / BuyTicket)
    private val _selectedEvent = MutableStateFlow<Event?>(null)
    val selectedEvent = _selectedEvent.asStateFlow()

    // Tickets des eingeloggten Users
    private val _tickets = MutableStateFlow<List<Ticket>>(emptyList())
    val tickets = _tickets.asStateFlow()

    // Profil des eingeloggten Users
    private val _currentUser = MutableStateFlow<UserEntity?>(null)
    val currentUser = _currentUser.asStateFlow()

    // Lotterie-Teilnahmen des Users (für den Alerts-Tab und den Lotterie-Button)
    private val _lotteryEntries = MutableStateFlow<List<LotteryEntryEntity>>(emptyList())
    val lotteryEntries = _lotteryEntries.asStateFlow()

    // Benachrichtigungen des Users (für den AlertsScreen)
    private val _notifications = MutableStateFlow<List<NotificationEntity>>(emptyList())
    val notifications = _notifications.asStateFlow()

    // Zahlungsmethoden des Users (für den PaymentMethodsScreen)
    private val _paymentMethods = MutableStateFlow<List<PaymentMethodEntity>>(emptyList())
    val paymentMethods = _paymentMethods.asStateFlow()

    // Fehlermeldungen — falls mal was schiefläuft
    private val _error = MutableStateFlow<String?>(null)
    val error = _error.asStateFlow()

    // Login-Fehlermeldung — wird im LoginScreen angezeigt
    private val _loginError = MutableStateFlow<String?>(null)
    val loginError = _loginError.asStateFlow()

    // Beim Start der App: DB befüllen (falls leer) und alles laden
    init {
        viewModelScope.launch {
            try {
                DatabaseSeeder.seed(db)
                loadAll()
            } catch (e: Exception) {
                Log.e("AppViewModel", "Fehler beim Starten / Seeding", e)
                _error.value = e.message
            }
        }
    }

    // ── Ladefunktionen ────────────────────────────────────────────────────────

    /**
     * Lädt alles auf einmal — Events, Tickets, User und Lotterie-Einträge.
     * Wird z.B. nach dem Login aufgerufen, damit alles frisch ist.
     */
    fun loadAll() {
        loadEvents()
        loadTickets()
        loadUser()
        loadLotteryEntries()
        loadNotifications()
        loadPaymentMethods()
    }

    /**
     * Lädt Events aus der DB, optional gefiltert nach Suchbegriff und Kategorie.
     * Wenn beides leer ist, kommen einfach alle Events zurück.
     */
    fun loadEvents(query: String? = null, category: String? = null) {
        viewModelScope.launch {
            val raw = if (query.isNullOrBlank() && category.isNullOrBlank())
                eventDao.getAll()
            else
                eventDao.search(query, category?.takeIf { it != "All" })

            // Nur Events ab Juli 2026 anzeigen — vergangene Events ausblenden
            val cutoff = LocalDate.of(2026, 7, 1)
            _events.value = raw
                .filter { it.dateEnd >= cutoff }
                .map { it.toUiModel() }
        }
    }

    /**
     * Lädt ein einzelnes Event anhand seiner ID.
     * Wird aufgerufen wenn der User auf eine Event-Karte klickt.
     */
    fun loadEventById(id: Int) {
        viewModelScope.launch {
            _selectedEvent.value = eventDao.getById(id)?.toUiModel()
        }
    }

    /**
     * Lädt alle Tickets des aktuell eingeloggten Users.
     * Sortiert nach Kaufdatum, neueste zuerst.
     */
    fun loadTickets() {
        viewModelScope.launch {
            val userId = _currentUserId.value
            _tickets.value = ticketDao.getByUser(userId).map { it.toUiModel() }
        }
    }

    /**
     * Lädt das Profil des eingeloggten Users aus der DB.
     */
    fun loadUser() {
        viewModelScope.launch {
            _currentUser.value = userDao.getById(_currentUserId.value)
        }
    }

    /**
     * Lädt alle Lotterie-Teilnahmen des eingeloggten Users.
     * Wird u.a. gebraucht um den Lotterie-Button grau zu machen wenn
     * der User schon teilgenommen hat.
     */
    fun loadLotteryEntries() {
        viewModelScope.launch {
            _lotteryEntries.value = lotteryDao.getByUser(_currentUserId.value)
        }
    }

    /** Lädt Notifications des eingeloggten Users */
    fun loadNotifications() {
        viewModelScope.launch {
            _notifications.value = db.notificationDao().getByUser(_currentUserId.value)
        }
    }

    /** Lädt Zahlungsmethoden des eingeloggten Users */
    fun loadPaymentMethods() {
        viewModelScope.launch {
            _paymentMethods.value = db.paymentMethodDao().getByUser(_currentUserId.value)
        }
    }

    // ── Login / User-Wechsel ──────────────────────────────────────────────────

    /**
     * Wechselt den eingeloggten User: "fan" → User 1, "host" → User 2.
     * Nur für den Quick Test Login — umgeht die echte Passwort-Prüfung.
     */
    fun loginAs(userType: String) {
        val userId = if (userType == "host") 2 else 1
        _currentUserId.value = userId
        _loginError.value = null
        loadAll()
    }

    /**
     * Echter Login: E-Mail-Hash in DB suchen, Passwort-Hash vergleichen.
     * Bei Erfolg wird onSuccess mit dem userType aufgerufen, bei Fehler
     * erscheint eine Fehlermeldung im LoginScreen.
     */
    fun login(email: String, password: String, onSuccess: (userType: String) -> Unit) {
        viewModelScope.launch {
            _loginError.value = null

            // Eingaben prüfen bevor wir überhaupt die DB anfragen
            if (email.isBlank() || password.isBlank()) {
                _loginError.value = "Please enter email and password."
                return@launch
            }

            // E-Mail hashen und in DB suchen
            val emailHash = sha256(email.trim().lowercase())
            val user = userDao.getByLoginEmailHash(emailHash)

            when {
                user == null -> _loginError.value = "No account found for this email."
                user.passwordHash != sha256(password) -> _loginError.value = "Wrong password."
                else -> {
                    // Login erfolgreich — User einloggen und alles laden
                    _currentUserId.value = user.id
                    loadAll()
                    onSuccess(user.userType)   // userType direkt aus DB-Objekt übergeben
                }
            }
        }
    }

    /** Löscht die Login-Fehlermeldung — z.B. wenn der User anfängt zu tippen */
    fun clearLoginError() {
        _loginError.value = null
    }

    // ── Profilbild ────────────────────────────────────────────────────────────

    /**
     * Speichert eine neue Profilbild-URL für den aktuellen User.
     * Wird mit einem leeren String aufgerufen wenn das Bild gelöscht wird.
     * Nach dem Speichern wird das currentUser-StateFlow automatisch aktualisiert.
     */
    fun updateAvatar(url: String) {
        viewModelScope.launch {
            val userId = _currentUserId.value
            db.userDao().updateAvatarUrl(userId, url)
            // currentUser neu laden damit die UI sofort das neue Bild zeigt
            _currentUser.value = db.userDao().getById(userId)
        }
    }

    // ── Ticket kaufen ─────────────────────────────────────────────────────────

    /**
     * Kauft ein Ticket für ein Event.
     *
     * Ablauf:
     * 1. Event und Adresse aus der DB holen
     * 2. Datum formatieren und QR-Code generieren
     * 3. Ticket in die DB schreiben
     * 4. "ticketsSold" beim Event um 1 erhöhen
     * 5. Ticket-Liste neu laden
     * 6. onDone aufrufen damit die UI weiternavigieren kann
     */
    fun buyTicket(
        eventId: Int,
        seat: String,
        section: String,
        price: Double,
        onDone: (Ticket) -> Unit
    ) {
        viewModelScope.launch {
            // Event aus DB holen — wenn es nicht existiert, abbrechen
            val event = eventDao.getById(eventId) ?: return@launch
            val adresse = eventDao.getAdresseById(event.adresseId)
            val location = adresse?.let { "${it.city}, ${it.country}" } ?: ""

            // Lesbares Datum für die Ticket-Anzeige
            val dateStr = event.dateStart.format(DateTimeFormatter.ofPattern("EEE, MMM d, yyyy"))

            // Eindeutiger QR-Code — Timestamp am Ende macht ihn einmalig
            val qr = "SP-$eventId-${event.title.take(6).uppercase()}-${System.currentTimeMillis() % 100000}"

            val ticket = TicketEntity(
                eventId      = eventId,
                userId       = _currentUserId.value,
                eventTitle   = event.title,
                eventDate    = dateStr,
                eventLocation = location,
                seat         = seat,
                section      = section,
                price        = price,
                status       = "Active",
                qrCode       = qr
            )

            // Ticket speichern und Verkaufszähler erhöhen
            val newId = ticketDao.insert(ticket).toInt()
            eventDao.incrementTicketsSold(eventId)

            // Ticket-Liste in der UI aktualisieren
            val uiTicket = ticket.copy(id = newId).toUiModel()
            loadTickets()
            onDone(uiTicket)
        }
    }

    // ── Lotterie ──────────────────────────────────────────────────────────────

    /**
     * Trägt den User in die Lotterie für ein Event ein.
     * Wenn er schon teilgenommen hat, passiert nichts (doppelte Teilnahme verhindern).
     */
    fun enterLottery(eventId: Int, ticketCount: Int = 1) {
        viewModelScope.launch {
            // Prüfen ob der User schon einen Eintrag für dieses Event hat
            val existing = lotteryDao.getEntry(eventId, _currentUserId.value)
            if (existing != null) return@launch

            lotteryDao.insert(
                LotteryEntryEntity(
                    eventId     = eventId,
                    userId      = _currentUserId.value,
                    ticketsPaid = ticketCount,
                    amountPaid  = ticketCount * 1.09
                )
            )

            // Liste aktualisieren damit der Button sofort grau wird
            loadLotteryEntries()
        }
    }

    /**
     * Gibt zurück ob der User für ein bestimmtes Event schon in der Lotterie ist.
     * Wird benutzt um den Lotterie-Button auf "bereits beigetreten" zu setzen.
     */
    fun isInLottery(eventId: Int): Boolean =
        _lotteryEntries.value.any { it.eventId == eventId }

    // ── Event erstellen (nur für Hosts) ──────────────────────────────────────

    /**
     * Erstellt ein neues Event inklusive Adresse.
     * Erst die Adresse speichern (um die ID zu kriegen), dann das Event verknüpfen.
     */
    fun createEvent(event: EventEntity, adresse: AdresseEntity) {
        viewModelScope.launch {
            val adresseId = db.adresseDao().insert(adresse).toInt()
            eventDao.insert(event.copy(adresseId = adresseId))
            loadEvents() // HomeScreen soll das neue Event sofort sehen
        }
    }

    /**
     * Löscht ein Event aus der DB (inkl. zugehöriger Tickets durch CASCADE).
     */
    fun deleteEvent(eventId: Int) {
        viewModelScope.launch {
            eventDao.deleteById(eventId)
            loadEvents()
        }
    }

    // ── Notifications ─────────────────────────────────────────────────────────

    /** Markiert eine einzelne Notification als gelesen */
    fun markNotificationRead(id: Int) {
        viewModelScope.launch {
            db.notificationDao().markRead(id)
            loadNotifications()
        }
    }

    /** Markiert alle Notifications als gelesen */
    fun markAllNotificationsRead() {
        viewModelScope.launch {
            db.notificationDao().markAllRead(_currentUserId.value)
            loadNotifications()
        }
    }

    /** Löscht eine Notification (Dismiss) */
    fun dismissNotification(id: Int) {
        viewModelScope.launch {
            db.notificationDao().deleteById(id)
            loadNotifications()
        }
    }

    /**
     * Lottery gewonnen: Ticket für das verknüpfte Event anlegen + Notification als gelesen markieren.
     */
    fun claimLotteryTicket(notificationId: Int, eventId: Int) {
        viewModelScope.launch {
            val event = eventDao.getById(eventId) ?: return@launch
            val adresse = eventDao.getAdresseById(event.adresseId)
            val location = adresse?.let { "${it.city}, ${it.country}" } ?: ""
            val dateStr = event.dateStart.format(DateTimeFormatter.ofPattern("EEE, MMM d, yyyy"))
            val qr = "SP-$eventId-${event.title.take(6).uppercase()}-${System.currentTimeMillis() % 100000}"

            ticketDao.insert(
                TicketEntity(
                    eventId       = eventId,
                    userId        = _currentUserId.value,
                    eventTitle    = event.title,
                    eventDate     = dateStr,
                    eventLocation = location,
                    seat          = "GA",
                    section       = "General",
                    price         = event.price,
                    status        = "Active",
                    qrCode        = qr
                )
            )
            db.notificationDao().markRead(notificationId)
            loadTickets()
            loadNotifications()
        }
    }

    /**
     * Lottery verloren: Cashback-Betrag dem User-Guthaben gutschreiben + Notification als gelesen markieren.
     */
    fun claimCashback(notificationId: Int, amount: Double) {
        viewModelScope.launch {
            val user = userDao.getById(_currentUserId.value) ?: return@launch
            userDao.update(user.copy(credits = user.credits + amount))
            db.notificationDao().markRead(notificationId)
            loadUser()
            loadNotifications()
        }
    }

    // ── Zahlungsmethoden ──────────────────────────────────────────────────────

    /** Fügt eine neue Zahlungsmethode für den aktuellen User hinzu */
    fun addPaymentMethod(name: String, type: String, emoji: String) {
        viewModelScope.launch {
            db.paymentMethodDao().insert(
                PaymentMethodEntity(
                    userId     = _currentUserId.value,
                    name       = name,
                    type       = type,
                    emoji      = emoji,
                    isVerified = false
                )
            )
            loadPaymentMethods()
        }
    }

    /** Löscht eine Zahlungsmethode */
    fun removePaymentMethod(id: Int) {
        viewModelScope.launch {
            db.paymentMethodDao().deleteById(id)
            loadPaymentMethods()
        }
    }

    // ── Lotterie: Anzahl Einträge pro Event ──────────────────────────────────

    /**
     * Gibt die Gesamtzahl der Lotterie-Einträge für ein Event zurück.
     * Wird im LotteryScreen angezeigt ("X total entries").
     * Da wir keine echten Teilnehmerdaten haben, wird die seeded Anzahl
     * aus der lokalen DB zurückgegeben (nur eigene Einträge in Demo).
     * In einer echten App käme dieser Wert vom Server.
     */
    suspend fun getLotteryEntryCount(eventId: Int): Int =
        lotteryDao.countByEvent(eventId)

    // ── Profil aktualisieren ──────────────────────────────────────────────────

    /**
     * Speichert das geänderte User-Profil in die DB und lädt es danach neu.
     */
    fun updateUser(user: UserEntity) {
        viewModelScope.launch {
            userDao.update(user)
            loadUser()
        }
    }

    /**
     * Erstellt einen neuen User in der DB und loggt ihn direkt ein.
     * Passwort und E-Mail werden nur als SHA-256-Hash gespeichert — niemals im Klartext.
     * Wird aus dem CreateAccountScreen aufgerufen.
     */
    fun createUser(
        displayName: String,
        email: String,
        phone: String,
        location: String,
        userType: String,
        password: String = "",
        onDone: () -> Unit
    ) {
        viewModelScope.launch {
            val year = java.time.LocalDate.now().year.toString()

            // E-Mail und Passwort hashen — Klartext kommt nie in die DB
            val emailHash    = if (email.isNotBlank()) sha256(email.trim().lowercase()) else ""
            val passwordHash = if (password.isNotBlank()) sha256(password) else ""

            val newId = userDao.insert(
                UserEntity(
                    name           = "",           // Sicherheitsregel: name bleibt leer
                    email          = "",           // Sicherheitsregel: email bleibt leer
                    userType       = userType,
                    credits        = 0.0,
                    currency       = "EUR",
                    displayName    = displayName,
                    phone          = phone,
                    location       = location,
                    memberSince    = year,
                    passwordHash   = passwordHash,
                    loginEmailHash = emailHash
                )
            ).toInt()
            _currentUserId.value = newId
            loadAll()
            onDone()
        }
    }

    /**
     * Speichert die gewählte Währung des Users (z.B. "EUR", "USD", "GBP", "AUD").
     * Alle Screens lesen currency aus currentUser → aktualisiert sich automatisch.
     */
    fun updateCurrency(currencyCode: String) {
        viewModelScope.launch {
            val user = userDao.getById(_currentUserId.value) ?: return@launch
            userDao.update(user.copy(currency = currencyCode))
            loadUser()
        }
    }

    /**
     * Speichert Profil-Felder (displayName, phone, location) ohne Email/Name zu überschreiben.
     */
    fun updateProfile(displayName: String, phone: String, location: String) {
        viewModelScope.launch {
            val current = userDao.getById(_currentUserId.value) ?: return@launch
            userDao.update(
                current.copy(
                    displayName = displayName,
                    phone       = phone,
                    location    = location
                )
            )
            loadUser()
        }
    }

    // ── Private Mapping-Funktionen ────────────────────────────────────────────

    /**
     * Wandelt ein EventEntity (DB-Objekt) in ein Event (UI-Modell) um.
     *
     * Dabei wird die Adresse aus der DB geholt und zu einem lesbaren
     * Ortsstring zusammengebaut, z.B. "Berlin, Germany".
     * "ticketsLeft" wird aus capacity − ticketsSold berechnet.
     */
    private suspend fun EventEntity.toUiModel(): Event {
        val adresse     = eventDao.getAdresseById(adresseId)
        val location    = adresse?.let { "${it.city}, ${it.country}" } ?: ""
        val ticketsLeft = (capacity - ticketsSold).coerceAtLeast(0)
        val dateStr     = dateStart.format(DateTimeFormatter.ofPattern("MMM d, yyyy"))

        return Event(
            id          = id,
            title       = title,
            location    = location,
            date        = dateStr,
            price       = price,
            category    = category,
            hasLottery  = hasLottery,
            imageColor  = imageColor,
            description = description,
            organizer   = organizer,
            ticketsLeft = ticketsLeft,
            ticketsSold = ticketsSold
        )
    }

    /**
     * Wandelt ein TicketEntity (DB-Objekt) in ein Ticket (UI-Modell) um.
     * Hier ist kein DB-Join nötig — die Anzeige-Felder sind direkt im Entity gespeichert.
     */
    private fun TicketEntity.toUiModel() = Ticket(
        id            = id,
        eventId       = eventId,
        eventTitle    = eventTitle,
        eventDate     = eventDate,
        eventLocation = eventLocation,
        seat          = seat,
        qrCode        = qrCode,
        section       = section,
        price         = price,
        status        = status
    )
}
