package com.example.foos.ui.state.screen.locationconfirm

import com.google.android.gms.maps.model.LatLng

data class LocationConfirmScreenUiState(
    val location: LatLng,
    val locationName: String,
)
