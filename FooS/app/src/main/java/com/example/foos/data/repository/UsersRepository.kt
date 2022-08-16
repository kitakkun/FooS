package com.example.foos.data.repository

import com.example.foos.model.UserData
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.tasks.await

object UsersRepository {

    suspend fun fetchUser(userId: String) : UserData? {
        val userData = Firebase.firestore.collection("users")
            .whereEqualTo("userId", userId)
            .get().await().toObjects(UserData::class.java)
        return userData[0]
    }

}