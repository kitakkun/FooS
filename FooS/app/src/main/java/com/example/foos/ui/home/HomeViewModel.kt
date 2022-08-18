package com.example.foos.ui.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.foos.Posts
import com.example.foos.data.domain.GetLatestPostsWithUserUseCase
import com.example.foos.data.domain.GetOlderPostsWithUserUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class HomeViewModel @Inject constructor(
    private val getLatestPostsWithUserUseCase: GetLatestPostsWithUserUseCase,
    private val getOlderPostsWithUserUseCase: GetOlderPostsWithUserUseCase,
) : ViewModel() {

    private var _uiState = MutableStateFlow(HomeScreenUiState(Posts.getPosts(), false))
    val uiState: StateFlow<HomeScreenUiState> get() = _uiState

    fun fetchNewerPosts() {
        viewModelScope.launch(Dispatchers.IO) {
            _uiState.update { it.copy(isRefreshing = true) }
            val posts = getLatestPostsWithUserUseCase().map {
                PostItemUiState(
                    userId = it.user.userId,
                    username = it.user.username,
                    userIcon = it.user.profileImage,
                    content = it.post.content,
                    attachedImages = it.post.attachedImages,
                )
            }
            _uiState.update {
                it.copy(
                    posts = (posts + it.posts).distinct(),
                    isRefreshing = false
                )
            }
        }
    }

    fun fetchOlderPosts() {
        viewModelScope.launch {
//            postsRepository.fetchOlderPosts()
        }
    }


}