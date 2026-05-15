package com.example.grama_vaxihealthcare.data.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "inbox_reports",
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
data class InboxReport(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val animalId: Long,
    val animalName: String,
    val animalType: String,
    val symptoms: String,
    val reportDate: String,
    val reportTime: String,
    val status: String = STATUS_SUBMITTED,
    val createdAt: Long = System.currentTimeMillis()
) {
    companion object {
        const val STATUS_SUBMITTED = "Submitted"
        const val STATUS_UNDER_REVIEW = "Under Review"
        const val STATUS_RESOLVED = "Resolved"
    }
}
