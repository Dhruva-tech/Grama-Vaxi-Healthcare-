package com.example.grama_vaxihealthcare.data.dao

import androidx.room.*
import com.example.grama_vaxihealthcare.data.entity.Vaccination
import kotlinx.coroutines.flow.Flow

@Dao
interface VaccinationDao {
    @Query("SELECT * FROM vaccinations WHERE animalId = :animalId")
    fun getVaccinationsForAnimal(animalId: Long): Flow<List<Vaccination>>

    @Query("SELECT * FROM vaccinations")
    fun getAllVaccinations(): Flow<List<Vaccination>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertVaccination(vaccination: Vaccination)

    @Query("DELETE FROM vaccinations WHERE animalId = :animalId")
    suspend fun deleteVaccinationsForAnimal(animalId: Long)
}
