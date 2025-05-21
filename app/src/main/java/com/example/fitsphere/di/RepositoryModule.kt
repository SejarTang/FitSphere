package com.example.myapplication.di

import com.example.myapplication.data.local.database.dao.DietDao
import com.example.myapplication.data.repository.DietRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RepositoryModule {

    @Provides
    @Singleton
    fun provideDietRepository(dietDao: DietDao): DietRepository {
        return DietRepository(dietDao)
    }
}
