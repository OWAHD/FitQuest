package com.example.fitquest.data.room.models

import androidx.room.Embedded
import androidx.room.Relation

data class UserWithWorkouts(
    @Embedded val user: User,

    @Relation(
        parentColumn = "userId",
        entityColumn = "workoutOwnerId"
    )
    val workouts: List<Workout>
)


data class WorkoutWithExercises(
    @Embedded val workout: Workout,

    @Relation(
        parentColumn = "workoutId",
        entityColumn = "workoutId"
    ) val exercises: List<RoomExercise>
)