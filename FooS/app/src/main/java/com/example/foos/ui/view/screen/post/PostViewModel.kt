package com.example.foos.ui.view.screen.post

import android.app.Application
import android.content.Context
import android.net.Uri
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavController
import com.example.foos.util.FileUtils.getRealPath
import com.example.foos.data.model.DatabasePost
import com.example.foos.data.repository.PostsRepository
import com.example.foos.ui.state.screen.post.PostScreenUiState
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.MainScope
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

    private var _uiState = MutableStateFlow(PostScreenUiState("", listOf()))
    val postUiState: StateFlow<PostScreenUiState> get() = _uiState.asStateFlow()

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

    fun post(navController: NavController) {
        MainScope().launch(Dispatchers.IO) {
            val databasePost = DatabasePost(
                "", Firebase.auth.uid.toString(), _uiState.value.content,
                _uiState.value.attachedImages, null, null, java.util.Date()
            )
            postsRepository.create(databasePost, getApplication())
        }
        navController.navigateUp()
    }
}