package com.github.kitakkun.foos.ui.view.screen

import android.app.Activity
import android.content.Context
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.github.kitakkun.foos.data.shared.PostCreateSharedData
import com.google.android.gms.maps.model.LatLng
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ScreenViewModel @Inject constructor(
    private val auth: FirebaseAuth,
) : ViewModel() {

    private var _navRoute = MutableSharedFlow<String>()
    val navRoute = _navRoute.asSharedFlow()

    private var _postCreateSharedData = mutableStateOf(PostCreateSharedData())
    val postCreateSharedData: State<PostCreateSharedData> = _postCreateSharedData

    /**
     * アクティビティを作り直す
     */
    fun recreateActivity(context: Context) {
        (context as Activity).recreate()
    }

    /**
     * Firebaseのログアウト処理
     */
    fun logOut() {
        auth.signOut()
    }

    fun navigate(route: String) {
        viewModelScope.launch {
            _navRoute.emit(route)
        }
    }

    fun updatePostCreateSharedData(location: LatLng?, locationName: String?) {
        _postCreateSharedData.value = PostCreateSharedData(location, locationName)
    }
}
