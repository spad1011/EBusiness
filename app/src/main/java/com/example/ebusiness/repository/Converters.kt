package com.example.ebusiness.repository

import androidx.room.TypeConverter
import java.time.LocalDate
import java.time.format.DateTimeFormatter

/**
 * TypeConverters für Room.
 *
 * SQLite kennt nur einfache Datentypen: TEXT, INTEGER, REAL, BLOB.
 * Kotlin-Typen wie LocalDate kann Room nicht direkt speichern.
 *
 * Diese Klasse sagt Room: "Wenn du ein LocalDate speichern willst,
 * ruf dateToString() auf. Wenn du es wieder lesen willst, ruf stringToDate() auf."
 *
 * Wir verwenden das ISO-Format: "2026-08-15" → einfach zu lesen und zu sortieren.
 */
class Converters {

    // ISO-Format: YYYY-MM-DD — international eindeutig und sortierbar
    private val formatter = DateTimeFormatter.ISO_LOCAL_DATE

    /**
     * Liest einen gespeicherten Datums-String aus der DB und wandelt ihn
     * zurück in ein LocalDate-Objekt.
     *
     * Gibt null zurück wenn der gespeicherte Wert null ist
     * (z.B. wenn das Feld optional ist).
     */
    @TypeConverter
    fun fromString(value: String?): LocalDate? =
        value?.let { LocalDate.parse(it, formatter) }

    /**
     * Wandelt ein LocalDate in einen String um bevor es in die DB geschrieben wird.
     * Beispiel: LocalDate.of(2026, 8, 15) → "2026-08-15"
     */
    @TypeConverter
    fun localDateToString(date: LocalDate?): String? =
        date?.format(formatter)
}
