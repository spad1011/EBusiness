package com.example.ebusiness.entities

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import java.time.LocalDate

/**
 * Repräsentiert ein Event in der Datenbank.
 *
 * Enthält alles was man über eine Veranstaltung wissen muss:
 * Titel, Beschreibung, Kategorie, Preis, Datum und wie viele Tickets
 * noch übrig sind. Die Adresse ist ausgelagert (adresseId → AdresseEntity),
 * damit wir denselben Ort für mehrere Events nutzen können.
 *
 * Beziehung: Ein Event gehört zu genau einer Adresse.
 * Wenn die Adresse gelöscht wird, verschwindet das Event auch (CASCADE).
 */
@Entity(
    tableName = "events",
    foreignKeys = [
        ForeignKey(
            entity        = AdresseEntity::class,
            parentColumns = ["id"],
            childColumns  = ["adresseId"],
            onDelete      = ForeignKey.CASCADE
        )
    ],
    indices = [Index("adresseId")]  // Index damit JOIN-Abfragen schnell sind
)
data class EventEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,

    /** Verweis auf den Veranstaltungsort (AdresseEntity) */
    val adresseId: Int = 0,

    // ── Grundinformationen ────────────────────────────────────────────────────

    /** Haupttitel des Events, z.B. "Rock am Ring" */
    val title: String = "",

    /** Kurzer Untertitel oder Teaser-Zeile */
    val subTitle: String = "",

    /** Ausführliche Beschreibung die im EventDetail-Screen angezeigt wird */
    val description: String = "",

    /** Name des Veranstalters, z.B. "LiveNation Events" */
    val organizer: String = "",

    /** Kategorie für den Filter: Music, Sports, Comedy, Conference, Theater, … */
    val category: String = "",

    // ── Ticket-Optionen ───────────────────────────────────────────────────────

    /** Normaler Ticketpreis in Euro */
    val price: Double = 0.0,

    /** Maximale Anzahl Besucher (Saalkapazität) */
    val capacity: Int = 0,

    /** Wie viele Tickets schon verkauft wurden — ticketsLeft = capacity - ticketsSold */
    val ticketsSold: Int = 0,

    /** Gibt es eine Lotterie für dieses Event? */
    val hasLottery: Boolean = false,

    /** Preis pro Lotterie-Los (Standard: 1,09 €) */
    val lotteryPrice: Double = 1.09,

    // ── Darstellung ───────────────────────────────────────────────────────────

    /**
     * Hintergrundfarbe der Event-Karte als ARGB-Long, z.B. 0xFF6650A4.
     * Wir haben kein echtes Bild-Upload, daher nehmen wir Farben als Placeholder.
     */
    val imageColor: Long = 0xFF6650A4,

    // ── Zeitraum ──────────────────────────────────────────────────────────────

    /** Startdatum (bei eintägigen Events = Enddatum) */
    val dateStart: LocalDate = LocalDate.now(),

    /** Enddatum bei mehrtägigen Festivals */
    val dateEnd: LocalDate = LocalDate.now()
)
