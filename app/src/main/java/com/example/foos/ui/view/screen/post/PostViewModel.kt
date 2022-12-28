package com.example.foos.ui.view.screen.post

import android.content.Context
import android.net.Uri
import androidx.compose.runtime.State
import com.example.foos.ui.state.component.PostItemUiState
import com.google.android.gms.maps.model.LatLng
import kotlinx.coroutines.flow.SharedFlow

interface PostViewModel {
    val uiState: State<PostItemUiState>
    val navUpEvent: SharedFlow<Unit>
    val navEvent: SharedFlow<String>

    fun onCancel()
    fun applyLocationData(location: LatLng, locationName: String)
    fun setImages(context: Context, imageUris: List<Uri>)
    fun onTextFieldUpdated(text: String)
    fun onImageAttachmentRemove(url: String)
    fun onLocationRemove()
    fun navigateToLocationSelect()
    fun post()
}