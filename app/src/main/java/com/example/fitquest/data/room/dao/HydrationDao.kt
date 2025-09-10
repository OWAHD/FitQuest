package com.example.fitquest.data.room.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.fitquest.data.room.models.HydrationLog

@Dao
interface HydrationDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertHydrationLog(log: HydrationLog)

    @Query("SELECT SUM(amountMl) FROM hydration_log WHERE userId = :userId AND date(timestamp/1000, 'unixepoch') = date('now')")
    suspend fun getTodayTotal(userId: Int): Int?

    @Query("SELECT * FROM hydration_log WHERE userId = :userId ORDER BY timestamp DESC")
    suspend fun getHydrationHistory(userId: Int): List<HydrationLog>
}