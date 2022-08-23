package com.example.foos.data.domain

import com.example.foos.data.model.DatabaseUser
import com.example.foos.data.repository.UsersRepository
import kotlinx.coroutines.coroutineScope
import javax.inject.Inject

class GetUserInfoUseCase @Inject constructor(
    private val usersRepository: UsersRepository,
) {

    suspend operator fun invoke(userId: String, onLoad: (DatabaseUser?) -> Unit) {
        val user = usersRepository.fetchUser(userId)
        onLoad.invoke(user)
    }

}