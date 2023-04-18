package com.github.kitakkun.foos.post.create

import com.google.android.gms.maps.model.LatLng


data class PostEditUiState(
    val title: String = "",
    val content: String = "",
    val isLoading: Boolean = false,
    val location: LatLng? = null,
    val locationName: String? = null,
    val attachedImageUrls: List<String> = emptyList(),
)
