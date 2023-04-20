package com.github.kitakkun.foos.user.auth.signup

import android.util.Log
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.github.kitakkun.foos.common.model.Email
import com.github.kitakkun.foos.common.model.Password
import com.github.kitakkun.foos.common.repository.UsersRepository
import com.github.michaelbull.result.Err
import com.github.michaelbull.result.Ok
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class SignUpViewModel @Inject constructor(
    private val usersRepository: UsersRepository,
) : ViewModel() {
    companion object {
        private const val TAG = "SignUpViewModel"
    }

    private var _uiState = mutableStateOf(SignUpUiState())
    val uiState: State<SignUpUiState> = _uiState

    fun updateEmail(email: String) {
        _uiState.value = _uiState.value.copy(email = email)
    }

    fun updatePassword(password: String) {
        _uiState.value = _uiState.value.copy(password = password)
    }

    fun signUp(onComplete: suspend (success: Boolean) -> Unit) =
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val email = Email(_uiState.value.email)
                val password = Password(_uiState.value.password)
                _uiState.value = _uiState.value.copy(isLoading = true)
                val result = usersRepository.create(email, password)
                _uiState.value = _uiState.value.copy(isLoading = false)
                when (result) {
                    is Ok -> {
                        Log.d(TAG, "signUp: Success.")
                        withContext(Dispatchers.Main) {
                            onComplete(true)
                        }
                    }
                    is Err -> {
                        Log.e(TAG, "signUp: ", result.error)
                        withContext(Dispatchers.Main) {
                            onComplete(false)
                        }
                    }
                }
            } catch (e: Throwable) {
                Log.e(TAG, "signUp: ", e)
                _uiState.value = _uiState.value.copy(isLoading = false)
                withContext(Dispatchers.Main) {
                    onComplete(false)
                }
            }
        }

    fun togglePasswordVisibility() {
        _uiState.value = _uiState.value.copy(isPasswordVisible = !_uiState.value.isPasswordVisible)
    }
}
