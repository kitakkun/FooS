package com.example.foos.ui.home

import android.util.Log
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.foos.Posts
import com.example.foos.data.repository.CombinedPostsRepository
import com.example.foos.data.repository.PostsRepository
import com.example.foos.data.repository.model.PostData
import com.example.foos.ui.post.PostUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class HomeViewModel @Inject constructor(
    private val combinedPostsRepository: CombinedPostsRepository
) : ViewModel() {


    val homeUiState = MutableStateFlow(HomeUiState(Posts.getPosts(), false))

    fun fetchNewerPosts() {
        viewModelScope.launch {
            homeUiState.update { it.copy(isRefreshing = true) }
            val posts = combinedPostsRepository.fetchPosts().map {
                PostItemUiState(
                    userId = it.userData.userId,
                    username = it.userData.username,
                    userIcon = it.userData.profileImage,
                    content = it.postData.content,
                    attachedImages = it.postData.attachedImages,
                )
            }
            Log.d("TAG", posts.size.toString())

            homeUiState.update { it.copy(posts = (posts + it.posts).distinct(), isRefreshing = false) }
        }
    }

    fun fetchOlderPosts() {
        viewModelScope.launch {
//            postsRepository.fetchOlderPosts()
        }
    }


}