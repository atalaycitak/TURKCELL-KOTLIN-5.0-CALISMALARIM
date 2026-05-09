package com.turkcell.authrepowithkoin

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

data class LoginUiState(
    val isLoading: Boolean = false,
    val token: String? = null,
    val error: String? = null
)

class LoginViewModel(private val authRepository: AuthRepository) : ViewModel() {

    private val _uiState = MutableStateFlow(LoginUiState())
    val uiState: StateFlow<LoginUiState> = _uiState

    fun login(email: String, password: String) {
        _uiState.value = LoginUiState(isLoading = true)
        Log.d("LoginViewModel", "Login istegi gonderiliyor: email=$email")

        viewModelScope.launch {
            try {
                val response = authRepository.login(email, password)
                Log.d("LoginViewModel", "Login basarili! Token: ${response.token}")
                _uiState.value = LoginUiState(token = response.token)
            } catch (e: Exception) {
                Log.e("LoginViewModel", "Login hatasi: ${e.message}", e)
                _uiState.value = LoginUiState(error = e.message ?: "Bilinmeyen hata")
            }
        }
    }
}
