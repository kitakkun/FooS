package com.example.foos.ui.view.screen.map

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.foos.data.domain.fetcher.post.FetchPostsByLocationBoundsUseCase
import com.example.foos.ui.state.component.PostItemUiState
import com.example.foos.ui.state.screen.map.MapScreenUiState
import com.google.android.gms.maps.model.LatLngBounds
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MapViewModel @Inject constructor(
    private val fetchPostsByLocationBoundsUseCase: FetchPostsByLocationBoundsUseCase,
) : ViewModel() {

    private var _uiState = mutableStateOf(
        MapScreenUiState(
            listOf(), PostItemUiState.Default
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
                val newPosts = fetchPostsByLocationBoundsUseCase(it).map { PostItemUiState.convert(it) }
                _uiState.value = uiState.value.copy(
                    posts = (uiState.value.posts + newPosts).distinct()
                )
            }
        }
    }
}