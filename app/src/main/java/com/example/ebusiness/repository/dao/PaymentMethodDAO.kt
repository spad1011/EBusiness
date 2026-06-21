package com.example.ebusiness.repository.dao

import androidx.room.*
import com.example.ebusiness.entities.PaymentMethodEntity

/**
 * Datenbankzugriff für gespeicherte Zahlungsmethoden.
 *
 * Jede Zahlungsmethode gehört zu einem User (userId).
 */
@Dao
interface PaymentMethodDAO {

    /** Alle Zahlungsmethoden eines Users */
    @Query("SELECT * FROM payment_methods WHERE userId = :userId ORDER BY id ASC")
    suspend fun getByUser(userId: Int): List<PaymentMethodEntity>

    /** Neue Zahlungsmethode speichern */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(method: PaymentMethodEntity): Long

    /** Mehrere auf einmal — für den Seeder */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(methods: List<PaymentMethodEntity>)

    /** Zahlungsmethode löschen */
    @Query("DELETE FROM payment_methods WHERE id = :id")
    suspend fun deleteById(id: Int)
}
