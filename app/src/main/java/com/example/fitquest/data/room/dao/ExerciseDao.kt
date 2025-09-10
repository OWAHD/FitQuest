package com.example.fitquest.data.room.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.fitquest.data.room.models.RoomExercise
import kotlinx.coroutines.flow.Flow

@Dao
interface ExerciseDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertExercise(exercise: RoomExercise)
    @Query("UPDATE exercises SET isCompleted = :completed WHERE id = :id")
    suspend fun markExerciseCompleted(id: Int, completed: Boolean)

    @Query("SELECT * FROM exercises WHERE workoutId = :workoutId")
    suspend fun getExercisesForWorkoutOnce(workoutId: Long): List<RoomExercise>

    @Query("SELECT * FROM exercises WHERE workoutId = :workoutId")
    fun getExercisesForWorkout(workoutId: Long): Flow<List<RoomExercise>>

    @Query("UPDATE exercises SET isCompleted = 0 WHERE workoutId = :workoutId")
    suspend fun resetExercisesForWorkout(workoutId: Long)

    @Query("SELECT * FROM exercises")
    fun getAllExercises(): Flow<List<RoomExercise>>

}