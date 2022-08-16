package com.example.foos.data.repository

import android.content.ContentResolver
import android.content.Context
import android.database.Cursor
import android.net.Uri
import android.provider.MediaStore
import com.example.foos.data.repository.model.PostContentData
import com.firebase.ui.auth.AuthUI.getApplicationContext
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.tasks.await
import java.io.File


/**
 * 投稿のデータベースへの登録、変更、削除を行う
 */
class PostDao {

    /**
     * 投稿をデータベースへ登録
     */
    suspend fun insert(postContentData: PostContentData) {
        val docRef = Firebase.firestore.collection("posts").document()
        val imageUrls = mutableListOf<String>()
        // アップロードしてダウンロードリンクを取得
        for (i in postContentData.attachedImages.indices) {
            val file = Uri.fromFile(File(postContentData.attachedImages[i].removePrefix("file://")))
            val ref = Firebase.storage.reference.child("images/posts/${docRef.id}/${file.lastPathSegment}")
            val uploadTask = ref.putFile(file)
            uploadTask.await()
            imageUrls.add(ref.downloadUrl.await().toString())
        }
        Firebase.firestore.collection("posts")
            .add(postContentData.copy(postId = docRef.id, attachedImages = imageUrls)).await()
    }

    suspend fun delete(postContentData: PostContentData) {

    }
}