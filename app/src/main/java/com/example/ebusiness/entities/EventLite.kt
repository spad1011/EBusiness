package com.example.ebusiness.entities

import androidx.room.Embedded
import java.time.LocalDate

data class EventLite (
    val id: Int,
    val name: String,
    val eventDateStart: LocalDate,
    val eventDateEnd: LocalDate,
    @Embedded(prefix = "adresse_")
    val eventAddress: Adresse,
    val description: String
)
