package com.turkcell.ticketapp.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.turkcell.core.domain.Event
import com.turkcell.core.domain.EventRepository
import com.turkcell.data.network.ApiException
import com.turkcell.data.network.NetworkException
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class EventListUiState(
    val events: List<Event> = emptyList(),
    val isLoading: Boolean = false,
    val errorMessage: String? = null
)

class EventListViewModel(
    private val eventRepository: EventRepository
) : ViewModel() {
    private val _state = MutableStateFlow(EventListUiState())
    val state: StateFlow<EventListUiState> = _state.asStateFlow()

    init {
        loadEvents()
    }

    fun refresh() {
        loadEvents()
    }

    private fun loadEvents() {
        _state.update { it.copy(isLoading = true, errorMessage = null) }
        viewModelScope.launch {
            eventRepository.getUpcomingEvents()
                .onSuccess { events ->
                    _state.update { it.copy(isLoading = false, events = events) }
                }
                .onFailure { error ->
                    _state.update { it.copy(isLoading = false, errorMessage = error.toUserMessage()) }
                }
        }
    }
}
