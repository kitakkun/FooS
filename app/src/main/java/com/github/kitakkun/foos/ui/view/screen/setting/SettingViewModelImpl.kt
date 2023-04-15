package com.github.kitakkun.foos.ui.view.screen.setting

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.canhub.cropper.CropImageView
import com.github.kitakkun.foos.common.repository.UsersRepository
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SettingViewModelImpl @Inject constructor(
    private val usersRepository: UsersRepository
) : ViewModel(), SettingViewModel {

    private var _uiState = mutableStateOf(SettingUiState("", ""))
    override val uiState: State<SettingUiState> = _uiState

    override fun fetchUserData() {
        viewModelScope.launch(Dispatchers.IO) {
            val user = usersRepository.fetchByUserId(Firebase.auth.uid.toString()) ?: return@launch
            _uiState.value = _uiState.value.copy(
                username = user.username,
                profileImage = user.profileImage
            )
        }
    }

    override fun onCropImageSucceeded(result: CropImageView.CropResult) {
        viewModelScope.launch(Dispatchers.IO) {
            result.uriContent?.let {
                val ref =
                    Firebase.storage.reference.child("images/profile/" + Firebase.auth.uid + ".png")
                val uploadTask = ref.putFile(it)
                uploadTask.addOnFailureListener {
                    /*TODO: Error handling*/
                }.addOnSuccessListener {
                    ref.downloadUrl.addOnSuccessListener {
                        val map = mapOf("profileImage" to it.toString())
                        Firebase.firestore.collection("users")
                            .document(Firebase.auth.uid.toString()).update(map)
                        fetchUserData()
                    }
                }
            }
        }
    }

    override fun onCropImageFailed(result: CropImageView.CropResult) {
        result.error?.printStackTrace()
    }
}
