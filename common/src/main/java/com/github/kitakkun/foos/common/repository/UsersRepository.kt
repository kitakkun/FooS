package com.github.kitakkun.foos.common.repository

import com.github.kitakkun.foos.common.model.DatabaseUser
import com.github.kitakkun.foos.common.model.auth.Email
import com.github.kitakkun.foos.common.model.auth.Password
import com.github.michaelbull.result.Result

interface UsersRepository {
    suspend fun create(
        email: Email,
        password: Password
    ): Result<DatabaseUser, Throwable>

    suspend fun create(databaseUser: DatabaseUser)
    suspend fun fetchByUserId(userId: String): DatabaseUser?
    suspend fun fetchByUserIds(userIds: List<String>): List<DatabaseUser>
    suspend fun update(databaseUser: DatabaseUser)
    suspend fun delete(userId: String)
}
