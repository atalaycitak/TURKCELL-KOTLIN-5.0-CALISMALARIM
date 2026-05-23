package com.turkcell.ticketapp.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.turkcell.core.domain.Ticket
import com.turkcell.core.domain.TicketRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class TicketDetailUiState(
    val ticket: Ticket? = null,
    val isLoading: Boolean = false,
    val errorMessage: String? = null
)

class TicketDetailViewModel(
    private val ticketId: String,
    private val ticketRepository: TicketRepository
) : ViewModel() {
    private val _state = MutableStateFlow(TicketDetailUiState())
    val state: StateFlow<TicketDetailUiState> = _state.asStateFlow()

    init {
        loadTicket()
    }

    fun loadTicket() {
        _state.update { it.copy(isLoading = true, errorMessage = null) }
        viewModelScope.launch {
            ticketRepository.getTicketById(ticketId)
                .onSuccess { ticket ->
                    _state.update { it.copy(isLoading = false, ticket = ticket) }
                }
                .onFailure { error ->
                    _state.update { it.copy(isLoading = false, errorMessage = error.toUserMessage()) }
                }
        }
    }
}
