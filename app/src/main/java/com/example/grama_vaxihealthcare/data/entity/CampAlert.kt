package com.example.grama_vaxihealthcare.data.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "camp_alerts")
data class CampAlert(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val title: String,
    val message: String,
    val date: String,
    val location: String,
    val doctorName: String,
    val createdAt: Long = System.currentTimeMillis(),
    val isNotified: Boolean = false
)
