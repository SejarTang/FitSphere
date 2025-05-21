package com.example.myapplication.data.repository

import com.example.myapplication.data.local.database.dao.DietDao
import com.example.myapplication.data.local.database.entity.DietEntity
import javax.inject.Inject

class DietRepository @Inject constructor(
    private val dietDao: DietDao
) {
    suspend fun saveDietRecord(record: DietEntity) = dietDao.insertDiet(record)
    suspend fun getDietHistory(): List<DietEntity> = dietDao.getAllDiet()
}
