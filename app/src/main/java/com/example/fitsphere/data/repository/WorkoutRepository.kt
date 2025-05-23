package com.example.fitsphere.repository

import com.example.fitsphere.dao.WorkoutDao
import com.example.fitsphere.data.local.database.entity.WorkoutEntity
import kotlinx.coroutines.flow.Flow

class WorkoutRepository(private val workoutDao: WorkoutDao) {

    // Save a workout entry
    suspend fun saveWorkout(workout: WorkoutEntity): Long {
        return workoutDao.insertWorkout(workout)
    }

    // Get all workouts as a Flow
    fun getAllWorkouts(): Flow<List<WorkoutEntity>> {
        return workoutDao.getAllWorkouts()
    }
}
