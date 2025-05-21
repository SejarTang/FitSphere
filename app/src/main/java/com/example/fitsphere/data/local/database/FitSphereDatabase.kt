package com.example.myapplication.data.local.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.myapplication.data.local.database.dao.DietDao
import com.example.myapplication.data.local.database.entity.DietEntity

@Database(
    entities = [DietEntity::class],
    version = 1,
    exportSchema = false
)
abstract class FitSphereDatabase : RoomDatabase() {
    abstract fun dietDao(): DietDao
}
