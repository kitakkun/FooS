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
import com.example.foos.ui.state.component.PostItemUiState
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

    private var _uiState = mutableStateOf(PostItemUiState.Default)
    val uiState: State<PostItemUiState> = _uiState

    private var _navUpEvent = MutableSharedFlow<Unit>()
    val navUpEvent = _navUpEvent.asSharedFlow()

    private var _navEvent = MutableSharedFlow<String>()
    val navEvent = _navEvent.asSharedFlow()

    /**
     * 投稿の作成がキャンセルされたとき
     */
    fun onCancel() {
        viewModelScope.launch {
            _navUpEvent.emit(Unit)
        }
    }

    fun applyLocationData(location: LatLng, locationName: String) {
        _uiState.value = uiState.value.copy(
            latitude = location.latitude,
            longitude = location.longitude,
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
     * 画像アタッチメントの削除
     */
    fun onImageAttachmentRemove(url: String) {
        _uiState.value =
            uiState.value.copy(attachedImages = uiState.value.attachedImages.filter { it != url })
    }

    /**
     * 位置情報アタッチメントの削除
     */
    fun onLocationRemove() {
        _uiState.value = uiState.value.copy(locationName = null, latitude = null, longitude = null)
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
                longitude = uiState.value.longitude,
                latitude = uiState.value.latitude,
                locationName = uiState.value.locationName,
            )
            postsRepository.create(databasePost, getApplication())
        }
        viewModelScope.launch {
            _navUpEvent.emit(Unit)
        }
    }
}
