package com.turkcell.ticketapp.viewmodel

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.turkcell.core.domain.Ticket
import com.turkcell.core.domain.TicketRepository
import com.turkcell.ticketapp.util.toUserMessage
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class MyTicketsUiState(
    val tickets: List<Ticket> = emptyList(),
    val isLoading: Boolean = false,
    val errorMessage: String? = null
)

class MyTicketsViewModel(
    private val ticketRepository: TicketRepository,
    private val savedStateHandle: SavedStateHandle
) : ViewModel() {
    private val _state = MutableStateFlow(MyTicketsUiState())
    val state: StateFlow<MyTicketsUiState> = _state.asStateFlow()

    init {
        loadTickets()
    }

    fun refresh() {
        loadTickets()
    }

    private fun loadTickets() {
        _state.update { it.copy(isLoading = true, errorMessage = null) }
        viewModelScope.launch {
            ticketRepository.getMyTickets()
                .onSuccess { tickets ->
                    _state.update { it.copy(isLoading = false, tickets = tickets) }
                }
                .onFailure { error ->
                    _state.update { it.copy(isLoading = false, errorMessage = error.toUserMessage()) }
                }
        }
    }
}
