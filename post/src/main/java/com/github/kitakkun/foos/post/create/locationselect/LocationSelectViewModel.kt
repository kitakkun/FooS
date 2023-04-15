package com.github.kitakkun.foos.post.create.locationselect

import androidx.compose.runtime.State
import com.google.android.gms.maps.model.LatLng
import kotlinx.coroutines.flow.SharedFlow

interface LocationSelectViewModel {
    val uiState: State<LocationSelectScreenUiState>
    val navToNextEvent: SharedFlow<String>
    val cancelNavEvent: SharedFlow<Unit>
    fun updatePinPosition(position: LatLng)
    fun cancel()
    fun navigateToConfirmScreen()
}
