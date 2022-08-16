package com.example.foos.data.repository

import com.example.foos.model.PostData
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.tasks.await

/**
 * 投稿のデータベースへの登録、変更、削除を行う
 */
class PostDao {

    /**
     * 投稿をデータベースへ登録
     */
    suspend fun insert(postData: PostData) {
        Firebase.firestore.collection("posts")
            .add(postData).await()
    }

    suspend fun delete(postData: PostData) {

    }
}