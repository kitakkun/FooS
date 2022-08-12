package com.example.foos.data.posts

import androidx.compose.compiler.plugins.kotlin.ComposeFqNames.remember
import androidx.compose.compiler.plugins.kotlin.lower.forEachWith
import androidx.compose.runtime.mutableStateOf
import com.example.foos.model.Post
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.withContext

object PostsRepository {

    private val MAX_LOAD_COUNT: Long = 10

    var latestPostId: String = ""

    val allPosts: MutableStateFlow<List<Post>> = MutableStateFlow(listOf())

    suspend fun fetchNewerPosts() {
        withContext(Dispatchers.IO) {
            Firebase.firestore.collection("posts")
                .whereGreaterThan("postId", latestPostId)
                .limit(MAX_LOAD_COUNT)
                .get()
                .addOnSuccessListener {
                    val objects = it.toObjects(Post::class.java)
                    allPosts.value = objects
                }
        }
    }

    suspend fun fetchOlderPosts() {
        withContext(Dispatchers.IO) {
            Firebase.firestore.collection("posts")
                .whereLessThan("postId", latestPostId)
                .limit(MAX_LOAD_COUNT)
                .get()
                .addOnSuccessListener {
                    val objects = it.toObjects(Post::class.java)
                    allPosts.value = objects
                }
        }
    }

}