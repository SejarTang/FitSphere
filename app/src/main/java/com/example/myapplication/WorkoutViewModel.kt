package com.example.myapplication

import android.app.Application
import android.util.Log
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
    private var latestWorkout: WorkoutEntry? = null

    fun cacheWorkout(workout: WorkoutEntry) {
        latestWorkout = workout
    }

    val workoutList: StateFlow<List<WorkoutEntry>> =
        workoutDao.getAllWorkouts()
            .stateIn(viewModelScope, SharingStarted.Lazily, emptyList())

    fun getWorkoutById(id: Int): WorkoutEntry? {
        Log.d("WorkoutViewModel", "Searching workout with id=$id")

        // 先查缓存
        if (latestWorkout?.id == id) {
            Log.d("WorkoutViewModel", "Found in latestWorkout cache")
            return latestWorkout
        }


        val fromList = workoutList.value.find { it.id == id }
        if (fromList != null) {
            Log.d("WorkoutViewModel", "Found in workoutList: $fromList")
            return fromList
        }

        Log.e("WorkoutViewModel", "Workout not found for id=$id")
        return null
    }


    suspend fun saveWorkout(
        type: String,
        startTime: Long,
        duration: Long,
        distance: Float,
        calories: Int,
        route: ArrayList<LatLngEntity>
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
