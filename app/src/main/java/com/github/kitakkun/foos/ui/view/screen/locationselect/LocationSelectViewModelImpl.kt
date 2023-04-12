package com.github.kitakkun.foos.ui.view.screen.locationselect

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.github.kitakkun.foos.ui.navigation.SubScreen
import com.github.kitakkun.foos.ui.state.screen.locationselect.LocationSelectScreenUiState
import com.google.android.gms.maps.model.LatLng
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LocationSelectViewModelImpl @Inject constructor() : ViewModel(), LocationSelectViewModel {

    private var _uiState = mutableStateOf(LocationSelectScreenUiState())
    override val uiState: State<LocationSelectScreenUiState> = _uiState

    private var _navEvent = MutableSharedFlow<String>()
    override val navToNextEvent = _navEvent.asSharedFlow()

    private var _navUpEvent = MutableSharedFlow<Unit>()
    override val cancelNavEvent = _navUpEvent.asSharedFlow()

    /**
     * update pin's position
     */
    override fun updatePinPosition(position: LatLng) {
        _uiState.value = uiState.value.copy(pinPosition = position)
    }

    override fun cancel() {
        viewModelScope.launch {
            _navUpEvent.emit(Unit)
        }
    }

    override fun navigateToConfirmScreen() {
        viewModelScope.launch {
            _navEvent.emit(SubScreen.PostCreate.LocationConfirm.route)
        }
    }
}
