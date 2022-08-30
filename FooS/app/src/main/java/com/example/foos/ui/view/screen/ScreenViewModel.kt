package com.example.foos.ui.view.screen

import android.util.Log
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.foos.data.shared.PostCreateSharedData
import com.google.android.gms.maps.model.LatLng
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ScreenViewModel @Inject constructor() : ViewModel() {

    private var _navRoute = MutableSharedFlow<String>()
    val navRoute = _navRoute.asSharedFlow()

    private var _postCreateSharedData = mutableStateOf(PostCreateSharedData())
    val postCreateSharedData: State<PostCreateSharedData> = _postCreateSharedData

    fun navigate(route: String) {
        viewModelScope.launch {
            _navRoute.emit(route)
        }
    }

    fun updatePostCreateSharedData(location: LatLng?, locationName: String?) {
        _postCreateSharedData.value = PostCreateSharedData(location, locationName)
    }
}