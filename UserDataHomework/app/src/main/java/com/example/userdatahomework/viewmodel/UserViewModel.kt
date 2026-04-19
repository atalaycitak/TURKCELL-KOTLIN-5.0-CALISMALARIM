package com.example.userdatahomework.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.userdatahomework.data.model.User
import com.example.userdatahomework.data.repository.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

sealed interface UserUiState {
    object Loading : UserUiState
    data class Success(val users: List<User>) : UserUiState
    data class Error(val message: String) : UserUiState
}

@HiltViewModel
class UserViewModel @Inject constructor(
    private val repository: UserRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow<UserUiState>(UserUiState.Loading)
    val uiState: StateFlow<UserUiState> = _uiState.asStateFlow()

    // arama cubugu icin query state
    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery.asStateFlow()

    // pull-to-refresh icin yukleniyor state
    private val _isRefreshing = MutableStateFlow(false)
    val isRefreshing: StateFlow<Boolean> = _isRefreshing.asStateFlow()

    init {
        fetchUsers()
    }

    fun fetchUsers() {
        viewModelScope.launch {
            _uiState.value = UserUiState.Loading
            try {
                val users = repository.getUsers()
                _uiState.value = UserUiState.Success(users)
            } catch (e: Exception) {
                _uiState.value = UserUiState.Error(
                    e.localizedMessage ?: "Bilinmeyen bir hata olustu"
                )
            }
        }
    }

    // pull-to-refresh ile listeyi yeniler
    fun refreshUsers() {
        viewModelScope.launch {
            _isRefreshing.value = true
            try {
                val users = repository.getUsers()
                _uiState.value = UserUiState.Success(users)
            } catch (e: Exception) {
                _uiState.value = UserUiState.Error(
                    e.localizedMessage ?: "Bilinmeyen bir hata olustu"
                )
            }
            _isRefreshing.value = false
        }
    }

    // arama sorgusunu gunceller
    fun onSearchQueryChange(query: String) {
        _searchQuery.value = query
    }

    // kullanici listesini arama sorgusuna gore filtreler
    fun getFilteredUsers(users: List<User>): List<User> {
        val query = _searchQuery.value.lowercase()
        if (query.isBlank()) return users
        return users.filter {
            it.name.lowercase().contains(query) ||
                it.email.lowercase().contains(query)
        }
    }
}
