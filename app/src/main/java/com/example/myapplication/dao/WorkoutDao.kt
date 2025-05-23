package com.example.myapplication.dao

import androidx.room.*
import com.example.myapplication.workout.WorkoutEntry
import kotlinx.coroutines.flow.Flow

@Dao
interface WorkoutDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertWorkout(workout: WorkoutEntry): Long

    @Query("SELECT * FROM workouts ORDER BY startTime DESC")
    fun getAllWorkouts(): Flow<List<WorkoutEntry>>
}
