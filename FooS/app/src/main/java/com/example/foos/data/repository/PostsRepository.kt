package com.example.foos.data.repository

import android.net.Uri
import com.example.foos.data.model.DatabasePost
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.tasks.await
import java.io.File

/**
 * 投稿内容のデータを管理するリポジトリ
 */
object PostsRepository {

    private const val MAX_LOAD_COUNT: Long = 10
    private const val COLLECTION = "posts"

    /**
     * 投稿をユーザーIDを指定して取得します
     */
    suspend fun fetchPostsByUserId(userId: String): List<DatabasePost> {
        return Firebase.firestore.collection(COLLECTION)
            .whereEqualTo("userId", userId)
            .orderBy("createdAt")
            .get().await().toObjects(DatabasePost::class.java)
    }

    /**
     * 投稿を取得します
     */
    suspend fun fetchPost(postId: String): DatabasePost? {
        val document = Firebase.firestore.collection(COLLECTION).document(postId)
        return document.get().await().toObject(DatabasePost::class.java)
    }

    /**
     * 投稿を作成します
     */
    suspend fun create(databasePost: DatabasePost) {
        val document = Firebase.firestore.collection(COLLECTION).document()
        val imageDownloadLinks = mutableListOf<String>()
        // アップロードしてダウンロードリンクを取得
        databasePost.attachedImages.forEach {
            val file = Uri.fromFile(File(it.removePrefix("file://")))
            val downloadUrl = FirebaseStorage.create(
                "images/posts/${document.id}/${file.lastPathSegment}",
                file.path.toString()
            )
            imageDownloadLinks.add(downloadUrl.toString())
        }
        document.set(databasePost.copy(postId = document.id, attachedImages = imageDownloadLinks))
        document.update("createdAt", FieldValue.serverTimestamp()).await()
    }

    /**
     * 投稿を削除します
     */
    suspend fun deletePost(postId: String) {
        Firebase.firestore.collection(COLLECTION).document(postId).delete()
    }

    /**
     * 最新の投稿を取得します
     */
    suspend fun fetchNewerPosts(): List<DatabasePost> {
        val response = Firebase.firestore.collection("posts")
            .limit(MAX_LOAD_COUNT)
            .get().await()
        return response.toObjects(DatabasePost::class.java)
    }

}