package com.example.ebusiness.repository.dao

import androidx.room.*
import com.example.ebusiness.entities.Event
import com.example.ebusiness.entities.EventLite

@Dao
interface TicketEventDAO {

    @Query("""
        SELECT events.id, events.name, events.eventDateStart, events.eventDateEnd, 
               events.description, adresse.id as adresse_id, adresse.streetName as adresse_streetName, 
               adresse.streetNumber as adresse_streetNumber, adresse.postalCode as adresse_postalCode, 
               adresse.country as adresse_country
        FROM events
        INNER JOIN adresse ON events.adresseId = adresse.id
    """)
    suspend fun getAllLite(): List<EventLite>

    @Query("""
        SELECT events.id, events.name, events.eventDateStart, events.eventDateEnd, 
               events.description, adresse.id as adresse_id, adresse.streetName as adresse_streetName, 
               adresse.streetNumber as adresse_streetNumber, adresse.postalCode as adresse_postalCode, 
               adresse.country as adresse_country
        FROM events
        INNER JOIN adresse ON events.adresseId = adresse.id
        WHERE (:name IS NULL OR events.name LIKE '%' || :name || '%')
    """)
    suspend fun getAllLiteFilterByName(name: String?): List<EventLite>

    @Query("SELECT * FROM events WHERE id = :id")
    suspend fun getEventById(id: Int): Event?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(event: Event): Long

    @Query("DELETE FROM events WHERE id = :id")
    suspend fun deleteEventById(id: Int): Int
}
