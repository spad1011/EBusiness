package com.example.ebusiness.entities

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import java.time.LocalDate

/**
 * Ein Ticket das ein User für ein Event gekauft hat.
 *
 * Wir speichern Datum, Ort und Event-Titel direkt im Ticket (denormalisiert),
 * damit der Tickets-Screen keine komplizierten JOINs braucht —
 * einfach alle Tickets für userId laden, fertig.
 *
 * Status-Werte: "Active" (noch gültig), "Past" (Event vorbei), "Lottery" (Lotterie-Ticket)
 *
 * Beziehungen:
 * - Gehört zu einem Event (CASCADE: wenn Event weg, Ticket auch weg)
 * - Gehört zu einem User (CASCADE: wenn User weg, Ticket auch weg)
 */
@Entity(
    tableName = "tickets",
    foreignKeys = [
        ForeignKey(
            entity        = EventEntity::class,
            parentColumns = ["id"],
            childColumns  = ["eventId"],
            onDelete      = ForeignKey.CASCADE
        ),
        ForeignKey(
            entity        = UserEntity::class,
            parentColumns = ["id"],
            childColumns  = ["userId"],
            onDelete      = ForeignKey.CASCADE
        )
    ],
    indices = [Index("eventId"), Index("userId")]  // Schnelle Abfragen nach User oder Event
)
data class TicketEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,

    /** Welches Event dieses Ticket gilt */
    val eventId: Int = 0,

    /** Wem dieses Ticket gehört */
    val userId: Int = 1,

    // ── Anzeige-Felder (denormalisiert für schnellen Zugriff) ─────────────────

    /** Event-Titel — gespeichert damit wir kein JOIN brauchen */
    val eventTitle: String = "",

    /** Datum als String für die Anzeige, z.B. "Wed, Jul 15, 2026" */
    val eventDate: String = "",

    /** Ort als String, z.B. "Berlin, Germany" */
    val eventLocation: String = "",

    // ── Sitzplatz ─────────────────────────────────────────────────────────────

    /** Platznummer oder -bezeichnung */
    val seat: String = "",

    /** Block oder Bereich, z.B. "A", "B", "VIP" */
    val section: String = "A",

    // ── Preis und Status ──────────────────────────────────────────────────────

    /** Bezahlter Preis in Euro */
    val price: Double = 0.0,

    /**
     * Aktueller Status:
     * "Active"  → Event steht noch aus, Ticket ist gültig
     * "Past"    → Event ist vorbei
     * "Lottery" → Lotterie-Teilnahme (noch kein richtiges Ticket)
     */
    val status: String = "Active",

    /** QR-Code-String für das Einlass-Scanning */
    val qrCode: String = "",

    /** Wann das Ticket gekauft wurde */
    val purchaseDate: LocalDate = LocalDate.now()
)
