package com.example.fitquest.data.room.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.fitquest.data.room.models.WorkoutHistory
import kotlinx.coroutines.flow.Flow

@Dao
interface WorkoutHistoryDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertWorkoutHistory(history: WorkoutHistory)

    @Query("SELECT * FROM workout_history ORDER BY date DESC")
    fun getWorkoutHistory(): Flow<List<WorkoutHistory>>
}