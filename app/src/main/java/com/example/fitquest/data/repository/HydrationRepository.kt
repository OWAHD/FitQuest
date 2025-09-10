package com.example.fitquest.data.repository

import com.example.fitquest.data.room.dao.HydrationDao
import com.example.fitquest.data.room.models.HydrationLog

interface HydrationRepository {
    suspend fun addWater(userId: Int, amountMl: Int)
    suspend fun getTodayTotal(userId: Int): Int
    suspend fun getHydrationHistory(userId: Int): List<HydrationLog>
}


class DatabaseHydrationRepository(private val hydrationDao: HydrationDao): HydrationRepository {

    override suspend fun addWater(userId: Int, amountMl: Int) {
        val log = HydrationLog(userId = userId, amountMl = amountMl)
        hydrationDao.insertHydrationLog(log)
    }

    override suspend fun getTodayTotal(userId: Int): Int {
        return hydrationDao.getTodayTotal(userId) ?: 0
    }

    override suspend fun getHydrationHistory(userId: Int): List<HydrationLog> {
        return hydrationDao.getHydrationHistory(userId)
    }
}