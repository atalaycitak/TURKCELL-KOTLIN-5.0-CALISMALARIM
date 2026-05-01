package com.turkcell.libraryapp.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.turkcell.libraryapp.data.model.BorrowRecord
import com.turkcell.libraryapp.data.repository.BorrowRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class BorrowViewModel : ViewModel() {
    private val repository = BorrowRepository()

    private val _borrows = MutableStateFlow<List<BorrowRecord>>(emptyList())
    val borrows: StateFlow<List<BorrowRecord>> = _borrows

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error

    private val _borrowSuccess = MutableStateFlow(false)
    val borrowSuccess: StateFlow<Boolean> = _borrowSuccess

    // Kitap ödünç al
    fun borrowBook(studentId: String, bookId: String) {
        viewModelScope.launch {
            _isLoading.value = true
            _error.value = null
            repository
                .borrowBook(studentId, bookId)
                .onSuccess {
                    _borrowSuccess.value = true
                }
                .onFailure {
                    _error.value = it.message ?: "Ödünç alma başarısız"
                }
            _isLoading.value = false
        }
    }

    // Öğrencinin kiralamalarını yükle
    fun loadMyBorrows(studentId: String) {
        viewModelScope.launch {
            _isLoading.value = true
            _error.value = null
            repository
                .getMyBorrows(studentId)
                .onSuccess { _borrows.value = it }
                .onFailure { _error.value = it.message }
            _isLoading.value = false
        }
    }

    // Kitap iade et
    fun returnBook(borrowId: String, bookId: String, studentId: String) {
        viewModelScope.launch {
            _isLoading.value = true
            _error.value = null
            repository
                .returnBook(borrowId, bookId)
                .onSuccess {
                    // İade sonrası listeyi yenile
                    loadMyBorrows(studentId)
                }
                .onFailure {
                    _error.value = it.message ?: "İade başarısız"
                }
            _isLoading.value = false
        }
    }

    // Başarı durumunu sıfırla
    fun resetBorrowSuccess() {
        _borrowSuccess.value = false
    }
}
