package com.example.foos.ui.post

import android.app.Application
import android.content.ContentResolver
import android.content.Context
import android.database.Cursor
import android.net.Uri
import android.provider.MediaStore
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.foos.FileUtils.getRealPath
import com.example.foos.data.repository.PostsRepository
import com.example.foos.data.model.Post
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PostViewModel @Inject constructor(
    private val postContentsRepository: PostsRepository,
    application: Application
) : AndroidViewModel(application) {

    private var _postUiState = MutableStateFlow(PostScreenUiState("", listOf()))
    val postUiState: StateFlow<PostScreenUiState> get() = _postUiState.asStateFlow()

    fun setImages(context: Context, imageUris: List<Uri>) {
        Log.d("IMAGE_URI", imageUris[0].toString())
        Log.d("IMAGE_URI", getRealPath(context, imageUris[0]) ?: "NULL")
        _postUiState.update { it.copy(attachedImages = (
                it.attachedImages + imageUris.map { uri -> "file://" + getRealPath(context, uri) }).filterNotNull().distinct()) }
//        _postUiState.update { it.copy(attachedImages = (
//                it.attachedImages + imageUris.map { uri -> uri.toString() }).distinct()) }
    }

    /**
     * URIからファイルPATHを取得する.
     * @param uri URI
     * @return ファイルPATH
     */
    private fun getPath(uri: Uri): String {
        var path = uri.toString()
        if (path.matches(Regex("^file:.*"))) {
            return path.replaceFirst("file://".toRegex(), "")
        } else if (!path.matches(Regex("^content:.*"))) {
            return path
        }
        val context: Context = getApplication() as Context
        val contentResolver: ContentResolver = context.contentResolver
        val columns = arrayOf(MediaStore.Images.Media.DATA)
        val cursor: Cursor? = contentResolver.query(uri, columns, null, null, null)
        if (cursor != null) {
            if (cursor.count > 0) {
                cursor.moveToFirst()
                path = cursor.getString(0)
            }
            cursor.close()
        }
        return path
    }

    fun convertUriToFile(context: Context, uri: Uri?): String? {
        if (uri == null) {
            return null
        }
        val scheme = uri.scheme
        var path: String? = null
        if ("file" == scheme) {
            path = uri.path
        } else if ("content" == scheme) {
            val contentResolver = context.contentResolver
            val cursor = contentResolver.query(uri, arrayOf(MediaStore.MediaColumns.DATA), null, null, null)
            if (cursor != null) {
                cursor.moveToFirst()
                path = cursor.getString(0)
                cursor.close()
            }
        }
        return path
//        return if (null == path) null else File(path)
    }

    fun onTextFieldUpdated(text: String) {
        _postUiState.update { it.copy(content = text) }
    }

    fun post() {
        viewModelScope.launch (Dispatchers.IO) {
            val postData = Post(
                "", Firebase.auth.uid.toString(), _postUiState.value.content,
                _postUiState.value.attachedImages, null, null, java.util.Date()
            )
            postContentsRepository.createPost(postData)
        }
    }
}