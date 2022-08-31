package com.example.foos.ui.view.screen.map

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.foos.data.domain.ConvertPostWithUserToUiStateUseCase
import com.example.foos.data.domain.GetPostWithUserByPostIdUseCase
import com.example.foos.data.model.DatabaseUser
import com.example.foos.data.model.PostWithUser
import com.example.foos.data.repository.PostsRepository.fetchByLatLngBounds
import com.example.foos.data.repository.UsersRepository
import com.example.foos.ui.navigation.SubScreen
import com.example.foos.ui.state.screen.home.PostItemUiState
import com.example.foos.ui.state.screen.map.MapScreenUiState
import com.google.android.gms.maps.model.LatLngBounds
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.joinAll
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MapViewModel @Inject constructor(
    private val usersRepository: UsersRepository,
    private val getPostWithUserByPostIdUseCase: GetPostWithUserByPostIdUseCase,
    private val convertPostWithUserToUiStateUseCase: ConvertPostWithUserToUiStateUseCase
) : ViewModel() {

    private var _uiState = mutableStateOf(
        MapScreenUiState(
            listOf(), PostItemUiState(
                userId = "userId",
                username = "username",
                postId = "",
                content = "content",
                attachedImages = listOf(""),
                userIcon = "",
            )
        )
    )
    val uiState: State<MapScreenUiState> = _uiState

    private var _navEvent = MutableSharedFlow<String>()
    val navEvent = _navEvent.asSharedFlow()

    fun showPostDetail(postItemUiState: PostItemUiState) {
        _uiState.value = uiState.value.copy(focusingPost = postItemUiState)
    }

    fun fetchNearbyPosts(viewBounds: LatLngBounds?) {
        viewModelScope.launch {
            viewBounds?.let {
                val posts = fetchByLatLngBounds(it)
                val tasks = mutableListOf<Job>()
                val users = mutableMapOf<String, DatabaseUser?>()
                posts.map {
                    tasks.add(async {
                        users.put(
                            it.postId,
                            usersRepository.fetchByUserId(it.userId)
                        )
                    })
                }
                tasks.joinAll()
                val newPosts = posts.mapNotNull { post ->
                    users[post.postId]?.let { user ->
                        convertPostWithUserToUiStateUseCase(PostWithUser(user, post))
                    }
                }
                _uiState.value = uiState.value.copy(
                    posts = (uiState.value.posts + newPosts).distinct()
                )
            }
        }
    }
}