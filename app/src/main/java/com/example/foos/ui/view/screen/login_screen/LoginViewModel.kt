package com.example.foos.ui.view.screen.login_screen

import android.app.Application
import android.util.Log
import android.widget.Toast
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.AndroidViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseAuthUserCollisionException
import com.google.firebase.auth.FirebaseAuthWeakPasswordException
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(private val auth: FirebaseAuth, application: Application) : AndroidViewModel(application) {

    companion object {
        private const val TAG = "LoginViewModel"
    }

    private val mutableUiState = mutableStateOf(LoginUiState())
    val uiState: State<LoginUiState> = mutableUiState

    fun onEmailChange(value: String) {
        mutableUiState.value = mutableUiState.value.copy(email = value)
    }

    fun onPasswordChange(value: String) {
        mutableUiState.value = mutableUiState.value.copy(password = value)
    }

    fun onPasswordVisibilityChange(value: Boolean) {
        mutableUiState.value = mutableUiState.value.copy(isPasswordVisible = value)
    }

    fun onCreateAccountClicked() {
        // TODO: なんかtry-catchしてもクラッシュしてしまう
        try {
            auth.createUserWithEmailAndPassword(uiState.value.email, uiState.value.password)
                .addOnCompleteListener { task ->
                    Log.d(TAG, task.result.credential.toString())
                    if (task.isSuccessful) {
                        Toast.makeText(getApplication(), "Succeed", Toast.LENGTH_SHORT).show()
                    } else {
                        Toast.makeText(getApplication(), "Fail", Toast.LENGTH_SHORT).show()
                    }
                }
                .addOnFailureListener {
                    Log.d(TAG, it.message ?: "")
                }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}
