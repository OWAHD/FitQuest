package com.example.fitquest.data.room.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import com.example.fitquest.data.room.models.User


@Dao
interface UserDao {
    @Query("SELECT * FROM users WHERE username = :username")
    suspend fun getUser(username: String): User?

    @Query("SELECT * FROM users WHERE userId = :id")
    suspend fun getUserById(id: Int): User?

    @Insert(onConflict= OnConflictStrategy.REPLACE)
    suspend fun insertUser(user: User): Long

//    @Transaction
//    @Query("SELECT * FROM users WHERE userid = :userId")
//    suspend fun getUserWithWorkouts(userId: Long): UserWithWorkouts


}