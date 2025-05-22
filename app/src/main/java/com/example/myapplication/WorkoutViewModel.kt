package com.example.myapplication

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.myapplication.dao.WorkoutDao
import com.example.myapplication.LatLngEntity
import com.example.myapplication.workout.WorkoutEntry
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class WorkoutViewModel(application: Application) : AndroidViewModel(application) {

    private val db = AppDatabase.getDatabase(application)
    private val workoutDao: WorkoutDao = db.workoutDao()

    val workoutList: StateFlow<List<WorkoutEntry>> =
        workoutDao.getAllWorkouts()
            .stateIn(viewModelScope, SharingStarted.Lazily, emptyList())

    fun getWorkoutById(id: Int): WorkoutEntry? {
        return workoutList.value.find { it.id == id }
    }

    suspend fun saveWorkout(
        type: String,
        startTime: Long,
        duration: Long,
        distance: Float,
        calories: Int,
        route: List<LatLngEntity>
    ): WorkoutEntry {
        val workout = WorkoutEntry(
            type = type,
            startTime = startTime,
            duration = duration,
            distance = distance,
            calories = calories,
            route = route
        )

        val insertedId = workoutDao.insertWorkout(workout).toInt()
        return workout.copy(id = insertedId)
    }




}
