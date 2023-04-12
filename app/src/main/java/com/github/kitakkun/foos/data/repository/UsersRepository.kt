package com.github.kitakkun.foos.data.repository

import com.github.kitakkun.foos.data.model.database.DatabaseUser
import com.github.kitakkun.foos.data.model.user.Email
import com.github.kitakkun.foos.data.model.user.Password
import com.github.michaelbull.result.Result

interface UsersRepository {
    suspend fun create(email: Email, password: Password): Result<DatabaseUser, Throwable>
    suspend fun create(databaseUser: DatabaseUser)
    suspend fun fetchByUserId(userId: String): DatabaseUser?
    suspend fun fetchByUserIds(userIds: List<String>): List<DatabaseUser>
    suspend fun update(databaseUser: DatabaseUser)
    suspend fun delete(userId: String)
}
