package com.example.foos.ui.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.foos.Posts
import com.example.foos.data.repository.CombinedPostsRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject


@HiltViewModel
class HomeViewModel @Inject constructor(
    private val combinedPostsRepository: CombinedPostsRepository
) : ViewModel() {

    val homeScreenUiState = MutableStateFlow(HomeScreenUiState(Posts.getPosts(), false))

    fun fetchNewerPosts() {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                homeScreenUiState.update { it.copy(isRefreshing = true) }
                val posts = combinedPostsRepository.fetchPosts().map {
                    PostItemUiState(
                        userId = it.userData.userId,
                        username = it.userData.username,
                        userIcon = it.userData.profileImage,
                        content = it.postContentData.content,
                        attachedImages = it.postContentData.attachedImages,
                    )
                }
                homeScreenUiState.update { it.copy(posts = (posts + it.posts).distinct(), isRefreshing = false) }
            }
        }
    }

    fun fetchOlderPosts() {
        viewModelScope.launch {
//            postsRepository.fetchOlderPosts()
        }
    }


}