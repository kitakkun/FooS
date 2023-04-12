package com.github.kitakkun.foos.ui.view.screen.auth.signup

import android.util.Log
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.github.kitakkun.foos.common.navigation.MainScreen
import com.github.kitakkun.foos.common.navigation.SubScreen
import com.github.kitakkun.foos.user.model.Email
import com.github.kitakkun.foos.user.model.Password
import com.github.kitakkun.foos.user.repository.UsersRepository
import com.github.michaelbull.result.Err
import com.github.michaelbull.result.Ok
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SignUpViewModel @Inject constructor(
    private val usersRepository: UsersRepository,
) : ViewModel() {

    private var _uiState = mutableStateOf(SignUpUiState())
    val uiState: State<SignUpUiState> = _uiState

    private var _navEvent = MutableSharedFlow<String>()
    val navEvent = _navEvent.asSharedFlow()

    fun updateEmail(email: String) {
        _uiState.value = _uiState.value.copy(email = email)
    }

    fun updatePassword(password: String) {
        _uiState.value = _uiState.value.copy(password = password)
    }

    fun signUp() = viewModelScope.launch(Dispatchers.IO) {
        try {
            val email = Email(_uiState.value.email)
            val password = Password(_uiState.value.password)
            _uiState.value = _uiState.value.copy(isLoading = true)
            val result = usersRepository.create(email, password)
            when (result) {
                is Ok -> {
                    navigateToHome()
                }
                is Err -> {
                    Log.e("SignUpViewModel", "signUp: ", result.error)
                }
            }
        } catch (e: Throwable) {
            Log.e("SignUpViewModel", "signUp: ", e)
        } finally {
            _uiState.value = _uiState.value.copy(isLoading = false)
        }
    }

    private fun navigateToHome() = viewModelScope.launch(Dispatchers.IO) {
        _navEvent.emit(MainScreen.Home.route)
    }

    fun navigateToSignIn() = viewModelScope.launch(Dispatchers.IO) {
        _navEvent.emit(SubScreen.Auth.SignIn.route)
    }

    fun togglePasswordVisibility() {
        _uiState.value = _uiState.value.copy(isPasswordVisible = !_uiState.value.isPasswordVisible)
    }
}
