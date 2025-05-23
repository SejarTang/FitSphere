package com.example.fitsphere.data.repository

import com.example.fitsphere.data.local.database.dao.DietDao
import com.example.fitsphere.data.local.database.entity.DietEntity

class DietRepository(private val dietDao: DietDao) {

    suspend fun saveDietRecord(record: DietEntity) {
        dietDao.insertDiet(record)
    }

    suspend fun getDietHistory(): List<DietEntity> {
        return dietDao.getAllDiet()
    }

    suspend fun deleteDietRecord(record: DietEntity) = dietDao.deleteDiet(record)

}
