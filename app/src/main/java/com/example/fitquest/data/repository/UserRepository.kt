package com.example.fitquest.data.repository

import com.example.fitquest.data.room.dao.UserDao
import com.example.fitquest.data.room.models.User

interface UserRepository{
    suspend fun login(username: String, password: String): User?
    suspend fun register(user: User): Long
    suspend fun getLoggedInUser(userId: Int): User?
}


class DatabaseUserRepository(
    private val userDao: UserDao ) : UserRepository
{
    override suspend fun register(user: User) = userDao.insertUser(user)
    override suspend fun login(username: String, password: String): User? {
        val user = userDao.getUser(username)
        return if (user?.password == password) user else null
    }
    override suspend fun getLoggedInUser(userId: Int): User? {
        return userDao.getUserById(userId)
    }

}