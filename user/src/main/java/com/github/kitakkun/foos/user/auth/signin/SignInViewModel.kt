package com.github.kitakkun.foos.user.auth.signin

import android.util.Log
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext

class SignInViewModel(
    private val firebaseAuth: FirebaseAuth,
) : ViewModel() {
    companion object {
        private const val TAG = "SignInViewModel"
    }

    private val _uiState = mutableStateOf(SignInUiState())
    val uiState: State<SignInUiState> = _uiState

    fun updateEmail(email: String) {
        _uiState.value = uiState.value.copy(email = email)
    }

    fun updatePassword(password: String) {
        _uiState.value = uiState.value.copy(password = password)
    }

    fun signIn(onCompleted: suspend (success: Boolean) -> Unit) =
        viewModelScope.launch(Dispatchers.IO) {
            try {
                _uiState.value = uiState.value.copy(isLoading = true)
                firebaseAuth.signInWithEmailAndPassword(
                    uiState.value.email,
                    uiState.value.password,
                ).await()
                withContext(Dispatchers.Main) {
                    onCompleted(true)
                }
            } catch (e: Throwable) {
                Log.e(TAG, "signIn: ", e)
                withContext(Dispatchers.Main) {
                    onCompleted(false)
                }
            } finally {
                _uiState.value = uiState.value.copy(isLoading = false)
            }
        }

    fun togglePasswordVisibility() {
        _uiState.value = uiState.value.copy(isPasswordVisible = !uiState.value.isPasswordVisible)
    }
}
