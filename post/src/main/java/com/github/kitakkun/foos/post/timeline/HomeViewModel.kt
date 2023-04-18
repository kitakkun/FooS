package com.github.kitakkun.foos.post.timeline

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.github.kitakkun.foos.common.usecase.FetchPostsUseCase
import com.github.kitakkun.foos.customview.composable.post.PostItemUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * HomeScreenに対応するViewModel
 */
@HiltViewModel
class HomeViewModel @Inject constructor(
    private val fetchPostsUseCase: FetchPostsUseCase,
) : ViewModel() {

    companion object {
        private const val TAG = "HomeViewModel"
    }

    private var mutableUiState = MutableStateFlow(HomeScreenUiState.Default)
    val uiState = mutableUiState.asStateFlow()

    fun refreshPosts() {
        viewModelScope.launch(Dispatchers.IO) {
            mutableUiState.update { it.copy(isRefreshing = true) }
            val posts = fetchPostsUseCase().map { PostItemUiState.convert(it) }
            mutableUiState.update { it.copy(posts = posts, isRefreshing = false) }
        }
    }

    fun fetchInitialPosts() {
        viewModelScope.launch(Dispatchers.IO) {
            mutableUiState.update { it.copy(isLoading = true) }
            val posts = fetchPostsUseCase().map { post ->
                PostItemUiState.convert(post)
            }
            Log.d(TAG, "fetchInitialPosts: $posts")
            mutableUiState.update {
                it.copy(isLoading = false, posts = (posts + uiState.value.posts).distinct())
            }
        }
    }

    fun fetchOlderPosts() {
        viewModelScope.launch {
            val oldestPost = uiState.value.posts.last()
            val oldestDate = oldestPost.createdAt
            oldestDate?.let {
                val posts = fetchPostsUseCase(end = it).map { post ->
                    PostItemUiState.convert(post)
                }
                mutableUiState.update {
                    it.copy(posts = (uiState.value.posts + posts).distinct())
                }
            }
        }
    }

    fun dispose() {
        viewModelScope.cancel()
    }
}
