package com.example.myapplication.data.repository

import com.example.myapplication.data.local.database.dao.DietDao
import com.example.myapplication.data.local.database.entity.DietEntity

class DietRepository(private val dietDao: DietDao) {

    suspend fun saveDietRecord(record: DietEntity) {
        dietDao.insertDiet(record)
    }

    suspend fun getDietHistory(): List<DietEntity> {
        return dietDao.getAllDiet()
    }
}
