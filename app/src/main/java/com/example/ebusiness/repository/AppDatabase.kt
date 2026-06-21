package com.example.ebusiness.repository

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.ebusiness.entities.*
import com.example.ebusiness.repository.dao.*

/**
 * Die zentrale Room-Datenbank der App.
 *
 * version = 4 → NotificationEntity: eventId + actionAmount für Claim-Logik.
 * fallbackToDestructiveMigration() → DB is wiped and re-seeded on version upgrade.
 * Für ein Lehrprojekt ist das in Ordnung; in einer echten App würde man
 * stattdessen explizite Migration-Skripte schreiben.
 */
@Database(
    entities = [
        EventEntity::class,
        AdresseEntity::class,
        TicketEntity::class,
        UserEntity::class,
        LotteryEntryEntity::class,
        NotificationEntity::class,
        PaymentMethodEntity::class
    ],
    version = 5,  // UserEntity: passwordHash + loginEmailHash für lokale Auth
    exportSchema = false
)
@TypeConverters(Converters::class)
abstract class AppDatabase : RoomDatabase() {

    abstract fun eventDao(): EventDAO
    abstract fun adresseDao(): AdresseDAO
    abstract fun ticketDao(): TicketDAO
    abstract fun userDao(): UserDAO
    abstract fun lotteryDao(): LotteryDAO
    abstract fun notificationDao(): NotificationDAO
    abstract fun paymentMethodDao(): PaymentMethodDAO

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getInstance(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "stagepot_database"
                )
                    // DB bei Versions-Upgrade neu aufbauen (Seeder befüllt sie wieder)
                    .fallbackToDestructiveMigration()
                    .build()
                    .also { INSTANCE = it }
            }
        }
    }
}
