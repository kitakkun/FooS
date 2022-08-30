package com.example.foos.ui.view.screen.locationconfirm

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.foos.ui.state.screen.locationconfirm.LocationConfirmScreenUiState
import com.google.android.gms.maps.model.LatLng
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LocationConfirmViewModel @Inject constructor() : ViewModel() {

    private var _uiState = mutableStateOf(LocationConfirmScreenUiState(LatLng(0.0, 0.0), ""))
    val uiState: State<LocationConfirmScreenUiState> = _uiState

    private var _cancelEvent = MutableSharedFlow<Unit>()
    val cancelEvent = _cancelEvent.asSharedFlow()

    private var _completeEvent = MutableSharedFlow<Unit>()
    val completeEvent = _completeEvent.asSharedFlow()

    fun backToPrevScreen() {
        viewModelScope.launch {
            _cancelEvent.emit(Unit)
        }
    }

    fun completeStep() {
        viewModelScope.launch {
            _completeEvent.emit(Unit)
        }
    }

    fun updateLocationName(newName: String) {
        if (!newName.contains("\n")) {
            _uiState.value = uiState.value.copy(locationName = newName)
        }
    }

}