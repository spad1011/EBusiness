package com.example.ebusiness.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "adresse")
data class Adresse(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val streetName: String,
    val streetNumber: String,
    val postalCode: String,
    val country: String
)
