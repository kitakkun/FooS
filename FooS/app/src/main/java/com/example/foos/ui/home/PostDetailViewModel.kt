package com.example.foos.ui.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.foos.data.domain.GetPostWithUserByPostIdUseCase
import com.example.foos.data.model.PostWithUser
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PostDetailViewModel @Inject constructor(
    private val getPostWithUserByPostIdUseCase: GetPostWithUserByPostIdUseCase
) : ViewModel() {

    private lateinit var postId: String
    private var postWithUser: PostWithUser? = null

    private var _uiState = MutableStateFlow(PostDetailScreenUiState(PostItemUiState.Default))
    val uiState: StateFlow<PostDetailScreenUiState> get() = _uiState

    fun setPostId(postId: String) {
        this.postId = postId
        viewModelScope.launch(Dispatchers.IO) {
            getPostWithUserByPostIdUseCase(postId)?.let {
                _uiState.update { state ->
                    state.copy(
                        postItemUiState = PostItemUiState(
                            userId = it.user.userId,
                            username = it.user.username,
                            userIcon = it.user.profileImage,
                            content = it.post.content,
                            attachedImages = it.post.attachedImages,
                        )
                    )
                }
            }
        }
    }


}