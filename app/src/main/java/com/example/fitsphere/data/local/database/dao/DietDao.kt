package com.example.myapplication.data.local.database.dao

import androidx.room.*
import com.example.myapplication.data.local.database.entity.DietEntity

@Dao
interface DietDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertDiet(diet: DietEntity)

    @Query("SELECT * FROM diet_records ORDER BY timestamp DESC")
    suspend fun getAllDiet(): List<DietEntity>

    @Delete
    suspend fun deleteDiet(diet: DietEntity)

}
