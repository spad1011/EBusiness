package com.example.ebusiness.repository.dao

import androidx.room.*
import com.example.ebusiness.entities.UserEntity
import kotlinx.coroutines.flow.Flow

/**
 * Datenbankzugriff für User-Profile.
 *
 * Wir haben nur zwei feste User (ID 1 = Fan, ID 2 = Host),
 * aber das DAO ist so gebaut dass es auch für mehrere User funktionieren würde.
 */
@Dao
interface UserDAO {

    /**
     * Profil eines Users anhand seiner ID holen.
     * Gibt null zurück wenn der User nicht existiert — das sollte nie passieren,
     * aber wir behandeln es sicherheitshalber.
     */
    @Query("SELECT * FROM users WHERE id = :id")
    suspend fun getById(id: Int): UserEntity?

    /**
     * User-Profil als Flow — aktualisiert sich automatisch wenn der User
     * sein Profil ändert oder Credits gutgeschrieben werden.
     */
    @Query("SELECT * FROM users WHERE id = :id")
    fun getByIdFlow(id: Int): Flow<UserEntity?>

    /**
     * User anhand seiner E-Mail-Adresse suchen.
     * Nützlich für spätere Login-Logik, falls echte Auth eingebaut wird.
     * Gibt nur den ersten Treffer zurück (LIMIT 1).
     */
    @Query("SELECT * FROM users WHERE email = :email LIMIT 1")
    suspend fun getByEmail(email: String): UserEntity?

    /**
     * Neuen User anlegen oder bestehenden überschreiben.
     * Gibt die generierte ID zurück.
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(user: UserEntity): Long

    /**
     * Ganzes User-Profil aktualisieren — z.B. nach dem Speichern im EditProfile-Screen.
     */
    @Update
    suspend fun update(user: UserEntity)

    /**
     * Credits auf einem User-Konto erhöhen.
     * Wird aufgerufen wenn der User eine Lotterie verliert (25% Cashback).
     * Direktes SQL ist effizienter als Laden + Ändern + Speichern.
     */
    @Query("UPDATE users SET credits = credits + :amount WHERE id = :id")
    suspend fun addCredits(id: Int, amount: Double)

    /**
     * Credits abziehen — aber nur wenn genug vorhanden sind.
     * Gibt zurück wie viele Zeilen geändert wurden:
     * 1 = Erfolg, 0 = nicht genug Credits (Abzug verhindert).
     */
    @Query("UPDATE users SET credits = credits - :amount WHERE id = :id AND credits >= :amount")
    suspend fun deductCredits(id: Int, amount: Double): Int

    /**
     * Umschalten zwischen "fan" und "host" — für den Quick-Test-Login.
     */
    @Query("UPDATE users SET userType = :type WHERE id = :id")
    suspend fun updateUserType(id: Int, type: String)

    /**
     * Speichert die URL/den Pfad des Profilbilds für einen User.
     * Wird aufgerufen wenn der User ein neues Bild auswählt oder es löscht.
     * Leerer String bedeutet: kein Profilbild gesetzt.
     */
    @Query("UPDATE users SET avatarUrl = :url WHERE id = :id")
    suspend fun updateAvatarUrl(id: Int, url: String)

    /**
     * Wie viele User gibt es in der DB?
     * Wird beim Seeding geprüft damit wir keine Duplikate anlegen.
     */
    @Query("SELECT COUNT(*) FROM users")
    suspend fun count(): Int
}
