package com.example.ebusiness.repository

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.ebusiness.entities.Adresse
import com.example.ebusiness.entities.Event
import com.example.ebusiness.repository.dao.TicketEventDAO

@Database(entities = [Event::class, Adresse::class], version = 1, exportSchema = false)
@TypeConverters(Converters::class)
abstract class TicketEventDatabase : RoomDatabase() {
    abstract fun ticketEventDao(): TicketEventDAO

    companion object {
        @Volatile
        private var INSTANCE: TicketEventDatabase? = null

        fun getDatabase(context: Context): TicketEventDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    TicketEventDatabase::class.java,
                    "ticket_event_database"
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }
}
