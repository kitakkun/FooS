package com.github.kitakkun.foos.user.setting

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.canhub.cropper.CropImageView
import com.github.kitakkun.foos.common.repository.UsersRepository
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class SettingViewModel(
    private val firebaseAuth: FirebaseAuth,
    private val usersRepository: UsersRepository
) : ViewModel() {

    private var mutableUiState = MutableStateFlow(SettingUiState("", ""))
    val uiState = mutableUiState.asStateFlow()

    fun fetchUserData() {
        viewModelScope.launch(Dispatchers.IO) {
            val user = usersRepository.fetchByUserId(Firebase.auth.uid.toString()) ?: return@launch
            mutableUiState.update {
                it.copy(
                    username = user.name,
                    profileImage = user.profileImage
                )
            }
        }
    }

    fun onCropImageSucceeded(result: CropImageView.CropResult) {
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

    fun onCropImageFailed(result: CropImageView.CropResult) {
        result.error?.printStackTrace()
    }

    fun logOut() {
        firebaseAuth.signOut()
    }
}
