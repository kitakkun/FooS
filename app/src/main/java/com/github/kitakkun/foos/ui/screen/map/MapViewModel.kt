package com.github.kitakkun.foos.ui.screen.map

import androidx.compose.runtime.State
import com.github.kitakkun.foos.customview.composable.post.PostItemUiState
import com.google.android.gms.maps.model.LatLngBounds
import kotlinx.coroutines.flow.SharedFlow

interface MapViewModel {
    val uiState: State<MapScreenUiState>
    val navEvent: SharedFlow<String>
    fun showPostDetail(postItemUiState: PostItemUiState)
    fun fetchNearbyPosts(viewBounds: LatLngBounds?)
}