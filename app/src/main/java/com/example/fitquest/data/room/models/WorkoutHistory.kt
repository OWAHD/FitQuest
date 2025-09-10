package com.example.fitquest.data.room.models

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "workout_history")
data class WorkoutHistory(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val workoutId: Int,
    val date: Long,               // store timestamp
    val totalDuration: Int,       // in seconds
    val totalCalories: Int,
    val completedExercises: String // JSON string of completed exercises
)

