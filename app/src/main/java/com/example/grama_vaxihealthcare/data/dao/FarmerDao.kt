package com.example.grama_vaxihealthcare.data.dao

import androidx.room.*
import com.example.grama_vaxihealthcare.data.entity.Farmer
import kotlinx.coroutines.flow.Flow

@Dao
interface FarmerDao {
    @Query("SELECT * FROM farmers")
    fun getAllFarmers(): Flow<List<Farmer>>

    @Query("SELECT * FROM farmers WHERE id = :id")
    suspend fun getFarmerById(id: Long): Farmer?

    @Query("SELECT * FROM farmers WHERE name = :name AND phoneNumber = :phone AND villageName = :village LIMIT 1")
    suspend fun findFarmer(name: String, phone: String, village: String): Farmer?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertFarmer(farmer: Farmer): Long

    @Update
    suspend fun updateFarmer(farmer: Farmer)

    @Delete
    suspend fun deleteFarmer(farmer: Farmer)

    @Query("DELETE FROM farmers")
    suspend fun deleteAllFarmers()
}
