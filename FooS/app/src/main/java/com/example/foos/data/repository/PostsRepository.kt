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

/**
 * 投稿内容のデータを管理するリポジトリ
 */
object PostsRepository {

    private const val MAX_LOAD_COUNT: Long = 10
    private const val COLLECTION = "posts"

    /**
     * 投稿を取得します
     */
    suspend fun fetchPost(postId: String): Post? {
        val document = Firebase.firestore.document(postId)
        return document.get().await().toObject(Post::class.java)
    }

    /**
     * 投稿を作成します
     */
    suspend fun create(post: Post) {
        val document = Firebase.firestore.collection(COLLECTION).document()
        val imageDownloadLinks = mutableListOf<String>()
        // アップロードしてダウンロードリンクを取得
        post.attachedImages.forEach {
            val file = Uri.fromFile(File(it.removePrefix("file://")))
            val downloadUrl = FirebaseStorage.create(
                "images/posts/${document.id}/${file.lastPathSegment}",
                file.path.toString()
            )
            imageDownloadLinks.add(downloadUrl.toString())
        }
        document.set(post.copy(postId = document.id, attachedImages = imageDownloadLinks))
        document.update("createdAt", FieldValue.serverTimestamp()).await()
    }

    /**
     * 投稿を削除します
     */
    suspend fun deletePost(postId: String) {
        FirestoreDao.delete("posts", postId)
    }

    /**
     * 最新の投稿を取得します
     */
    suspend fun fetchNewerPosts(): List<Post> {
        val response = Firebase.firestore.collection("posts")
            .limit(MAX_LOAD_COUNT)
            .get().await()
        return response.toObjects(Post::class.java)
    }

}