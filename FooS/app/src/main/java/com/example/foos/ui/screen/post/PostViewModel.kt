package com.example.foos.ui.screen.post

import android.app.Application
import android.content.ContentResolver
import android.content.Context
import android.database.Cursor
import android.net.Uri
import android.provider.MediaStore
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavController
import com.example.foos.FileUtils.getRealPath
import com.example.foos.data.model.Post
import com.example.foos.data.repository.PostsRepository
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class PostViewModel @Inject constructor(
    private val postsRepository: PostsRepository,
    application: Application
) : AndroidViewModel(application) {

    private var _uiState = MutableStateFlow(PostScreenUiState("", listOf(), false))
    val postUiState: StateFlow<PostScreenUiState> get() = _uiState.asStateFlow()

    fun setImages(context: Context, imageUris: List<Uri>) {
        Log.d("IMAGE_URI", imageUris[0].toString())
        Log.d("IMAGE_URI", getRealPath(context, imageUris[0]) ?: "NULL")
        _uiState.update {
            it.copy(attachedImages = (
                    it.attachedImages + imageUris.map { uri ->
                        "file://" + getRealPath(
                            context,
                            uri
                        )
                    }).filterNotNull().distinct()
            )
        }
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
            val cursor =
                contentResolver.query(uri, arrayOf(MediaStore.MediaColumns.DATA), null, null, null)
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
        _uiState.update { it.copy(content = text) }
    }

    fun post(navController: NavController) {
        viewModelScope.launch(Dispatchers.IO) {
            _uiState.update { it.copy(posting = true) }
            val post = Post(
                "", Firebase.auth.uid.toString(), _uiState.value.content,
                _uiState.value.attachedImages, null, null, java.util.Date()
            )
            postsRepository.createPost(post)
            withContext(Dispatchers.Main) {
                navController.navigateUp()
            }
            _uiState.update { it.copy(posting = false) }
        }
    }
}