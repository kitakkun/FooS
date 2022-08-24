package com.example.foos.data.repository

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import androidx.compose.ui.platform.LocalContext
import coil.decode.BitmapFactoryDecoder
import com.example.foos.data.model.DatabasePost
import com.example.foos.util.ImageConverter
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.tasks.await
import java.io.*

/**
 * 投稿内容のデータを管理するリポジトリ
 */
object PostsRepository {

    private const val MAX_LOAD_COUNT: Long = 10
    private const val COLLECTION = "posts"

    /**
     * 投稿を取得します
     */
    suspend fun fetchPost(postId: String): DatabasePost? {
        val document = Firebase.firestore.document(postId)
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
            val resized = ImageConverter.resize(bitmap, 1080, true)
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
            .limit(MAX_LOAD_COUNT)
            .get().await()
        return response.toObjects(DatabasePost::class.java)
    }

}