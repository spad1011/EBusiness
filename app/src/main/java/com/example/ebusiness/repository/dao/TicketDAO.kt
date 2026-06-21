package com.example.ebusiness.repository.dao

import androidx.room.*
import com.example.ebusiness.entities.TicketEntity
import kotlinx.coroutines.flow.Flow

/**
 * Datenbankzugriff für Tickets.
 *
 * Alle Abfragen beziehen sich auf einen bestimmten User (userId),
 * damit jeder User nur seine eigenen Tickets sieht.
 */
@Dao
interface TicketDAO {

    /**
     * Alle Tickets eines Users als Flow — aktualisiert sich automatisch
     * wenn ein neues Ticket gekauft wird. Für den Tickets-Tab.
     */
    @Query("SELECT * FROM tickets WHERE userId = :userId ORDER BY purchaseDate DESC")
    fun getByUserFlow(userId: Int): Flow<List<TicketEntity>>

    /**
     * Einmalige Abfrage der Tickets eines Users.
     * Wird nach dem Login und nach einem Ticket-Kauf aufgerufen.
     */
    @Query("SELECT * FROM tickets WHERE userId = :userId ORDER BY purchaseDate DESC")
    suspend fun getByUser(userId: Int): List<TicketEntity>

    /**
     * Ein einzelnes Ticket anhand seiner ID holen.
     * Für den Ticket-Detail-Screen wenn der User auf ein Ticket klickt.
     */
    @Query("SELECT * FROM tickets WHERE id = :id")
    suspend fun getById(id: Int): TicketEntity?

    /**
     * Tickets eines Users gefiltert nach Status.
     * Nützlich wenn wir nur "Active" oder nur "Past" Tickets brauchen.
     * Beispiel: getByStatus(1, "Active") → alle gültigen Tickets von User 1
     */
    @Query("SELECT * FROM tickets WHERE userId = :userId AND status = :status")
    suspend fun getByStatus(userId: Int, status: String): List<TicketEntity>

    /**
     * Neues Ticket in die DB schreiben.
     * Gibt die generierte ID zurück — die brauchen wir um das Ticket
     * sofort in der UI anzeigen zu können.
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(ticket: TicketEntity): Long

    /**
     * Mehrere Tickets auf einmal einfügen — für den Seeder.
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(tickets: List<TicketEntity>)

    /**
     * Ganzes Ticket-Objekt aktualisieren (z.B. wenn sich der Status ändert).
     */
    @Update
    suspend fun update(ticket: TicketEntity)

    /**
     * Nur den Status eines Tickets ändern ohne das ganze Objekt zu laden.
     * Effizienter als update() wenn sich nur der Status ändert.
     */
    @Query("UPDATE tickets SET status = :status WHERE id = :id")
    suspend fun updateStatus(id: Int, status: String)

    /**
     * Ticket löschen — z.B. wenn der User ein Ticket storniert.
     */
    @Delete
    suspend fun delete(ticket: TicketEntity)

    /**
     * Wie viele Tickets hat ein User insgesamt?
     * Wird für den Badge-Zähler in der Navigation genutzt.
     */
    @Query("SELECT COUNT(*) FROM tickets WHERE userId = :userId")
    suspend fun countByUser(userId: Int): Int
}
