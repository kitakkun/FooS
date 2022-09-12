package com.example.foos.data.repository

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import com.example.foos.data.model.database.DatabasePost
import com.example.foos.util.ImageConverter
import com.google.android.gms.maps.model.LatLngBounds
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.google.firebase.storage.FirebaseStorage
import kotlinx.coroutines.tasks.await
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.util.*
import javax.inject.Inject

/**
 * 投稿内容のデータを管理するリポジトリ
 */
class PostsRepositoryImpl @Inject constructor(
    private val database: FirebaseFirestore,
    private val storage: FirebaseStorage,
): PostsRepository {

    companion object {
        const val DEFAULT_LOAD_LIMIT: Long = 15
        private const val MAX_UPLOAD_IMAGE_SIZE = 1024
        private const val COLLECTION = "posts"
    }

    override suspend fun fetch(
        start: Date?,
        end: Date?,
        count: Long,
    ): List<DatabasePost> {
        var query = database.collection(COLLECTION)
            .orderBy("createdAt", Query.Direction.DESCENDING)
        start?.let { query = query.whereGreaterThanOrEqualTo("createdAt", start) }
        end?.let { query = query.whereLessThanOrEqualTo("createdAt", end) }
        return query.limit(count)
            .get().await().toObjects(DatabasePost::class.java)
    }

    /**
     * 画像付きの投稿をフェッチ
     */
    override suspend fun fetchWithMediaByUserId(
        userId: String,
        start: Date?,
        end: Date?,
        count: Long,
    ): List<DatabasePost> {
        var query = database.collection(COLLECTION)
            .whereEqualTo("userId", userId)
            .orderBy("createdAt", Query.Direction.DESCENDING)
        start?.let { query = query.whereGreaterThanOrEqualTo("createdAt", start) }
        end?.let { query = query.whereLessThanOrEqualTo("createdAt", end) }
        return query.limit(count)
            .get().await().toObjects(DatabasePost::class.java)
            .filter { it.attachedImages.isNotEmpty() }
    }

    /**
     *
     */
    override suspend fun fetchByLatLngBounds(bounds: LatLngBounds): List<DatabasePost> {
        return database.collection(COLLECTION)
            .whereLessThanOrEqualTo("longitude", bounds.northeast.longitude)
            .whereGreaterThanOrEqualTo("longitude", bounds.southwest.longitude)
            .get().await().toObjects(DatabasePost::class.java).filter {
                it.latitude!! <= bounds.northeast.latitude && it.latitude >= bounds.southwest.latitude
            }
    }

    /**
     * ユーザーIDと日時を指定して投稿を取得します
     * @param userId ユーザID
     * @param start 開始の日時 ex) 2022/01/01
     * @param end 終了の日時   ex) 2022/01/06
     * @param count 取得するデータ数
     */
    override suspend fun fetchByUserIdWithDate(
        userId: String,
        start: Date?,
        end: Date?,
        count: Long,
    ): List<DatabasePost> {
        val collection = database.collection(COLLECTION)
        var query = collection.whereEqualTo("userId", userId)
        start?.let { query = query.whereGreaterThanOrEqualTo("createdAt", start) }
        end?.let { query = query.whereLessThanOrEqualTo("createdAt", end) }
        query = query.orderBy("createdAt", Query.Direction.DESCENDING)
            .limit(count)
        return query.get().await().toObjects(DatabasePost::class.java)
    }

    /**
     * ユーザーID(複数)と日時を指定して投稿を取得します
     * @param userIds ユーザID
     * @param start 開始の日時 ex) 2022/01/01
     * @param end 終了の日時   ex) 2022/01/06
     * @param count 取得するデータ数
     */
    override suspend fun fetchByUserIdsWithDate(
        userIds: List<String>,
        start: Date?,
        end: Date?,
        count: Long,
    ): List<DatabasePost> {
        val collection = database.collection(COLLECTION)
        var query = collection.whereIn("userId", userIds)
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
    override suspend fun fetchByUserId(
        userId: String,
        count: Long,
    ): List<DatabasePost> {
        val collection = database.collection(COLLECTION)
        var query = collection.whereEqualTo("userId", userId)
        query = query.orderBy("createdAt", Query.Direction.DESCENDING)
            .limit(count)
        return query.get().await().toObjects(DatabasePost::class.java)
    }

    /**
     * 指定ユーザ（複数）の最新の投稿を取得します
     * @param userIds ユーザIDのリスト
     * @param count 取得するデータ数
     */
    override suspend fun fetchByUserIds(
        userIds: List<String>,
        count: Long,
    ): List<DatabasePost> {
        val collection = database.collection(COLLECTION)
        var query = collection.whereIn("userId", userIds)
        query = query.orderBy("createdAt", Query.Direction.DESCENDING)
            .limit(count)
        return query.get().await().toObjects(DatabasePost::class.java)
    }

    /**
     * 投稿を取得します
     */
    override suspend fun fetchByPostId(postId: String): DatabasePost? {
        val document = database.collection(COLLECTION).document(postId)
        return document.get().await().toObject(DatabasePost::class.java)
    }

    /**
     * 投稿を作成します
     */
    override suspend fun create(databasePost: DatabasePost, context: Context) {
        val document = database.collection(COLLECTION).document()
        val imageDownloadLinks = mutableListOf<String>()
        // ファイルの圧縮
        databasePost.attachedImages.forEachIndexed { i, url ->
            val bitmap = BitmapFactory.decodeFile(url.removePrefix("file://"))
            val resized = ImageConverter.resize(bitmap, MAX_UPLOAD_IMAGE_SIZE, true)
            val compressedFilePath = "${context.cacheDir}/image$i.jpeg"
            try {
                val outStream = FileOutputStream(compressedFilePath)
                resized.compress(Bitmap.CompressFormat.JPEG, 85, outStream)
                // アップロードしてダウンロードリンクを取得
                val file = Uri.fromFile(File(compressedFilePath))
                val ref = storage.reference.child(
                    "images/posts/${document.id}/${file.lastPathSegment}",
                )
                ref.putFile(file).await()
                val downloadUrl = ref.downloadUrl.await()
                imageDownloadLinks.add(downloadUrl.toString())
            } catch (e: IOException) {
                e.printStackTrace()
            }
        }
        document.set(
            databasePost.copy(
                postId = document.id,
                attachedImages = imageDownloadLinks
            )
        )
        document.update("createdAt", FieldValue.serverTimestamp()).await()
    }

    /**
     * 投稿を削除します
     */
    override suspend fun deletePost(postId: String) {
        database.collection(COLLECTION).document(postId).delete()
    }

    /**
     * 最新の投稿を取得します
     */
    override suspend fun fetchNewerPosts(from: Date): List<DatabasePost> {
        val response = database.collection(COLLECTION)
            .whereLessThanOrEqualTo("createdAt", from)
            .orderBy("createdAt", Query.Direction.DESCENDING)
            .limit(DEFAULT_LOAD_LIMIT)
            .get().await()
        return response.toObjects(DatabasePost::class.java)
    }

    /**
     * 古い投稿を取得します
     */
    override suspend fun fetchOlderPosts(from: Date): List<DatabasePost> {
        val response = database.collection(COLLECTION)
            .whereLessThanOrEqualTo("createdAt", from)
            .orderBy("createdAt", Query.Direction.DESCENDING)
            .limit(DEFAULT_LOAD_LIMIT)
            .get().await()
        return response.toObjects(DatabasePost::class.java)
    }

}