package com.example.foos.ui.state.screen.post

import com.google.android.gms.maps.model.LatLng

data class PostScreenUiState(
    var content: String,
    val attachedImages: List<String>,
    val location: LatLng?,
    val locationName: String?,
)