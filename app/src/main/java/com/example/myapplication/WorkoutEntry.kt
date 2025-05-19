package com.example.myapplication

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.android.gms.maps.model.LatLng

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