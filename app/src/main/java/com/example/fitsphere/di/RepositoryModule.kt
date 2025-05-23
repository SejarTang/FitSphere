package com.example.fitsphere.di

import com.example.fitsphere.data.repository.DietRepository

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
