package com.example.ebusiness.entities

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import java.time.LocalDate

@Entity(
    tableName = "events",
    foreignKeys = [
        ForeignKey(
            entity = Adresse::class,
            parentColumns = ["id"],
            childColumns = ["adresseId"],
            onDelete = ForeignKey.CASCADE
        )
    ]
)
data class Event(
    @PrimaryKey(autoGenerate = true)
    var id: Int = 0,
    var adresseId: Int = 0,
    var name: String = "",
    var subname: String = "",
    var description: String = "",
    var eventDateStart: LocalDate,
    var eventDateEnd: LocalDate,
    var capacity: Int = 0
)
