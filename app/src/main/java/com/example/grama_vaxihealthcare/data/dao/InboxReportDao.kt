package com.example.grama_vaxihealthcare.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.grama_vaxihealthcare.data.entity.InboxReport
import kotlinx.coroutines.flow.Flow

@Dao
interface InboxReportDao {
    @Query("SELECT * FROM inbox_reports ORDER BY createdAt DESC")
    fun getAllInboxReports(): Flow<List<InboxReport>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertInboxReport(report: InboxReport)
}
