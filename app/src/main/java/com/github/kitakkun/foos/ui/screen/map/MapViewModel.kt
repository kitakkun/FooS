package com.github.kitakkun.foos.ui.screen.map

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.github.kitakkun.foos.common.usecase.FetchPostsByLocationBoundsUseCase
import com.github.kitakkun.foos.customview.composable.post.PostItemUiState
import com.google.android.gms.maps.model.LatLngBounds
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MapViewModel @Inject constructor(
    private val fetchPostsByLocationBoundsUseCase: FetchPostsByLocationBoundsUseCase,
) : ViewModel() {
    private val mutableUiState =
        MutableStateFlow(MapScreenUiState(listOf(), PostItemUiState.Default))
    val uiState = mutableUiState.asStateFlow()

    fun showPostDetail(postItemUiState: PostItemUiState) {
        mutableUiState.value = uiState.value.copy(focusingPost = postItemUiState)
    }

    fun fetchNearbyPosts(viewBounds: LatLngBounds?) = viewModelScope.launch {
        viewBounds ?: return@launch
        val newPosts =
            fetchPostsByLocationBoundsUseCase(viewBounds).map { PostItemUiState.convert(it) }
        mutableUiState.value = uiState.value.copy(
            posts = (uiState.value.posts + newPosts).distinct()
        )
    }
}
