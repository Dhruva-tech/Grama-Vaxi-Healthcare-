package com.example.grama_vaxihealthcare.data.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "disease_reports",
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
data class DiseaseReport(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val animalId: Long,
    val symptoms: String,
    val imageUrl: String?,
    val aiSuggestions: String?,
    val reportDate: Long = System.currentTimeMillis()
)
