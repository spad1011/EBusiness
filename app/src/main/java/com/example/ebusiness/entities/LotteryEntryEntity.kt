package com.example.ebusiness.entities

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import java.time.LocalDate

/**
 * Eine Lotterie-Teilnahme: ein User hat sich für ein Event beworben.
 *
 * So funktioniert das StagePot-Lotteriesystem:
 * 1. User kauft für 1,09€ pro Los einen Eintrag (ticketsPaid Lose möglich)
 * 2. Nach dem Ziehungsdatum bekommt der Status "Won" oder "Lost"
 * 3. Bei "Won" → normales Ticket wird erstellt
 * 4. Bei "Lost" → 25% des bezahlten Betrags als Credits zurück
 *
 * Ein User kann pro Event nur eine LotteryEntry haben
 * (wird im DAO und ViewModel geprüft).
 *
 * Beziehungen:
 * - Gehört zu einem Event (CASCADE: wenn Event weg, Entry auch weg)
 * - Gehört zu einem User (CASCADE: wenn User weg, Entry auch weg)
 */
@Entity(
    tableName = "lottery_entries",
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
    indices = [Index("eventId"), Index("userId")]
)
data class LotteryEntryEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,

    /** Das Event für das der User in der Lotterie ist */
    val eventId: Int = 0,

    /** Welcher User teilnimmt */
    val userId: Int = 1,

    /** Wann der User sich eingetragen hat */
    val entryDate: LocalDate = LocalDate.now(),

    /**
     * Aktueller Lotterie-Status:
     * "Pending" → Ziehung noch nicht stattgefunden
     * "Won"     → Gewonnen! Ticket wurde zugeteilt
     * "Lost"    → Nicht gewonnen, Cashback wurde gutgeschrieben
     */
    val status: String = "Pending",

    /** Anzahl der gekauften Lose (mehr Lose = höhere Gewinnchance) */
    val ticketsPaid: Int = 1,

    /** Gesamtbetrag den der User für die Lose bezahlt hat (ticketsPaid × 1,09€) */
    val amountPaid: Double = 1.09
)
