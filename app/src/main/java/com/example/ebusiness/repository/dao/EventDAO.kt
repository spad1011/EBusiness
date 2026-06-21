package com.example.ebusiness.repository.dao

import androidx.room.*
import com.example.ebusiness.entities.AdresseEntity
import com.example.ebusiness.entities.EventEntity
import kotlinx.coroutines.flow.Flow

/**
 * Datenbankzugriff für Events.
 *
 * Hier stehen alle SQL-Abfragen die mit der "events"-Tabelle zu tun haben.
 * Room generiert daraus automatisch den echten Code — wir schreiben
 * nur die Interfaces und Annotationen.
 */
@Dao
interface EventDAO {

    // ── Lese-Abfragen ─────────────────────────────────────────────────────────

    /**
     * Liefert alle Events als Flow — d.h. wenn sich die DB ändert,
     * aktualisiert sich die UI automatisch. Sortiert nach Startdatum.
     */
    @Query("SELECT * FROM events ORDER BY dateStart ASC")
    fun getAllFlow(): Flow<List<EventEntity>>

    /**
     * Einmalige Abfrage aller Events — für den ersten Ladevorgang.
     * Im Gegensatz zu getAllFlow() gibt es hier keine automatischen Updates.
     */
    @Query("SELECT * FROM events ORDER BY dateStart ASC")
    suspend fun getAll(): List<EventEntity>

    /**
     * Holt ein einzelnes Event anhand seiner ID.
     * Gibt null zurück wenn kein Event mit dieser ID existiert.
     */
    @Query("SELECT * FROM events WHERE id = :id")
    suspend fun getById(id: Int): EventEntity?

    /**
     * Volltextsuche über Titel und Beschreibung, optional mit Kategorie-Filter.
     *
     * Beispiele:
     * - search("rock", null)       → alle Events mit "rock" im Titel/Beschreibung
     * - search(null, "Music")      → alle Musik-Events
     * - search("berlin", "Sports") → Sport-Events in Berlin
     *
     * :query und :category können null sein — dann wird der jeweilige Filter ignoriert.
     */
    @Query("""
        SELECT * FROM events
        WHERE (:query IS NULL OR title LIKE '%' || :query || '%'
                              OR description LIKE '%' || :query || '%')
          AND (:category IS NULL OR category = :category)
        ORDER BY dateStart ASC
    """)
    suspend fun search(query: String?, category: String?): List<EventEntity>

    /**
     * Alle Events einer bestimmten Kategorie, z.B. alle "Music"-Events.
     */
    @Query("SELECT * FROM events WHERE category = :category ORDER BY dateStart ASC")
    suspend fun getByCategory(category: String): List<EventEntity>

    /**
     * Holt die Adresse eines Events — wird beim Mapping Entity → UI-Modell gebraucht
     * um den Ortsstring ("Berlin, Germany") zusammenzubauen.
     */
    @Query("SELECT * FROM adresse WHERE id = :id")
    suspend fun getAdresseById(id: Int): AdresseEntity?

    // ── Schreib-Operationen ───────────────────────────────────────────────────

    /**
     * Fügt ein neues Event ein, oder überschreibt es wenn die ID schon existiert.
     * Gibt die neue Datenbank-ID zurück (wichtig beim Seeding mit fester ID).
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(event: EventEntity): Long

    /**
     * Fügt mehrere Events auf einmal ein — effizienter als einzeln.
     * Wird beim Seeding benutzt.
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(events: List<EventEntity>)

    /**
     * Aktualisiert ein bestehendes Event (z.B. wenn der Host es bearbeitet).
     */
    @Update
    suspend fun update(event: EventEntity)

    /**
     * Erhöht den Verkaufszähler um 1 wenn jemand ein Ticket kauft.
     * Wird direkt nach einem Ticket-Kauf aufgerufen.
     */
    @Query("UPDATE events SET ticketsSold = ticketsSold + 1 WHERE id = :id")
    suspend fun incrementTicketsSold(id: Int)

    /**
     * Löscht ein Event-Objekt aus der DB.
     */
    @Delete
    suspend fun delete(event: EventEntity)

    /**
     * Löscht ein Event anhand seiner ID.
     * Gibt zurück wie viele Zeilen gelöscht wurden (0 = nicht gefunden, 1 = Erfolg).
     */
    @Query("DELETE FROM events WHERE id = :id")
    suspend fun deleteById(id: Int): Int

    /**
     * Zählt wie viele Events in der DB sind.
     * Wird vom DatabaseSeeder genutzt um zu prüfen ob schon Daten vorhanden sind.
     */
    @Query("SELECT COUNT(*) FROM events")
    suspend fun count(): Int
}
