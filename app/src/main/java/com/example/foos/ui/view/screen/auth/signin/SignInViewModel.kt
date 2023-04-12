package com.example.foos.ui.view.screen.auth.signin

import android.app.Application
import android.widget.Toast
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.foos.R
import com.example.foos.ui.navigation.SubScreen
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

@HiltViewModel
class SignInViewModel @Inject constructor(
    private val firebaseAuth: FirebaseAuth,
    private val application: Application,
) : AndroidViewModel(application) {

    private val _uiState = mutableStateOf(SignInUiState())
    val uiState: State<SignInUiState> = _uiState

    private val _navEvent = MutableSharedFlow<String>()
    val navEvent = _navEvent.asSharedFlow()

    fun updateEmail(email: String) {
        _uiState.value = uiState.value.copy(email = email)
    }

    fun updatePassword(password: String) {
        _uiState.value = uiState.value.copy(password = password)
    }

    fun signIn() = viewModelScope.launch(Dispatchers.IO) {
        try {
            _uiState.value = uiState.value.copy(isLoading = true)
            firebaseAuth.signInWithEmailAndPassword(
                uiState.value.email,
                uiState.value.password,
            ).await()
        } catch (e: Throwable) {
            Toast.makeText(
                application,
                application.getString(R.string.message_sign_in_error),
                Toast.LENGTH_LONG
            ).show()
        } finally {
            _uiState.value = uiState.value.copy(isLoading = false)
        }
    }

    fun navigateToSignUp() {
        _navEvent.tryEmit(SubScreen.Auth.SignUp.route)
    }
}
