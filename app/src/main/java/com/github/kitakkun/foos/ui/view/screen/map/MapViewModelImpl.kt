package com.github.kitakkun.foos.ui.view.screen.map

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.github.kitakkun.foos.data.domain.fetcher.post.FetchPostsByLocationBoundsUseCase
import com.github.kitakkun.foos.ui.state.component.PostItemUiState
import com.github.kitakkun.foos.ui.state.screen.map.MapScreenUiState
import com.google.android.gms.maps.model.LatLngBounds
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MapViewModelImpl @Inject constructor(
    private val fetchPostsByLocationBoundsUseCase: FetchPostsByLocationBoundsUseCase,
) : ViewModel(), MapViewModel {

    private var _uiState = mutableStateOf(
        MapScreenUiState(
            listOf(), PostItemUiState.Default
        )
    )
    override val uiState: State<MapScreenUiState> = _uiState

    private var _navEvent = MutableSharedFlow<String>()
    override val navEvent = _navEvent.asSharedFlow()

    override fun showPostDetail(postItemUiState: PostItemUiState) {
        _uiState.value = uiState.value.copy(focusingPost = postItemUiState)
    }

    override fun fetchNearbyPosts(viewBounds: LatLngBounds?) {
        viewModelScope.launch {
            viewBounds?.let {
                val newPosts =
                    fetchPostsByLocationBoundsUseCase(it).map { PostItemUiState.convert(it) }
                _uiState.value = uiState.value.copy(
                    posts = (uiState.value.posts + newPosts).distinct()
                )
            }
        }
    }
}
