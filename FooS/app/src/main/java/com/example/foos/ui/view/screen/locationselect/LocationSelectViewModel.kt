package com.example.foos.ui.view.screen.locationselect

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.foos.ui.state.screen.locationselect.LocationSelectScreenUiState
import com.example.foos.ui.view.screen.Page
import com.google.android.gms.maps.model.LatLng
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LocationSelectViewModel @Inject constructor(): ViewModel(){

    private var _uiState = MutableStateFlow(LocationSelectScreenUiState(LatLng(0.0,0.0)))
    val uiState = _uiState.asStateFlow()

    private var _navEvent = MutableSharedFlow<String>()
    val navEvent = _navEvent.asSharedFlow()

    private var _navUpEvent = MutableSharedFlow<Unit>()
    val navUpEvent = _navUpEvent.asSharedFlow()

    /**
     * update pin's position
     */
    fun updatePinPosition(position: LatLng) {
        _uiState.update { it.copy(pinPosition = position) }
    }

    fun navigateUp() {
        viewModelScope.launch {
            _navUpEvent.emit(Unit)
        }
    }

    fun navigateToNextStep() {
        viewModelScope.launch {
            val longitude = uiState.value.pinPosition.longitude
            val latitude = uiState.value.pinPosition.latitude
            _navEvent.emit("${Page.LocationConfirm.route}/${longitude}/${latitude}")
        }
    }


}