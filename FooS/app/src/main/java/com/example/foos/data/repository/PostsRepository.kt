package com.example.foos.data.repository

import android.util.Log
import com.example.foos.data.repository.model.PostData
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext

object PostsRepository {

    private val MAX_LOAD_COUNT: Long = 10

    var latestPostId: String = ""

    val allPosts: MutableStateFlow<List<PostData>> = MutableStateFlow(listOf())

    suspend fun fetchInitialPosts() {
        withContext(Dispatchers.IO) {
            Firebase.firestore.collection("posts")
                .orderBy("createdAt")
        }
    }

    suspend fun fetchNewerPosts() : List<PostData> {
        val response = Firebase.firestore.collection("posts")
            .limit(MAX_LOAD_COUNT)
//            .whereGreaterThan("postId", latestPostId)
            .get().await()
        return response.toObjects(PostData::class.java)
    }

    suspend fun fetchOlderPosts() {
        withContext(Dispatchers.IO) {
            Firebase.firestore.collection("posts")
                .limit(MAX_LOAD_COUNT)
                .whereLessThan("postId", latestPostId)
                .get()
                .addOnSuccessListener {
                    val objects = it.toObjects(PostData::class.java)
                    allPosts.value = objects
                }
        }
    }

}