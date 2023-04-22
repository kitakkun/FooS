package com.github.kitakkun.foos.common.repository

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import com.github.kitakkun.foos.common.ext.join
import com.github.kitakkun.foos.common.model.DatabasePost
import com.google.android.gms.maps.model.LatLngBounds
import com.google.android.gms.tasks.CancellationTokenSource
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.google.firebase.storage.FirebaseStorage
import kotlinx.coroutines.tasks.await
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.util.*

/**
 * 投稿内容のデータを管理するリポジトリ
 */
class PostsRepositoryImpl(
    private val database: FirebaseFirestore,
    private val storage: FirebaseStorage,
) : PostsRepository {

    companion object {
        const val DEFAULT_LOAD_LIMIT: Long = 15
        private const val MAX_UPLOAD_IMAGE_SIZE = 1024
        private const val COLLECTION = "posts"
    }

    private val cancellationTokenSources: MutableList<CancellationTokenSource> = mutableListOf()

    private fun issueQueryWithCancelToken(
        query: Query,
    ) {
        val cancellationTokenSource = CancellationTokenSource()
        cancellationTokenSources.add(cancellationTokenSource)

    }

    /**
     * 作成日時以外フィルタなしの通常のフェッチ
     */
    override suspend fun fetch(start: Date?, end: Date?, count: Long?): List<DatabasePost> {
//        val cancellationTokenSource = CancellationTokenSource()
//        cancellationTokenSources.add(cancellationTokenSource)
        val result = database.collection(COLLECTION)
            .orderBy("createdAt", Query.Direction.DESCENDING)
            .let { if (start != null) it.whereGreaterThanOrEqualTo("createdAt", start) else it }
            .let { if (end != null) it.whereLessThanOrEqualTo("createdAt", end) else it }
            .let { if (count != null) it.limit(count) else it }
            .get()
            .await()
            .toObjects(DatabasePost::class.java)
//        cancellationTokenSources.remove(cancellationTokenSource)
        return result
    }

    /**
     * ユーザIDでフィルタしてフェッチ
     */
    override suspend fun fetchByUserId(
        userId: String,
        start: Date?,
        end: Date?,
        count: Long?
    ): List<DatabasePost> =
        database.collection(COLLECTION)
            .whereEqualTo("userId", userId)
            .orderBy("createdAt", Query.Direction.DESCENDING)
            .let { if (start != null) it.whereGreaterThanOrEqualTo("createdAt", start) else it }
            .let { if (end != null) it.whereLessThanOrEqualTo("createdAt", end) else it }
            .let { if (count != null) it.limit(count) else it }
            .get().await().toObjects(DatabasePost::class.java)

    /**
     * ユーザIDでフィルタしてフェッチ（複数版）
     */
    override suspend fun fetchByUserIds(
        userIds: List<String>,
        start: Date?,
        end: Date?,
        count: Long?
    ): List<DatabasePost> =
        if (userIds.isEmpty()) listOf()
        else if (userIds.size > 10)
            userIds.chunked(10).map { fetchByUserIds(it, start, end, count) }.join()
        else database.collection(COLLECTION)
            .whereIn("userId", userIds)
            .orderBy("createdAt", Query.Direction.DESCENDING)
            .let { if (start != null) it.whereGreaterThanOrEqualTo("createdAt", start) else it }
            .let { if (end != null) it.whereLessThanOrEqualTo("createdAt", end) else it }
            .let { if (count != null) it.limit(count) else it }
            .get().await().toObjects(DatabasePost::class.java)

    /**
     * 投稿IDでフェッチ
     */
    override suspend fun fetchByPostId(postId: String): DatabasePost? =
        database.collection(COLLECTION)
            .whereEqualTo("postId", postId)
            .get().await().toObjects(DatabasePost::class.java).let {
                if (it.size > 0) it[0]
                else null
            }

    /**
     * 投稿IDで複数まとめてフェッチ
     */
    override suspend fun fetchByPostIds(postIds: List<String>): List<DatabasePost> =
        if (postIds.isEmpty()) listOf()
        else if (postIds.size > 10) postIds.chunked(10).map { fetchByPostIds(it) }.join()
        else database.collection(COLLECTION)
            .whereIn("postId", postIds)
            .get().await().toObjects(DatabasePost::class.java)

    /**
     * 画像付きの投稿をフェッチ
     */
    override suspend fun fetchPostsWithMediaByUserId(
        userId: String,
        start: Date?,
        end: Date?,
        count: Long?,
    ): List<DatabasePost> =
        database.collection(COLLECTION)
            .whereEqualTo("userId", userId)
            .orderBy("createdAt", Query.Direction.DESCENDING)
            .let { if (start != null) it.whereGreaterThanOrEqualTo("createdAt", start) else it }
            .let { if (end != null) it.whereLessThanOrEqualTo("createdAt", end) else it }
            .let { if (count != null) it.limit(count) else it }
            .get().await().toObjects(DatabasePost::class.java)
            .filter { it.attachedImages.isNotEmpty() }

    /**
     * 位置情報範囲を指定して投稿をフェッチ
     */
    override suspend fun fetchByLatLngBounds(bounds: LatLngBounds): List<DatabasePost> =
        database.collection(COLLECTION)
            .whereLessThanOrEqualTo("longitude", bounds.northeast.longitude)
            .whereGreaterThanOrEqualTo("longitude", bounds.southwest.longitude)
            .get().await().toObjects(DatabasePost::class.java).filter {
                it.latitude!! <= bounds.northeast.latitude && it.latitude as Double >= bounds.southwest.latitude
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

}
