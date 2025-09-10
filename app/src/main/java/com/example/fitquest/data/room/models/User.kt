package com.example.fitquest.data.room.models

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.Relation

@Entity(tableName = "users")
data class User(
    @PrimaryKey(autoGenerate = true) val userId: Long = 0,
    val name: String,
    val username: String,
    val password: String,
    val weight: Int,
    val height: Int,
    val waterGoal: Int,
    val stepGoal: Int,
)