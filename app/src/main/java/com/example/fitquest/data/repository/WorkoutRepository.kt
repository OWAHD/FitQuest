package com.example.fitquest.data.repository


import com.example.fitquest.data.room.dao.ExerciseDao
import com.example.fitquest.data.room.dao.WorkoutDao
import com.example.fitquest.data.room.dao.WorkoutHistoryDao
import com.example.fitquest.data.room.models.RoomExercise
import com.example.fitquest.data.room.models.Workout
import com.example.fitquest.data.room.models.WorkoutHistory
import com.example.fitquest.data.room.models.WorkoutWithExercises
import kotlinx.coroutines.flow.Flow

interface WorkoutRepository{
    suspend fun insertWorkout(workout: Workout): Long
    fun getAllWorkouts(): Flow<List<Workout>>
    fun getWorkoutItem(workoutName: String): Flow<Workout>
    suspend fun delete(workout: Workout)
    suspend fun resetWorkout(workoutId: Long)
    suspend fun insertWorkoutWithExercises(workout: Workout, exercises: List<RoomExercise>)

    fun getAllWorkoutsWithExercises(): Flow<List<WorkoutWithExercises>>
    fun getWorkoutWithExercises(workoutId: Long): Flow<WorkoutWithExercises>
    fun getWorkoutHistory(): Flow<List<WorkoutHistory>>
    suspend fun saveWorkoutHistory(history: WorkoutHistory)
}




class DatabaseWorkoutRepository(
    private val workoutDao: WorkoutDao,
    private val exerciseDao: ExerciseDao,
    private val workoutHistoryDao: WorkoutHistoryDao
    ) : WorkoutRepository
{
    override suspend fun insertWorkout(workout: Workout): Long = workoutDao.insertWorkout(workout)
    override fun getAllWorkouts(): Flow<List<Workout>> = workoutDao.getAllWorkouts()
    override fun getWorkoutItem(workoutName:String): Flow<Workout> = workoutDao.getWorkoutByName(workoutName)
    override suspend fun delete(workout:Workout) = workoutDao.delete(workout)
//    override fun getWorkoutHistory(): Flow<List<Workout>> = workoutDao.getWorkoutHistory()
    override suspend fun resetWorkout(workoutId: Long) {
        workoutDao.resetWorkout(workoutId)
        exerciseDao.resetExercisesForWorkout(workoutId)
    }
    override suspend fun insertWorkoutWithExercises(workout: Workout, exercises: List<RoomExercise>) = workoutDao.insertWorkoutWithExercises(workout, exercises)
    override fun getAllWorkoutsWithExercises(): Flow<List<WorkoutWithExercises>> = workoutDao.getAllWorkoutsWithExercises()
    override fun getWorkoutWithExercises(workoutId: Long): Flow<WorkoutWithExercises> = workoutDao.getWorkoutWithExercises(workoutId)
    override fun getWorkoutHistory(): Flow<List<WorkoutHistory>> =
        workoutHistoryDao.getWorkoutHistory()
    override suspend fun saveWorkoutHistory(history: WorkoutHistory) {
        workoutHistoryDao.insertWorkoutHistory(history)
    }
}





