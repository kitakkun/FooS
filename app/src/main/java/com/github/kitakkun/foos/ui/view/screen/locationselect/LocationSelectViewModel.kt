package com.github.kitakkun.foos.ui.view.screen.locationselect

import androidx.compose.runtime.State
import com.github.kitakkun.foos.ui.state.screen.locationselect.LocationSelectScreenUiState
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
