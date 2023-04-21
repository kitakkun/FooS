package com.github.kitakkun.foos.user.auth.signup

import android.util.Log
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.github.kitakkun.foos.common.model.Email
import com.github.kitakkun.foos.common.model.InvalidEmailException
import com.github.kitakkun.foos.common.model.InvalidPasswordException
import com.github.kitakkun.foos.common.model.Password
import com.github.kitakkun.foos.common.repository.UsersRepository
import com.github.kitakkun.foos.user.auth.EmailError
import com.github.kitakkun.foos.user.auth.PasswordError
import com.github.michaelbull.result.Err
import com.github.michaelbull.result.Ok
import com.github.michaelbull.result.getError
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
        _uiState.value = uiState.value.copy(
            email = email,
            isEmailErrorVisible = false,
            emailError = null
        )

        val validationResult = Email.validate(uiState.value.email)
        if (validationResult is Ok) return

        val emailError = when (validationResult.getError()) {
            is InvalidEmailException.InvalidFormat -> EmailError.INVALID_FORMAT
            is InvalidEmailException.Blank -> EmailError.BLANK
            else -> null
        } ?: return

        _uiState.value = uiState.value.copy(
            isEmailErrorVisible = true,
            emailError = emailError
        )
    }

    fun updatePassword(password: String) {
        _uiState.value = _uiState.value.copy(
            password = password,
            isPasswordErrorVisible = false,
            passwordError = null
        )

        val validationResult = Password.validate(_uiState.value.password)
        if (validationResult is Ok) return

        val passwordError = when (validationResult.getError()) {
            is InvalidPasswordException.Blank -> PasswordError.BLANK
            is InvalidPasswordException.TooShort -> PasswordError.TOO_SHORT
            is InvalidPasswordException.TooLong -> PasswordError.TOO_LONG
            is InvalidPasswordException.NoNumericCharacter -> PasswordError.NO_NUMERIC_CHARACTER
            is InvalidPasswordException.NoAlphabeticCharacter -> PasswordError.NO_ALPHABETIC_CHARACTER
            is InvalidPasswordException.ContainInvalidCharacter -> PasswordError.CONTAIN_INVALID_CHARACTER
            else -> null
        } ?: return

        _uiState.value = _uiState.value.copy(
            isPasswordErrorVisible = true,
            passwordError = passwordError
        )
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
                withContext(Dispatchers.Main) {
                    onComplete(false)
                }
            }
        }

    fun togglePasswordVisibility() {
        _uiState.value = _uiState.value.copy(isPasswordVisible = !_uiState.value.isPasswordVisible)
    }
}
