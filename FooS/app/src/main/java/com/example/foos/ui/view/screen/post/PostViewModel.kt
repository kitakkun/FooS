package com.example.foos.ui.view.screen.post

import android.app.Application
import android.content.Context
import android.net.Uri
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.foos.data.model.DatabasePost
import com.example.foos.data.repository.PostsRepository
import com.example.foos.ui.state.screen.post.PostScreenUiState
import com.example.foos.util.FileUtils.getRealPath
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PostViewModel @Inject constructor(
    private val postsRepository: PostsRepository,
    application: Application
) : AndroidViewModel(application) {

    private var _uiState = MutableStateFlow(PostScreenUiState("", listOf()))
    val postUiState: StateFlow<PostScreenUiState> get() = _uiState.asStateFlow()

    private var _navUpEvent = MutableSharedFlow<Boolean>()
    val navUpEvent = _navUpEvent.asSharedFlow()

    fun setImages(context: Context, imageUris: List<Uri>) {
        _uiState.update {
            it.copy(
                attachedImages = (it.attachedImages + imageUris.map { uri ->
                    "file://" + getRealPath(context, uri)
                }).distinct()
            )
        }
    }

    fun onTextFieldUpdated(text: String) {
        _uiState.update { it.copy(content = text) }
    }

    /**
     * 投稿処理を行う
     */
    fun post() {
        MainScope().launch(Dispatchers.IO) {
            val databasePost = DatabasePost(
                "", Firebase.auth.uid.toString(), _uiState.value.content,
                _uiState.value.attachedImages, null, null, java.util.Date()
            )
            postsRepository.create(databasePost, getApplication())
        }
        viewModelScope.launch {
            _navUpEvent.emit(true)
        }
    }
}