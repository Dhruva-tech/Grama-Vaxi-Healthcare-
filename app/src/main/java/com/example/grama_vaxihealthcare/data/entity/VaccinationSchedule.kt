package com.example.grama_vaxihealthcare.data.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "vaccination_schedules",
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
data class VaccinationSchedule(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val animalId: Long,
    val vaccineName: String,
    val date: Long,
    val daysRemaining: Int
)
