package com.turkcell.ticketapp.viewmodel

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.turkcell.core.domain.AuthRepository
import com.turkcell.ticketapp.util.toUserMessage
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class RegisterUiState(
    val email: String = "",
    val password: String = "",
    val confirmPassword: String = "",
    val isLoading: Boolean = false,
    val errorMessage: String? = null,
    val isRegistered: Boolean = false
) {
    val canSubmit: Boolean
        get() = email.isNotBlank()
                && android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()
                && password.length in 8..128
                && confirmPassword == password
                && !isLoading
}

class RegisterViewModel(
    private val authRepository: AuthRepository,
    private val savedStateHandle: SavedStateHandle
) : ViewModel() {

    private companion object {
        const val KEY_EMAIL = "email"
        const val KEY_PASSWORD = "password"
        const val KEY_CONFIRM_PASSWORD = "confirm_password"
    }

    private val _state = MutableStateFlow(
        RegisterUiState(
            email = savedStateHandle[KEY_EMAIL] ?: "",
            password = savedStateHandle[KEY_PASSWORD] ?: "",
            confirmPassword = savedStateHandle[KEY_CONFIRM_PASSWORD] ?: ""
        )
    )
    val state: StateFlow<RegisterUiState> = _state.asStateFlow()

    fun onEmailChange(value: String) {
        savedStateHandle[KEY_EMAIL] = value
        _state.update { it.copy(email = value, errorMessage = null) }
    }

    fun onPasswordChange(value: String) {
        savedStateHandle[KEY_PASSWORD] = value
        _state.update { it.copy(password = value, errorMessage = null) }
    }

    fun onConfirmPasswordChange(value: String) {
        savedStateHandle[KEY_CONFIRM_PASSWORD] = value
        _state.update { it.copy(confirmPassword = value, errorMessage = null) }
    }

    fun submit() {
        val current = _state.value
        if (!current.canSubmit) return

        if (current.password != current.confirmPassword) {
            _state.update { it.copy(errorMessage = "Sifreler eslesmiyor") }
            return
        }

        _state.update { it.copy(isLoading = true, errorMessage = null) }

        viewModelScope.launch {
            authRepository.register(current.email, current.password)
                .onSuccess { _state.update { it.copy(isLoading = false, isRegistered = true) } }
                .onFailure { error -> _state.update { it.copy(isLoading = false, errorMessage = error.toUserMessage()) } }
        }
    }
}
