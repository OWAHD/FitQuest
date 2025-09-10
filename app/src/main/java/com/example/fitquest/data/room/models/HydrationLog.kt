package com.example.fitquest.data.room.models

import androidx.room.Entity
import androidx.room.PrimaryKey



@Entity(tableName = "hydration_log")
data class HydrationLog(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val userId: Int, // foreign key reference to User
    val amountMl: Int, // amount of water consumed
    val timestamp: Long = System.currentTimeMillis()
)