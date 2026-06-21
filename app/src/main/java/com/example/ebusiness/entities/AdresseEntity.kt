package com.example.ebusiness.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * Speichert den Veranstaltungsort eines Events.
 *
 * Ausgelagert aus EventEntity damit wir für mehrere Events
 * dieselbe Adresse verwenden können (z.B. zwei Konzerte in Berlin).
 * In der UI wird daraus ein lesbarer String gebaut: "Berlin, Germany".
 */
@Entity(tableName = "adresse")
data class AdresseEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,

    /** Straßenname — optional, bei den meisten Events nicht ausgefüllt */
    val streetName: String = "",

    /** Hausnummer — optional */
    val streetNumber: String = "",

    /** Postleitzahl */
    val postalCode: String = "",

    /** Stadt, z.B. "Berlin" oder "Munich" */
    val city: String = "",

    /** Land, z.B. "Germany" */
    val country: String = ""
)
