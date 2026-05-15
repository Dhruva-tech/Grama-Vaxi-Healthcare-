package com.example.grama_vaxihealthcare.data.dao

import androidx.room.*
import com.example.grama_vaxihealthcare.data.entity.Animal
import kotlinx.coroutines.flow.Flow

@Dao
interface AnimalDao {
    @Query("SELECT * FROM animals")
    fun getAllAnimals(): Flow<List<Animal>>

    @Query("SELECT * FROM animals WHERE name LIKE '%' || :query || '%' OR type LIKE '%' || :query || '%'")
    fun searchAnimals(query: String): Flow<List<Animal>>

    @Query("SELECT * FROM animals WHERE id = :id")
    suspend fun getAnimalById(id: Long): Animal?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAnimal(animal: Animal): Long

    @Update
    suspend fun updateAnimal(animal: Animal)

    @Delete
    suspend fun deleteAnimal(animal: Animal)
}
