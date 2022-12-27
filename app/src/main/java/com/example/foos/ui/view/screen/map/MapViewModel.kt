package com.example.foos.ui.view.screen.map

import androidx.compose.runtime.State
import com.example.foos.ui.state.component.PostItemUiState
import com.example.foos.ui.state.screen.map.MapScreenUiState
import com.google.android.gms.maps.model.LatLngBounds
import kotlinx.coroutines.flow.SharedFlow

interface MapViewModel {
    val uiState: State<MapScreenUiState>
    val navEvent: SharedFlow<String>
    fun showPostDetail(postItemUiState: PostItemUiState)
    fun fetchNearbyPosts(viewBounds: LatLngBounds?)
}