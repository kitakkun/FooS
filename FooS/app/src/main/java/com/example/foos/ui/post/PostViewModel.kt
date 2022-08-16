package com.example.foos.ui.post

import android.net.Uri
import android.util.Log
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.foos.data.repository.PostDao
import com.example.foos.model.PostData
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject
import kotlin.coroutines.suspendCoroutine

@HiltViewModel
class PostViewModel @Inject constructor(
    private val dao: PostDao
) : ViewModel() {

    private var _postUiState = MutableStateFlow(PostUiState("", listOf()))
    val postUiState: StateFlow<PostUiState> get() = _postUiState.asStateFlow()

    fun setImages(imageUris: List<Uri>) {
        _postUiState.update { it.copy(attachedImages = (it.attachedImages + imageUris.map { uri -> uri.toString() }).distinct()) }
    }

    fun onTextFieldUpdated(text: String) {
        _postUiState.update { it.copy(content = text) }
    }

    fun post() {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                dao.insert(PostData(Firebase.auth.uid.toString(), _postUiState.value.content, listOf()))
            }
        }
    }
}