package com.example.foos.ui.state.screen.locationselect

import com.google.android.gms.maps.model.LatLng

data class LocationSelectScreenUiState(
    val pinPosition: LatLng? = null,
)
