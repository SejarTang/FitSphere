package com.example.myapplication.di

import android.content.Context
import androidx.room.Room
import com.example.myapplication.data.local.database.FitSphereDatabase
import com.example.myapplication.data.local.database.dao.DietDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext context: Context): FitSphereDatabase {
        return Room.databaseBuilder(
            context,
            FitSphereDatabase::class.java,
            "fitsphere_db"
        ).build()
    }

    @Provides
    fun provideDietDao(database: FitSphereDatabase): DietDao {
        return database.dietDao()
    }
}
