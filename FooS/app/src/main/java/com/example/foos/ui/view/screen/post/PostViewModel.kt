package com.example.foos.ui.view.screen.post

import android.app.Application
import android.content.Context
import android.net.Uri
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.foos.data.model.database.DatabasePost
import com.example.foos.data.repository.PostsRepository
import com.example.foos.ui.navigation.SubScreen
import com.example.foos.ui.state.screen.post.PostScreenUiState
import com.example.foos.util.FileUtils.getRealPath
import com.google.android.gms.maps.model.LatLng
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PostViewModel @Inject constructor(
    private val postsRepository: PostsRepository,
    application: Application
) : AndroidViewModel(application) {

    private var _uiState = mutableStateOf(PostScreenUiState("", listOf(), null, null))
    val uiState: State<PostScreenUiState> = _uiState

    private var _navUpEvent = MutableSharedFlow<Boolean>()
    val navUpEvent = _navUpEvent.asSharedFlow()

    private var _navEvent = MutableSharedFlow<String>()
    val navEvent = _navEvent.asSharedFlow()

    fun applyLocationData(location: LatLng, locationName: String) {
        _uiState.value = uiState.value.copy(
            location = location,
            locationName = locationName
        )
    }

    fun setImages(context: Context, imageUris: List<Uri>) {
        _uiState.value = _uiState.value.copy(
            attachedImages = (uiState.value.attachedImages + imageUris.map { uri ->
                "file://" + getRealPath(context, uri)
            }).distinct()
        )
    }

    fun onTextFieldUpdated(text: String) {
        _uiState.value = uiState.value.copy(content = text)
    }

    /**
     * 位置情報の指定を行う
     */
    fun navigateToLocationSelect() {
        viewModelScope.launch {
            _navEvent.emit(SubScreen.PostCreate.LocationSelect.route)
        }
    }

    /**
     * 投稿処理を行う
     */
    fun post() {
        MainScope().launch(Dispatchers.IO) {
            val databasePost = DatabasePost(
                postId = "",
                userId = Firebase.auth.uid.toString(),
                content = uiState.value.content,
                attachedImages = uiState.value.attachedImages,
                longitude = uiState.value.location?.longitude,
                latitude = uiState.value.location?.latitude,
                locationName = uiState.value.locationName,
            )
            postsRepository.create(databasePost, getApplication())
        }
        viewModelScope.launch {
            _navUpEvent.emit(true)
        }
    }
}