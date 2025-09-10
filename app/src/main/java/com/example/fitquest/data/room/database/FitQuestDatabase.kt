package com.example.fitquest.data.room.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.fitquest.data.room.Converters
import com.example.fitquest.data.room.dao.ExerciseDao
import com.example.fitquest.data.room.dao.HydrationDao
import com.example.fitquest.data.room.dao.UserDao
import com.example.fitquest.data.room.dao.WorkoutDao
import com.example.fitquest.data.room.dao.WorkoutHistoryDao
import com.example.fitquest.data.room.models.HydrationLog
import com.example.fitquest.data.room.models.RoomExercise
import com.example.fitquest.data.room.models.User
import com.example.fitquest.data.room.models.Workout
import com.example.fitquest.data.room.models.WorkoutHistory



@Database(
    entities = [User::class, Workout::class, RoomExercise::class, HydrationLog::class, WorkoutHistory::class],
    version = 2,
    exportSchema = false
)
@TypeConverters(Converters::class)
abstract class FitQuestDatabase : RoomDatabase() {

    abstract fun userDao(): UserDao
    abstract fun workoutDao(): WorkoutDao
    abstract fun exerciseDao(): ExerciseDao
    abstract fun hydrationDao(): HydrationDao
    abstract fun workoutHistoryDao(): WorkoutHistoryDao




    companion object{
        @Volatile
        private var Instance: FitQuestDatabase? = null

        fun getDatabase(context: Context): FitQuestDatabase{

            return Instance ?: synchronized(this) {
                Room.databaseBuilder(
                    context.applicationContext, FitQuestDatabase::class.java, "fitQuest_database")
                    .fallbackToDestructiveMigration(false)
                    .build()
                    .also { Instance = it }
            }
        }
    }
}