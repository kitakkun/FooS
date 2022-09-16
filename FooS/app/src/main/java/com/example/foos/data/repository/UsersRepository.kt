package com.example.foos.data.repository

import com.example.foos.data.model.database.DatabaseUser

interface UsersRepository {
    suspend fun fetchByUserId(userId: String): DatabaseUser?
    suspend fun fetchByUserIds(userIds: List<String>): List<DatabaseUser>
    suspend fun update(databaseUser: DatabaseUser)
    suspend fun delete(userId: String)
}