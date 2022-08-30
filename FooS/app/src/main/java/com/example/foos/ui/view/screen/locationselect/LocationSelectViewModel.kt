package com.example.foos.ui.view.screen.locationselect

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.foos.ui.state.screen.locationselect.LocationSelectScreenUiState
import com.example.foos.ui.navigation.SubScreen
import com.google.android.gms.maps.model.LatLng
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LocationSelectViewModel @Inject constructor() : ViewModel() {

    private var _uiState = mutableStateOf(LocationSelectScreenUiState(LatLng(0.0, 0.0)))
    val uiState: State<LocationSelectScreenUiState> = _uiState

    private var _navEvent = MutableSharedFlow<String>()
    val navToNextEvent = _navEvent.asSharedFlow()

    private var _navUpEvent = MutableSharedFlow<Unit>()
    val cancelNavEvent = _navUpEvent.asSharedFlow()

    /**
     * update pin's position
     */
    fun updatePinPosition(position: LatLng) {
        _uiState.value = uiState.value.copy(pinPosition = position)
    }

    fun cancel() {
        viewModelScope.launch {
            _navUpEvent.emit(Unit)
        }
    }

    fun navigateToConfirmScreen() {
        viewModelScope.launch {
            _navEvent.emit(SubScreen.PostCreate.LocationConfirm.route)
        }
    }


}