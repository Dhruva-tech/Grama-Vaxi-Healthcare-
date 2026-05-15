package com.example.grama_vaxihealthcare.data.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "animals")
data class Animal(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val name: String,
    val type: String, // Cow, Goat, Sheep, Buffalo
    val breed: String,
    val gender: String,
    val age: Int,
    val imageUrl: String? = null,
    val photoUri: String? = null,
    val lastVaccinationDate: Long?,
    val nextVaccinationDate: Long?,
    val healthNotes: String
)
