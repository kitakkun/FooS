package com.example.foos.ui.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.foos.data.posts.PostsRepository
import com.example.foos.model.Post
import com.example.foos.ui.composable.HomeUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class HomeViewModel @Inject constructor(
    private val postsRepository: PostsRepository
) : ViewModel() {

    private val postList: StateFlow<List<Post>> = postsRepository.allPosts

    val homeUiState = MutableStateFlow(HomeUiState(false, postList.value))

    fun fetchNewerPosts() {
        viewModelScope.launch {
            postsRepository.fetchNewerPosts()
        }
    }

    fun fetchOlderPosts() {
        viewModelScope.launch {
            postsRepository.fetchOlderPosts()
        }
    }


}