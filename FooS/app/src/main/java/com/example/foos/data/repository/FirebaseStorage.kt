package com.example.foos.data.repository

import android.net.Uri
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import kotlinx.coroutines.tasks.await
import java.io.File

/**
 * Firebaseのクラウドストレージを簡単に扱えるようにするためのオブジェクト
 */
object FirebaseStorage {

    /**
     * 指定のリソースを作成（アップロード）します
     * @param remotePath クラウドストレージ上のパス
     * @param localPath アップロードするファイルのパス
     * @return アップロードしたリソースのクラウド上のパス
     */
    suspend fun create(remotePath: String, localPath: String): Uri {
        val file = Uri.fromFile(File(localPath))
        val ref = Firebase.storage.reference.child(remotePath)
        ref.putFile(file).await()
        return ref.downloadUrl.await()
    }

    /**
     * クラウド上の指定のリソースを削除します
     * @param remotePath 削除するリソースのパス
     */
    suspend fun delete(remotePath: String) {
        Firebase.storage.reference.child(remotePath).delete().await()
    }
}