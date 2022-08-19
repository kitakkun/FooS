package com.example.foos.data.repository

import android.net.Uri
import com.example.foos.data.model.Post
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import java.io.File

object PostsRepository {

    private const val MAX_LOAD_COUNT: Long = 10

    var latestPostId: String = ""

    val allPosts: MutableStateFlow<List<Post>> = MutableStateFlow(listOf())

    suspend fun fetchPost(postId: String) : Post? {
        val ref = FirestoreDao.createDocumentReference("posts", postId)
        return ref.get().await().toObject(Post::class.java)
    }

    suspend fun createPost(postData: Post) {
        val docRef = FirestoreDao.createDocumentReference(FirestoreDao.COLLECTION_POSTS)
        val imageUrls = mutableListOf<String>()
        // アップロードしてダウンロードリンクを取得
        for (i in postData.attachedImages.indices) {
            val file = Uri.fromFile(File(postData.attachedImages[i].removePrefix("file://")))
            val remotePath = "images/posts/${docRef.id}/${file.lastPathSegment}"
            val localPath = file.path.toString()
            val downloadLink = FirebaseStorage.create(remotePath, localPath)
            imageUrls.add(downloadLink.toString())
        }
        val updates = hashMapOf<String, Any>(
            "createdAt" to FieldValue.serverTimestamp()
        )
        docRef.set(postData.copy(postId = docRef.id, attachedImages = imageUrls)).await()
        docRef.update(updates).await()
    }

    suspend fun deletePost(postId: String) {
        FirestoreDao.delete("posts", postId)
    }

    suspend fun fetchInitialPosts() {
        withContext(Dispatchers.IO) {
            Firebase.firestore.collection("posts")
                .orderBy("createdAt")
        }
    }

    suspend fun fetchNewerPosts() : List<Post> {
        val response = Firebase.firestore.collection("posts")
            .limit(MAX_LOAD_COUNT)
//            .whereGreaterThan("postId", latestPostId)
            .get().await()
        return response.toObjects(Post::class.java)
    }

    suspend fun fetchOlderPosts() {
        withContext(Dispatchers.IO) {
            Firebase.firestore.collection("posts")
                .limit(MAX_LOAD_COUNT)
                .whereLessThan("postId", latestPostId)
                .get()
                .addOnSuccessListener {
                    val objects = it.toObjects(Post::class.java)
                    allPosts.value = objects
                }
        }
    }

}