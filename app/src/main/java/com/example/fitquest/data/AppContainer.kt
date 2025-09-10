package com.example.fitquest.data

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import com.example.fitquest.data.preferencesDatastore.UserPreferencesDataStore
import com.example.fitquest.data.repository.ExerciseRepository
import com.example.fitquest.data.repository.DatabaseExerciseRepository
import com.example.fitquest.data.repository.DatabaseHydrationRepository
import com.example.fitquest.data.repository.DatabaseUserRepository
import com.example.fitquest.data.repository.DatabaseWorkoutRepository
import com.example.fitquest.data.repository.HydrationRepository
import com.example.fitquest.data.repository.WorkoutRepository
import com.example.fitquest.data.repository.UserRepository
import com.example.fitquest.data.room.database.FitQuestDatabase
import com.example.fitquest.network.FitQuestApiService
import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.converter.gson.GsonConverterFactory

interface AppContainer{
    val workoutRepository: WorkoutRepository
    val userRepository: UserRepository
    val exerciseRepository: ExerciseRepository
    val hydrationRepository: HydrationRepository
    val userPreferences: UserPreferencesDataStore
}


class AppDataContainer(private val context: Context) : AppContainer{

    private val BASE_URL = "https://dummyjson.com/c/"


    private val loggingInterceptor = HttpLoggingInterceptor().apply {
        level = HttpLoggingInterceptor.Level.BODY // Log request and response details
    }

    private val okHttpClient = OkHttpClient.Builder()
        .addInterceptor(loggingInterceptor) // Added logging for debugging
        // Add authentication interceptors here if needed
        .build()

    private val retrofit: Retrofit = Retrofit.Builder()
        .addConverterFactory(GsonConverterFactory.create())
        .addConverterFactory(Json.asConverterFactory("application/json".toMediaType()))
        .baseUrl(BASE_URL)
        .client(okHttpClient)
        .build()

    private val retrofitService : FitQuestApiService by lazy {
            retrofit.create(FitQuestApiService::class.java)
    }

    override val workoutRepository: WorkoutRepository by lazy {
        DatabaseWorkoutRepository(
            FitQuestDatabase.getDatabase(context).workoutDao(),
            FitQuestDatabase.getDatabase(context).exerciseDao(),
            FitQuestDatabase.getDatabase(context).workoutHistoryDao()
        )
    }

    override val userRepository: UserRepository by lazy {
        DatabaseUserRepository(
            FitQuestDatabase.getDatabase(context).userDao()
        )
    }

    override val exerciseRepository: ExerciseRepository by lazy {
        DatabaseExerciseRepository(
            retrofitService,
            FitQuestDatabase.getDatabase(context).exerciseDao(),
            FitQuestDatabase.getDatabase(context).workoutDao()
        )
    }

    override val hydrationRepository: HydrationRepository by lazy {
        DatabaseHydrationRepository(
            FitQuestDatabase.getDatabase(context).hydrationDao()
        )
    }

    private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(
        name = "user_data"
    )

    override val userPreferences: UserPreferencesDataStore by lazy {
        UserPreferencesDataStore(dataStore = context.dataStore)
    }


}

