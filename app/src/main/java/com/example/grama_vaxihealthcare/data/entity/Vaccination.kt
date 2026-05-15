package com.example.grama_vaxihealthcare.data.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "vaccinations",
    foreignKeys = [
        ForeignKey(
            entity = Animal::class,
            parentColumns = ["id"],
            childColumns = ["animalId"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [Index("animalId")]
)
data class Vaccination(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val animalId: Long,
    val vaccineName: String,
    val dateAdministered: Long,
    val nextDueDate: Long
)
