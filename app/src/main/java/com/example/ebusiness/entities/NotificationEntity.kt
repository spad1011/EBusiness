package com.example.ebusiness.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * Eine App-interne Benachrichtigung für den User.
 *
 * Wird in der "notifications"-Tabelle gespeichert.
 * Typen: "lottery_win", "lottery_lose", "reminder", "offer", "credits"
 * Der Typ steuert welches Icon und welche Farbe im AlertsScreen gezeigt wird.
 */
@Entity(tableName = "notifications")
data class NotificationEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,

    /** Für welchen User diese Benachrichtigung ist (1 = Fan, 2 = Host) */
    val userId: Int,

    /** Bestimmt Icon + Farbe im UI */
    val type: String,  // "lottery_win" | "lottery_lose" | "reminder" | "offer" | "credits"

    /** Kurzer Titel, z.B. "Lottery Win! 🎉" */
    val title: String,

    /** Ausführliche Nachricht */
    val message: String,

    /** true = User hat die Notification gesehen/gelesen */
    val isRead: Boolean = false,

    /** Zeitstempel als String, z.B. "2026-05-20" */
    val createdAt: String = "2026-06-01",

    /** Optionaler Label für den Action-Button, z.B. "Claim Ticket" */
    val actionLabel: String? = null,

    /** Event-ID für lottery_win → Ticket wird für dieses Event angelegt */
    val eventId: Int? = null,

    /** Betrag für lottery_lose → wird dem User als Credits gutgeschrieben */
    val actionAmount: Double? = null
)
