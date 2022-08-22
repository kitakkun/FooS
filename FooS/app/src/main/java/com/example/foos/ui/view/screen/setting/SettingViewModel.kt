package com.example.foos.ui.view.screen.setting

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.canhub.cropper.CropImageView
import com.example.foos.data.repository.UsersRepository
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SettingViewModel @Inject constructor(
    private val usersRepository: UsersRepository
) : ViewModel(
) {
    private var _uiState = MutableStateFlow(SettingUiState("", ""))
    val uiState: StateFlow<SettingUiState> get() = _uiState

    fun fetchUserData() {
        viewModelScope.launch(Dispatchers.IO) {
            val user = usersRepository.fetchUser(Firebase.auth.uid.toString()) ?: return@launch
            _uiState.update {
                it.copy(
                    username = user.username,
                    profileImage = user.profileImage
                )
            }
        }
    }

    fun onCropImageSuccessful(result: CropImageView.CropResult) {
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
}