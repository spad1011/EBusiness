package com.example.ebusiness.repository.dao

import androidx.room.*
import com.example.ebusiness.entities.NotificationEntity

/**
 * Datenbankzugriff für Benachrichtigungen.
 *
 * Jede Notification gehört zu einem User (userId).
 * Im AlertsScreen werden nur die Notifications des eingeloggten Users gezeigt.
 */
@Dao
interface NotificationDAO {

    /** Alle Notifications eines Users, neueste zuerst */
    @Query("SELECT * FROM notifications WHERE userId = :userId ORDER BY id DESC")
    suspend fun getByUser(userId: Int): List<NotificationEntity>

    /** Anzahl ungelesener Notifications (für den Badge in der NavBar) */
    @Query("SELECT COUNT(*) FROM notifications WHERE userId = :userId AND isRead = 0")
    suspend fun countUnread(userId: Int): Int

    /** Neue Notification einfügen */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(notification: NotificationEntity): Long

    /** Mehrere auf einmal — für den Seeder */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(notifications: List<NotificationEntity>)

    /** Notification als gelesen markieren */
    @Query("UPDATE notifications SET isRead = 1 WHERE id = :id")
    suspend fun markRead(id: Int)

    /** Alle Notifications eines Users als gelesen markieren */
    @Query("UPDATE notifications SET isRead = 1 WHERE userId = :userId")
    suspend fun markAllRead(userId: Int)

    /** Einzelne Notification löschen (Dismiss) */
    @Query("DELETE FROM notifications WHERE id = :id")
    suspend fun deleteById(id: Int)
}
