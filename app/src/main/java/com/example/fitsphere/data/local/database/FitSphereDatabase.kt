package com.example.fitsphere.data.local.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.fitsphere.dao.WorkoutDao
import com.example.fitsphere.data.local.database.dao.DietDao
import com.example.fitsphere.data.local.database.entity.DietEntity
import com.example.fitsphere.data.local.database.entity.WorkoutEntity
import com.example.fitsphere.data.local.database.entity.Converters

@Database(
    entities = [DietEntity::class, WorkoutEntity::class],
    version = 2,
    exportSchema = false
)
@TypeConverters(Converters::class)
abstract class FitSphereDatabase : RoomDatabase() {
    abstract fun dietDao(): DietDao
    abstract fun workoutDao(): WorkoutDao

    companion object {
        @Volatile private var INSTANCE: FitSphereDatabase? = null

        fun getDatabase(context: Context): FitSphereDatabase {
            return INSTANCE ?: synchronized(this) {
                Room.databaseBuilder(
                    context.applicationContext,
                    FitSphereDatabase::class.java,
                    "fit_db"
                ).fallbackToDestructiveMigration().build().also { INSTANCE = it }
            }
        }
    }
}
