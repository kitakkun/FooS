package com.example.foos.ui.view.screen.map

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.foos.data.repository.PostsRepository
import com.example.foos.data.repository.PostsRepository.fetchByLatLngBounds
import com.example.foos.ui.navigation.SubScreen
import com.example.foos.ui.state.screen.map.MapScreenUiState
import com.google.android.gms.maps.model.LatLngBounds
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MapViewModel @Inject constructor(
    private val postsRepository: PostsRepository
) : ViewModel() {

    private var _uiState = mutableStateOf(MapScreenUiState(listOf()))
    val uiState: State<MapScreenUiState> = _uiState

    private var _navEvent = MutableSharedFlow<String>()
    val navEvent = _navEvent.asSharedFlow()

    fun navigateToPost(postId: String) {
        viewModelScope.launch {
            _navEvent.emit("${SubScreen.PostDetail.route}/$postId")
        }
    }

    /**
     *
     */
    fun fetchNearbyPosts(viewBounds: LatLngBounds?) {
        viewModelScope.launch {
            viewBounds?.let {
                _uiState.value = uiState.value.copy(
                    posts = (uiState.value.posts + fetchByLatLngBounds(bounds = it)).distinct()
                )

            }
        }
    }
}