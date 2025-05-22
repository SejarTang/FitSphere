package com.example.myapplication.workout

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.myapplication.LatLngEntity

@Entity(tableName = "workouts")
data class WorkoutEntry(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val type: String,
    val startTime: Long,
    val duration: Long,
    val distance: Float, // in km
    val calories: Int,
    val route: List<LatLngEntity> // for map visualization
)