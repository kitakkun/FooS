package com.example.foos.data.repository

import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.tasks.await

object FirestoreDao {

    const val COLLECTION_POSTS = "posts"
    const val COLLECTION_USERS = "users"
    const val COLLECTION_REACTIONS = "reactions"

    /**
     * ドキュメント参照を作成します
     * @param collection コレクション名
     * @param document ドキュメント名（省略すると新規作成）
     */
    fun createDocumentReference(
        collection: String,
        document: String? = null
    ): DocumentReference {
        val collectionRef = Firebase.firestore.collection(collection)
        return if (document == null) collectionRef.document()
        else collectionRef.document(document)
    }

    /**
     * 指定コレクション内の指定ドキュメントを削除します
     * @param collection コレクション名
     * @param document ドキュメント名
     */
    suspend fun delete(collection: String, document: String) {
        Firebase.firestore.collection(collection).document(document).delete().await()
    }

}