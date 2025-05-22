package com.example.myapplication.di

import com.example.myapplication.data.repository.DietRepository

object RepositoryProvider {

    private var dietRepository: DietRepository? = null

    fun initRepository() {
        if (dietRepository == null) {
            val dietDao = DatabaseProvider.getDietDao()
            dietRepository = DietRepository(dietDao)
        }
    }

    fun getDietRepository(): DietRepository {
        return dietRepository
            ?: throw IllegalStateException("Repository not initialized. Call initRepository() first.")
    }
}
