package com.example.foos.ui.view.screen.locationconfirm

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LocationConfirmViewModel @Inject constructor() : ViewModel() {

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
}
