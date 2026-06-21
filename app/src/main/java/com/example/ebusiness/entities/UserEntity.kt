package com.example.ebusiness.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * Lokales User-Profil — wird in der DB gespeichert damit Einstellungen
 * auch nach einem Neustart der App erhalten bleiben.
 *
 * Wichtig: Hier werden KEINE sensiblen oder persönlichen Daten
 * ohne Zustimmung des Users gespeichert. Name und E-Mail sind
 * beim ersten Start leer und werden nur gefüllt wenn der User
 * sein Profil manuell ausfüllt.
 *
 * Wir haben zwei feste User-IDs: 1 = Fan, 2 = Host.
 * In einer echten App würde man das natürlich über Auth lösen.
 */
@Entity(tableName = "users")
data class UserEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,

    /**
     * Anzeigename — bleibt leer bis der User sein Profil ausfüllt.
     * In der UI zeigen wir dann "Profil einrichten" als Hinweis.
     */
    val name: String = "",

    /**
     * E-Mail-Adresse — wird nur gespeichert wenn der User sie selbst eingibt.
     * Wird nicht aus dem System oder anderen Quellen übernommen.
     */
    val email: String = "",

    /**
     * Nutzertyp: "fan" für normale Benutzer, "host" für Veranstalter.
     * Bestimmt welche Tabs und Funktionen sichtbar sind.
     */
    val userType: String = "fan",

    /**
     * StagePot-Guthaben in Euro.
     * Wird bei Lotterie-Verlust als 25%-Cashback gutgeschrieben
     * und kann für zukünftige Lotterie-Teilnahmen genutzt werden.
     */
    val credits: Double = 0.0,

    /**
     * Bevorzugte Währung für die Anzeige, z.B. "EUR" oder "USD".
     * Wechselkurs-Umrechnung ist nicht implementiert — nur für die Darstellung.
     */
    val currency: String = "EUR",

    /**
     * URL zu einem Profilbild — optional.
     * Wenn leer, werden Initialen als Fallback angezeigt.
     */
    val avatarUrl: String = "",

    /**
     * Anzeigename in der UI — darf ein Pseudonym oder Nickname sein.
     * Wird im ProfileScreen und EditProfileScreen angezeigt.
     * Bleibt leer bis der User sein Profil ausfüllt.
     */
    val displayName: String = "",

    /**
     * Optionale Telefonnummer — wird nur gespeichert wenn der User sie eingibt.
     */
    val phone: String = "",

    /**
     * Optionaler Wohnort / Standort, z.B. "Berlin, Germany".
     */
    val location: String = "",

    /**
     * Jahr seit dem der User Mitglied ist, z.B. "2024".
     * Wird im ProfileScreen als "Member since …" angezeigt.
     */
    val memberSince: String = "2026"
)
