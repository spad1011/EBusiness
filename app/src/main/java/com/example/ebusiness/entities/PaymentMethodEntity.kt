package com.example.ebusiness.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * Eine gespeicherte Zahlungsmethode eines Users.
 *
 * Wird in der "payment_methods"-Tabelle gespeichert.
 * Typen: "paypal", "card", "google_pay", "apple_pay"
 */
@Entity(tableName = "payment_methods")
data class PaymentMethodEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,

    /** Welchem User diese Zahlungsmethode gehört */
    val userId: Int,

    /** Anzeigename, z.B. "PayPal Business" oder "Visa •••• 4242" */
    val name: String,

    /** Typ für interne Logik: "paypal", "card", "google_pay", "apple_pay" */
    val type: String,

    /** Emoji-Icon für die UI */
    val emoji: String,

    /** true = Zahlungsmethode wurde verifiziert */
    val isVerified: Boolean = false
)
