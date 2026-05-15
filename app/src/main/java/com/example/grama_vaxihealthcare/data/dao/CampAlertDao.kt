package com.example.grama_vaxihealthcare.data.dao

import androidx.room.*
import com.example.grama_vaxihealthcare.data.entity.CampAlert
import kotlinx.coroutines.flow.Flow

@Dao
interface CampAlertDao {
    @Query("SELECT * FROM camp_alerts ORDER BY date ASC")
    fun getAllCampAlerts(): Flow<List<CampAlert>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCampAlert(campAlert: CampAlert)

    @Update
    suspend fun updateCampAlert(campAlert: CampAlert)
}
