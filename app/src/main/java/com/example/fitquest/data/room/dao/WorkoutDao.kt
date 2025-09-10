package com.example.fitquest.data.room.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import com.example.fitquest.data.room.models.RoomExercise
import com.example.fitquest.data.room.models.Workout
import com.example.fitquest.data.room.models.WorkoutWithExercises
import kotlinx.coroutines.flow.Flow


@Dao
interface WorkoutDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertWorkout(workout: Workout): Long

    @Query("UPDATE workouts SET isCompleted = :completed, completedAt = :timestamp WHERE workoutId = :workoutId")
    suspend fun updateWorkoutCompletion(workoutId: Long, completed: Boolean, timestamp: Long?)

    @Query("UPDATE workouts SET isCompleted = 0, completedAt = NULL WHERE workoutId = :workoutId")
    suspend fun resetWorkout(workoutId: Long)

    @Delete
    suspend fun delete(workout: Workout)

    @Query("SELECT * FROM workouts WHERE workoutName LIKE '%'|| :workoutName ||'%' ")
    fun getWorkoutByName(workoutName: String): Flow<Workout>

    @Query("SELECT * FROM workouts WHERE workoutId = :id")
    suspend fun getWorkoutById(id: Long): Workout

    @Query("SELECT * FROM workouts WHERE isCompleted = 1 ORDER BY completedAt DESC")
    fun getWorkoutHistory(): Flow<List<Workout>>

    @Insert
    suspend fun insertExercise(exercise: List<RoomExercise>)

    @Query("SELECT * FROM workouts")
    fun getAllWorkouts(): Flow<List<Workout>>

    @Transaction
    suspend fun insertWorkoutWithExercises(workout: Workout, exercises: List<RoomExercise>) {
        val workoutId = insertWorkout(workout)
        val exercisesWithWorkoutId = exercises.map { it.copy(workoutId = workoutId) }
        insertExercise(exercisesWithWorkoutId)
    }

    @Transaction
    @Query("SELECT * FROM workouts WHERE workoutId = :workoutId")
    fun getWorkoutWithExercises(workoutId: Long): Flow<WorkoutWithExercises>

    @Transaction
    @Query("SELECT * FROM workouts")
    fun getAllWorkoutsWithExercises(): Flow<List<WorkoutWithExercises>>


}