package com.example.foos.ui.view.screen.login_screen

import android.app.Application
import android.widget.Toast
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.AndroidViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthUserCollisionException
import com.google.firebase.auth.FirebaseAuthWeakPasswordException
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(private val auth: FirebaseAuth, application: Application) :
    AndroidViewModel(application) {
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

    fun onCreateAccountClick() {
        auth.createUserWithEmailAndPassword(uiState.value.email, uiState.value.password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    Toast.makeText(getApplication(), "Succeed", Toast.LENGTH_SHORT).show()
                } else {
                    val message = when (task.exception) {
                        is FirebaseAuthUserCollisionException -> "The specified email address is already in use."
                        is FirebaseAuthWeakPasswordException -> "Password should be at least 6 characters."
                        else -> null
                    }
                    message?.let {
                        Toast.makeText(getApplication(), it, Toast.LENGTH_SHORT).show()
                    }
                }
            }
            .addOnFailureListener {
                when (it) {
                    is FirebaseAuthUserCollisionException ->
                        Toast.makeText(
                            getApplication(),
                            "The specified email address is already used by others.",
                            Toast.LENGTH_SHORT
                        ).show()
                }
            }
    }

    fun onLoginTextClick() {
        /* TODO: actual implementation */
    }
}
