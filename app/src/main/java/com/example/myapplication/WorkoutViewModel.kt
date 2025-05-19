package com.example.myapplication

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.google.android.gms.maps.model.LatLng
import kotlinx.coroutines.launch

class WorkoutViewModel(application: Application) : AndroidViewModel(application) {

    private val db = AppDatabase.getDatabase(application)
    private val workoutDao = db.workoutDao()

    fun saveWorkout(
        type: String,
        startTime: Long,
        duration: Long,
        distance: Float,
        calories: Int,
        route: List<LatLngEntity>
    ) {
        val workout = WorkoutEntry(
            type = type,
            startTime = startTime,
            duration = duration,
            distance = distance,
            calories = calories,
            route = route
        )
        viewModelScope.launch {
            workoutDao.insertWorkout(workout)
        }
    }
}
