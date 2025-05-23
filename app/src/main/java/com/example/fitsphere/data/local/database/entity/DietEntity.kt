package com.example.fitsphere.data.local.database.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "diet_records")
data class DietEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val foodName: String,
    val calories: Int,
    val isBreakfast: Boolean,
    val isLunch: Boolean,
    val isDinner: Boolean,
    val timestamp: Long = System.currentTimeMillis()
)
