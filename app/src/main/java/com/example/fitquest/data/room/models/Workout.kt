package com.example.fitquest.data.room.models

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey


@Entity(tableName = "workouts",
//    foreignKeys = [
//        ForeignKey(
//            entity = User::class,
//            parentColumns = ["userId"],
//            childColumns = ["userId"],
//            onDelete = ForeignKey.CASCADE
//        )
//    ],
//    indices = [Index("userId")]
)
data class Workout(
    val userId: Long ,
    val workoutName: String,
    val description: String,
    val isCompleted: Boolean = false,
    val completedAt: Long? = null, // Track completion time
    @PrimaryKey(autoGenerate = true) val workoutId: Long = 0
)