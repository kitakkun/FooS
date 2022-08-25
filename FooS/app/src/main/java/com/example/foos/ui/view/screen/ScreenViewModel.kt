package com.example.foos.ui.view.screen

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ScreenViewModel @Inject constructor(): ViewModel() {

    private var _navRoute = MutableSharedFlow<String>()
    val navRoute = _navRoute.asSharedFlow()

    fun navigate(route: String) {
        val result = _navRoute.tryEmit(route)
        Log.d("RESULT", result.toString())
        viewModelScope.launch {
        }
    }
}