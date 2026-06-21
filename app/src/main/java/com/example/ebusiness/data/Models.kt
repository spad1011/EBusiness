package com.example.ebusiness.data

// UI-Datenmodelle — flache Kotlin-Data-Classes ohne Room-Annotationen.
// Das ViewModel mappt die Entities auf diese Modelle bevor es sie an die Screens weitergibt.

/**
 * UI-Modell für ein Event.
 * Wird vom ViewModel aus EventEntity gebaut und an die Screens übergeben.
 * Alle Geldbeträge sind in EUR gespeichert — Umrechnung erfolgt per formatPrice().
 */
data class Event(
    val id: Int,
    val title: String,
    val location: String,
    val date: String,
    val price: Double,
    val category: String,
    val hasLottery: Boolean,
    val imageColor: Long,
    val description: String,
    val organizer: String = "Event Organizer",
    val ticketsLeft: Int = 100,
    val ticketsSold: Int = 0
)

/**
 * UI-Modell für ein Ticket.
 * Wird vom ViewModel aus TicketEntity gebaut und an die Screens übergeben.
 */
data class Ticket(
    val id: Int,
    val eventId: Int,
    val eventTitle: String,
    val eventDate: String,
    val eventLocation: String,
    val seat: String,
    val qrCode: String,
    val section: String = "A",
    val price: Double = 0.0,
    val status: String = "Active"   // "Active", "Past", "Lottery"
)
