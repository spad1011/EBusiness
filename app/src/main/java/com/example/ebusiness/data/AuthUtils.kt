package com.example.ebusiness.data

import java.security.MessageDigest

// Einfache Hashing-Hilfsfunktionen für die lokale Authentifizierung.
// Wir speichern niemals Klartext-Passwörter oder E-Mails in der DB —
// nur die SHA-256-Hashes davon.

/**
 * Berechnet den SHA-256 Hash eines Strings.
 * Wird für Passwörter und E-Mail-Hashes verwendet.
 */
fun sha256(input: String): String {
    val bytes = MessageDigest.getInstance("SHA-256").digest(input.toByteArray())
    return bytes.joinToString("") { "%02x".format(it) }
}
