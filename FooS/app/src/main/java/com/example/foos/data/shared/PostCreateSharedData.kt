package com.example.foos.data.shared

import com.google.android.gms.maps.model.LatLng

data class PostCreateSharedData(
    val location: LatLng? = null,
    val locationName: String? = null,
)