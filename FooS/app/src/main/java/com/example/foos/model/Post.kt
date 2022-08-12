package com.example.foos.model

import com.google.android.gms.maps.model.LatLng

data class Post(
    val username: String,
    val userIconUrl: String,
    val content: String,
    val ImageUrls: List<String>,
    val locationName: String,
    val location: LatLng,
)
