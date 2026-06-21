package com.example.ebusiness.data

// Währungs-Hilfsfunktionen: Symbol-Mapping, EUR→X Umrechnung und formatierte Ausgabe.

/** Returns the display symbol for a currency code stored in the DB (UserEntity.currency). */
fun currencySymbol(currency: String): String = when (currency.uppercase()) {
    "USD" -> "$"
    "GBP" -> "£"
    "AUD" -> "A$"
    "CHF" -> "CHF "
    "JPY" -> "¥"
    else  -> "€"   // EUR and anything unknown
}

/**
 * Konvertiert einen EUR-Betrag in die Ziel-Währung.
 * Alle Beträge werden intern in EUR gespeichert.
 * Kurse: ECB-Näherungswerte, für Demo-Zwecke fest hinterlegt.
 */
fun convertFromEur(amount: Double, currency: String): Double = when (currency.uppercase()) {
    "USD" -> amount * 1.08
    "GBP" -> amount * 0.85
    "AUD" -> amount * 1.65
    "CHF" -> amount * 0.96
    "JPY" -> amount * 160.0
    else  -> amount   // EUR — kein Umrechnen nötig
}

/**
 * Konvertiert einen EUR-Betrag und formatiert ihn mit Symbol, z.B. "A$82.50".
 * Sollte überall verwendet werden wo Geldbeträge angezeigt werden.
 */
fun formatPrice(amount: Double, currency: String): String {
    val converted = convertFromEur(amount, currency)
    return "${currencySymbol(currency)}${"%.2f".format(converted)}"
}
