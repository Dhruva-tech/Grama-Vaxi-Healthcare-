package com.example.grama_vaxihealthcare.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.grama_vaxihealthcare.data.entity.DiseaseReport
import kotlinx.coroutines.flow.Flow

@Dao
interface DiseaseReportDao {
    @Query("SELECT * FROM disease_reports ORDER BY reportDate DESC")
    fun getAllReports(): Flow<List<DiseaseReport>>

    @Query("SELECT * FROM disease_reports WHERE animalId = :animalId ORDER BY reportDate DESC")
    fun getReportsForAnimal(animalId: Long): Flow<List<DiseaseReport>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertReport(report: DiseaseReport)
}
