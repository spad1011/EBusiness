package com.example.ebusiness.repository.dao

import androidx.room.*
import com.example.ebusiness.entities.AdresseEntity

/**
 * Datenbankzugriff für Adressen (Veranstaltungsorte).
 *
 * Meistens werden Adressen über das EventDAO geladen (JOIN),
 * aber manchmal brauchen wir direkten Zugriff — z.B. wenn ein
 * Host einen neuen Veranstaltungsort anlegen will.
 */
@Dao
interface AdresseDAO {

    /**
     * Speichert eine neue Adresse und gibt die generierte ID zurück.
     * Die ID brauchen wir direkt danach um das Event damit zu verknüpfen.
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(adresse: AdresseEntity): Long

    /**
     * Fügt mehrere Adressen auf einmal ein — wird beim Seeding genutzt.
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(adressen: List<AdresseEntity>)

    /**
     * Holt eine Adresse anhand ihrer ID.
     * Gibt null zurück wenn die ID nicht existiert.
     */
    @Query("SELECT * FROM adresse WHERE id = :id")
    suspend fun getById(id: Int): AdresseEntity?

    /**
     * Alle gespeicherten Adressen — z.B. für ein Dropdown im "Event erstellen"-Dialog.
     */
    @Query("SELECT * FROM adresse")
    suspend fun getAll(): List<AdresseEntity>

    /**
     * Löscht eine Adresse. Achtung: zugehörige Events werden durch
     * CASCADE automatisch mitgelöscht (Foreign Key in EventEntity).
     */
    @Delete
    suspend fun delete(adresse: AdresseEntity)

    /**
     * Wie viele Adressen gibt es? Wird beim Seeding-Check genutzt.
     */
    @Query("SELECT COUNT(*) FROM adresse")
    suspend fun count(): Int
}
