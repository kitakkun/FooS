package com.github.kitakkun.foos.post.create

import android.content.Context
import android.net.Uri
import androidx.lifecycle.ViewModel
import com.github.kitakkun.foos.common.model.DatabasePost
import com.github.kitakkun.foos.common.repository.PostsRepository
import com.github.kitakkun.foos.post.create.edit.PostEditUiState
import com.github.kitakkun.foos.util.FileUtils.getRealPath
import com.google.android.gms.maps.model.LatLng
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class PostCreateViewModel(
    private val postsRepository: PostsRepository,
    private val firebaseAuth: FirebaseAuth,
) : ViewModel() {

    private var mutableUiState = MutableStateFlow(PostEditUiState())
    val uiState = mutableUiState.asStateFlow()

    fun setLocation(location: LatLng) {
        mutableUiState.update { it.copy(location = location) }
    }

    fun updateLocationName(locationName: String) {
        mutableUiState.update { it.copy(locationName = locationName) }
    }

    fun setImages(context: Context, imageUris: List<Uri>) {
        mutableUiState.update {
            it.copy(
                attachedImageUrls = (it.attachedImageUrls + imageUris.map { uri ->
                    "file://" + getRealPath(context, uri)
                }).distinct()
            )
        }
    }

    fun updateContentText(text: String) {
        mutableUiState.update {
            it.copy(content = text)
        }
    }

    fun removeImageAttachment(url: String) {
        mutableUiState.update {
            it.copy(attachedImageUrls = it.attachedImageUrls.filter { it != url })
        }
    }

    fun removeLocationData() {
        mutableUiState.update {
            it.copy(locationName = null, location = null)
        }
    }

    fun post(context: Context) {
        MainScope().launch(Dispatchers.IO) {
            val databasePost = DatabasePost(
                postId = "",
                userId = firebaseAuth.uid.toString(),
                content = uiState.value.content,
                attachedImages = uiState.value.attachedImageUrls,
                longitude = uiState.value.location?.longitude,
                latitude = uiState.value.location?.latitude,
                locationName = uiState.value.locationName,
            )
            postsRepository.create(databasePost, context)
        }
    }
}
