package com.turkcell.ticketapp.viewmodel

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.turkcell.core.domain.Purchase
import com.turkcell.core.domain.PurchaseRepository
import com.turkcell.core.domain.PurchaseStatus
import com.turkcell.ticketapp.util.toUserMessage
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class MyPurchasesUiState(
    val purchases: List<Purchase> = emptyList(),
    val isLoading: Boolean = false,
    val errorMessage: String? = null,
    val payingPurchaseId: String? = null,
    val paymentSuccess: Boolean = false
)

class MyPurchasesViewModel(
    private val purchaseRepository: PurchaseRepository,
    private val savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val _state = MutableStateFlow(MyPurchasesUiState())
    val state: StateFlow<MyPurchasesUiState> = _state.asStateFlow()

    init {
        loadPurchases()
    }

    fun refresh() {
        loadPurchases()
    }

    private fun loadPurchases() {
        if (_state.value.isLoading) return
        _state.update { it.copy(isLoading = true, errorMessage = null) }
        viewModelScope.launch {
            purchaseRepository.getMyPurchases()
                .onSuccess { purchases ->
                    _state.update { it.copy(isLoading = false, purchases = purchases) }
                }
                .onFailure { error ->
                    _state.update { it.copy(isLoading = false, errorMessage = error.toUserMessage()) }
                }
        }
    }

    fun payPurchase(purchaseId: String) {
        _state.update { it.copy(payingPurchaseId = purchaseId, errorMessage = null) }
        viewModelScope.launch {
            purchaseRepository.payPurchase(purchaseId)
                .onSuccess { updatedPurchase ->
                    _state.update { current ->
                        val updatedList = current.purchases.map { p ->
                            if (p.id == purchaseId) updatedPurchase else p
                        }
                        current.copy(
                            payingPurchaseId = null,
                            purchases = updatedList,
                            paymentSuccess = true
                        )
                    }
                }
                .onFailure { error ->
                    _state.update {
                        it.copy(payingPurchaseId = null, errorMessage = error.toUserMessage())
                    }
                }
        }
    }

    fun consumePaymentSuccess() {
        _state.update { it.copy(paymentSuccess = false) }
    }
}
