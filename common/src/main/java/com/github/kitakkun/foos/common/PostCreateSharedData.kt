package com.github.kitakkun.foos.common

import com.google.android.gms.maps.model.LatLng

data class PostCreateSharedData(
    val location: LatLng? = null,
    val locationName: String? = null,
)
