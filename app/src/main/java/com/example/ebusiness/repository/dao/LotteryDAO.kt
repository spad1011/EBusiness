package com.example.ebusiness.repository.dao

import androidx.room.*
import com.example.ebusiness.entities.LotteryEntryEntity
import kotlinx.coroutines.flow.Flow

/**
 * Datenbankzugriff für Lotterie-Teilnahmen.
 *
 * Jede Teilnahme ist ein Eintrag in der "lottery_entries"-Tabelle.
 * Ein User kann pro Event nur einmal teilnehmen — das wird in getEntry() geprüft.
 */
@Dao
interface LotteryDAO {

    /**
     * Alle Lotterie-Einträge eines Users als Flow — für den Alerts-Tab.
     * Neueste zuerst, damit man den letzten Eintrag sofort sieht.
     */
    @Query("SELECT * FROM lottery_entries WHERE userId = :userId ORDER BY entryDate DESC")
    fun getByUserFlow(userId: Int): Flow<List<LotteryEntryEntity>>

    /**
     * Alle Lotterie-Einträge eines Users — einmalige Abfrage.
     * Wird beim Login und nach einer neuen Teilnahme aufgerufen.
     */
    @Query("SELECT * FROM lottery_entries WHERE userId = :userId ORDER BY entryDate DESC")
    suspend fun getByUser(userId: Int): List<LotteryEntryEntity>

    /**
     * Prüft ob ein User schon für ein bestimmtes Event in der Lotterie ist.
     * Gibt null zurück wenn noch keine Teilnahme vorhanden ist.
     *
     * Wird im ViewModel aufgerufen bevor ein neuer Eintrag angelegt wird —
     * so verhindern wir doppelte Teilnahmen.
     */
    @Query("SELECT * FROM lottery_entries WHERE eventId = :eventId AND userId = :userId LIMIT 1")
    suspend fun getEntry(eventId: Int, userId: Int): LotteryEntryEntity?

    /**
     * Neue Lotterie-Teilnahme speichern.
     * REPLACE überschreibt falls irgendwie doch ein Duplikat entstehen sollte.
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(entry: LotteryEntryEntity): Long

    /**
     * Mehrere Einträge auf einmal — für den Seeder.
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(entries: List<LotteryEntryEntity>)

    /**
     * Status eines Eintrags ändern: "Pending" → "Won" oder "Lost".
     * Würde in einer echten App vom Server aufgerufen werden wenn die
     * Lotterie-Ziehung stattfindet.
     */
    @Query("UPDATE lottery_entries SET status = :status WHERE id = :id")
    suspend fun updateStatus(id: Int, status: String)

    /**
     * Wie viele offene Lotterie-Teilnahmen hat ein User?
     * Wird für den Benachrichtigungs-Badge genutzt.
     */
    @Query("SELECT COUNT(*) FROM lottery_entries WHERE userId = :userId AND status = 'Pending'")
    suspend fun countPending(userId: Int): Int

    @Query("SELECT COUNT(*) FROM lottery_entries WHERE eventId = :eventId")
    suspend fun countByEvent(eventId: Int): Int
}
