package com.example.fitquest.data.repository

import com.example.fitquest.data.room.dao.ExerciseDao
import com.example.fitquest.data.room.dao.WorkoutDao
import com.example.fitquest.data.room.models.RoomExercise
import com.example.fitquest.network.FitQuestApiService
import com.example.fitquest.network.models.Exercise
import kotlinx.coroutines.flow.Flow

interface ExerciseRepository{
    suspend fun getExercisesFromApi(): List<Exercise>
    suspend fun insertExercise(exercise: RoomExercise)
    suspend fun completeExercise(exerciseId: Int, workoutId: Long)
    fun getExercisesForWorkout(workoutId: Long): Flow<List<RoomExercise>>
}


class DatabaseExerciseRepository(
    private val fitQuestApiService: FitQuestApiService,
    private val exerciseDao: ExerciseDao,
    private val workoutDao: WorkoutDao,
) : ExerciseRepository
{
    override suspend fun getExercisesFromApi(): List<Exercise> = fitQuestApiService.getExercises().results
    override suspend fun insertExercise(exercise: RoomExercise) = exerciseDao.insertExercise(exercise)
    override suspend fun completeExercise(exerciseId: Int, workoutId: Long) {
        exerciseDao.markExerciseCompleted(exerciseId, true)
        val exercises = exerciseDao.getExercisesForWorkoutOnce(workoutId)
        val allDone = exercises.all { it.isCompleted }
        if (allDone) {
            workoutDao.updateWorkoutCompletion(workoutId, true, System.currentTimeMillis())
        }
    }
    override fun getExercisesForWorkout(workoutId: Long): Flow<List<RoomExercise>> = exerciseDao.getExercisesForWorkout(workoutId)

}