package com.example.fitsphere.di

import android.content.Context
import androidx.room.Room
import com.example.fitsphere.data.local.database.FitSphereDatabase
import com.example.fitsphere.data.local.database.dao.DietDao

object DatabaseProvider {

    private var databaseInstance: FitSphereDatabase? = null

    fun initDatabase(context: Context) {
        if (databaseInstance == null) {
            databaseInstance = Room.databaseBuilder(
                context.applicationContext,
                FitSphereDatabase::class.java,
                "fitsphere_db"
            ).build()
        }
    }

    fun getDatabase(): FitSphereDatabase {
        return databaseInstance
            ?: throw IllegalStateException("Database is not initialized. Call initDatabase(context) first.")
    }

    // 获取 DietDao
    fun getDietDao(): DietDao {
        return getDatabase().dietDao()
    }
}
