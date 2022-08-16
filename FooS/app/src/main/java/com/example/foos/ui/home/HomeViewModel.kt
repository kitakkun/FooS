package com.example.foos.ui.home

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.foos.Posts
import com.example.foos.data.repository.PostsRepository
import com.example.foos.model.PostData
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class HomeViewModel @Inject constructor(
    private val postsRepository: PostsRepository
) : ViewModel() {

    private var _isRefreshing = mutableStateOf(false)
    val isRefreshing: MutableState<Boolean> get() = _isRefreshing

    private val postList: StateFlow<List<PostData>> = postsRepository.allPosts

    val homeUiState = MutableStateFlow(HomeUiState(Posts.getPosts()))

    fun fetchNewerPosts() {
        _isRefreshing.value = true
        viewModelScope.launch {
            postsRepository.fetchNewerPosts()
            _isRefreshing.value = false
        }
    }

    fun fetchOlderPosts() {
        viewModelScope.launch {
            postsRepository.fetchOlderPosts()
        }
    }


}