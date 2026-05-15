package com.example.grama_vaxihealthcare.data.dao

import androidx.room.*
import com.example.grama_vaxihealthcare.data.entity.VaccinationSchedule
import kotlinx.coroutines.flow.Flow

@Dao
interface VaccinationScheduleDao {
    @Query("SELECT * FROM vaccination_schedules WHERE animalId = :animalId")
    fun getSchedulesForAnimal(animalId: Long): Flow<List<VaccinationSchedule>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertSchedule(schedule: VaccinationSchedule)

    @Update
    suspend fun updateSchedule(schedule: VaccinationSchedule)

    @Query("SELECT * FROM vaccination_schedules")
    fun getAllSchedules(): Flow<List<VaccinationSchedule>>

    @Query("SELECT COUNT(*) FROM vaccination_schedules WHERE date >= :currentDate")
    fun getUpcomingShotsCount(currentDate: Long): Flow<Int>

    @Query("DELETE FROM vaccination_schedules WHERE animalId = :animalId")
    suspend fun deleteSchedulesForAnimal(animalId: Long)
}
