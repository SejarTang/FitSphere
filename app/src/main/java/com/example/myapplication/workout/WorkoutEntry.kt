package com.example.myapplication.workout

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.myapplication.LatLngEntity
import kotlinx.parcelize.Parcelize

@Parcelize
@Entity(tableName = "workouts")
data class WorkoutEntry(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val type: String,
    val startTime: Long,
    val duration: Long,
    val distance: Float,
    val calories: Int,
    val route: ArrayList<LatLngEntity>
) : Parcelable
