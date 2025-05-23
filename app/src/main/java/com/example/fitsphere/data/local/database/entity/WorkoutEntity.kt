package com.example.fitsphere.data.local.database.entity

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.fitsphere.data.local.database.entity.LatLngEntity
import kotlinx.parcelize.Parcelize

@Parcelize
@Entity(tableName = "workouts")
data class WorkoutEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val type: String,
    val startTime: Long,
    val duration: Long,
    val distance: Float,
    val calories: Int,
    val route: ArrayList<LatLngEntity>
) : Parcelable