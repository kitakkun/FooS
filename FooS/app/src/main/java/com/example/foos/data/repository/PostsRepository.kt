package com.example.foos.data.repository

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import com.example.foos.data.model.DatabasePost
import com.example.foos.util.ImageConverter
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.tasks.await
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.util.*

/**
 * 投稿内容のデータを管理するリポジトリ
 */
object PostsRepository {

    private const val MAX_LOAD_COUNT: Long = 10
    private const val MAX_UPLOAD_IMAGE_SIZE = 1024
    private const val COLLECTION = "posts"

    /**
     * ユーザーIDと日時を指定して投稿を取得します
     * @param userId ユーザID
     * @param start 開始の日時 ex) 2022/01/01
     * @param end 終了の日時   ex) 2022/01/06
     * @param count 取得するデータ数
     */
    suspend fun fetchByUserIdWithDate(
        userId: String,
        start: Date? = null,
        end: Date? = null,
        count: Long = MAX_LOAD_COUNT,
    ): List<DatabasePost> {
        val collection = Firebase.firestore.collection(COLLECTION)
        var query = collection.whereEqualTo("userId", userId)
        start?.let { query = query.whereGreaterThanOrEqualTo("createdAt", start) }
        end?.let { query = query.whereGreaterThanOrEqualTo("createdAt", end) }
        query = query.orderBy("createdAt", Query.Direction.DESCENDING)
            .limit(count)
        return query.get().await().toObjects(DatabasePost::class.java)
    }

    /**
     * 指定ユーザの最新の投稿を取得します
     * @param userId ユーザID
     * @param count 取得するデータ数
     */
    suspend fun fetchByUserId(
        userId: String,
        count: Long = MAX_LOAD_COUNT
    ): List<DatabasePost> {
        val collection = Firebase.firestore.collection(COLLECTION)
        var query = collection.whereEqualTo("userId", userId)
        query = query.orderBy("createdAt", Query.Direction.DESCENDING)
            .limit(count)
        return query.get().await().toObjects(DatabasePost::class.java)
    }

    /**
     * 投稿を取得します
     */
    suspend fun fetchByPostId(postId: String): DatabasePost? {
        val document = Firebase.firestore.collection(COLLECTION).document(postId)
        return document.get().await().toObject(DatabasePost::class.java)
    }

    /**
     * 投稿を作成します
     */
    suspend fun create(databasePost: DatabasePost, context: Context) {
        val document = Firebase.firestore.collection(COLLECTION).document()
        val imageDownloadLinks = mutableListOf<String>()
        // ファイルの圧縮
        var i = 1
        databasePost.attachedImages.forEach {
            val bitmap = BitmapFactory.decodeFile(it.removePrefix("file://"))
            val resized = ImageConverter.resize(bitmap, MAX_UPLOAD_IMAGE_SIZE, true)
            val compressedFilePath = "${context.cacheDir}/image$i.jpeg"
            try {
                val outStream = FileOutputStream(compressedFilePath)
                resized.compress(Bitmap.CompressFormat.JPEG, 85, outStream)
                // アップロードしてダウンロードリンクを取得
                val file = Uri.fromFile(File(compressedFilePath))
                val downloadUrl = FirebaseStorage.create(
                    "images/posts/${document.id}/${file.lastPathSegment}",
                    file.path.toString()
                )
                imageDownloadLinks.add(downloadUrl.toString())
                i++
            } catch (e: IOException) {
                e.printStackTrace()
            }
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
            .orderBy("createdAt", Query.Direction.DESCENDING)
            .limit(MAX_LOAD_COUNT)
            .get().await()
        return response.toObjects(DatabasePost::class.java)
    }

}