package com.example.fitquest.data.room.models

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "exercises",
    foreignKeys = [
        ForeignKey(
            entity = Workout::class,
            parentColumns = ["workoutId"],
            childColumns = ["workoutId"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [Index("workoutId")]
)
data class RoomExercise(
    val workoutId: Long,
    val name: String,
    val category: String,
    val equipment: String,
    val muscles: List<String>,
    val description: String,
    val reps: Int,
    val sets: Int,
    val isCompleted: Boolean = false,
    val duration: Int = 30,
    @PrimaryKey(autoGenerate = true) val id: Int = 0
)