package com.turkcell.ticketapp.viewmodel

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.turkcell.core.domain.Event
import com.turkcell.core.domain.EventRepository
import com.turkcell.core.domain.PurchaseRepository
import com.turkcell.core.domain.PurchaseRequest
import com.turkcell.ticketapp.util.toUserMessage
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class EventDetailUiState(
    val event: Event? = null,
    val isLoading: Boolean = false,
    val selectedTicketTypeId: String? = null,
    val quantity: Int = 1,
    val isPurchasing: Boolean = false,
    val purchaseSuccess: Boolean = false,
    val errorMessage: String? = null
) {
    val canPurchase: Boolean get() = selectedTicketTypeId != null && quantity > 0 && !isPurchasing
}

class EventDetailViewModel(
    private val eventId: String,
    private val eventRepository: EventRepository,
    private val purchaseRepository: PurchaseRepository,
    private val savedStateHandle: SavedStateHandle
) : ViewModel() {

    private companion object {
        const val KEY_SELECTED_TICKET_TYPE = "selected_ticket_type_id"
        const val KEY_QUANTITY = "quantity"
    }

    private val _state = MutableStateFlow(
        EventDetailUiState(
            selectedTicketTypeId = savedStateHandle[KEY_SELECTED_TICKET_TYPE],
            quantity = savedStateHandle[KEY_QUANTITY] ?: 1
        )
    )
    val state: StateFlow<EventDetailUiState> = _state.asStateFlow()

    init {
        loadEvent()
    }

    fun loadEvent() {
        if (_state.value.isLoading) return
        _state.update { it.copy(isLoading = true, errorMessage = null) }
        viewModelScope.launch {
            eventRepository.getEventById(eventId)
                .onSuccess { event ->
                    _state.update { it.copy(isLoading = false, event = event) }
                }
                .onFailure { error ->
                    _state.update { it.copy(isLoading = false, errorMessage = error.toUserMessage()) }
                }
        }
    }

    fun selectTicketType(ticketTypeId: String) {
        savedStateHandle[KEY_SELECTED_TICKET_TYPE] = ticketTypeId
        _state.update { it.copy(selectedTicketTypeId = ticketTypeId, errorMessage = null) }
    }

    fun setQuantity(quantity: Int) {
        if (quantity in 1..20) {
            savedStateHandle[KEY_QUANTITY] = quantity
            _state.update { it.copy(quantity = quantity, errorMessage = null) }
        }
    }

    fun purchase() {
        val current = _state.value
        val ticketTypeId = current.selectedTicketTypeId ?: return
        if (!current.canPurchase) return

        _state.update { it.copy(isPurchasing = true, errorMessage = null) }

        viewModelScope.launch {
            val createResult = purchaseRepository.createPurchase(
                listOf(PurchaseRequest(ticketTypeId = ticketTypeId, quantity = current.quantity))
            )

            createResult.onFailure { error ->
                _state.update { it.copy(isPurchasing = false, errorMessage = error.toUserMessage()) }
                return@launch
            }

            val purchase = createResult.getOrNull() ?: return@launch

            purchaseRepository.payPurchase(purchase.id)
                .onSuccess {
                    _state.update { it.copy(isPurchasing = false, purchaseSuccess = true) }
                }
                .onFailure { error ->
                    _state.update { it.copy(isPurchasing = false, errorMessage = error.toUserMessage()) }
                }
        }
    }

    fun consumeSuccess() {
        _state.update { it.copy(purchaseSuccess = false) }
    }
}
