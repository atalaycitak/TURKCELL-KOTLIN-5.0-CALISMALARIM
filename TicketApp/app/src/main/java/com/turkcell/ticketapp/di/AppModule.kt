package com.turkcell.ticketapp.di

import com.turkcell.ticketapp.viewmodel.EventDetailViewModel
import com.turkcell.ticketapp.viewmodel.EventListViewModel
import com.turkcell.ticketapp.viewmodel.LoginViewModel
import com.turkcell.ticketapp.viewmodel.MyTicketsViewModel
import com.turkcell.ticketapp.viewmodel.RegisterViewModel
import com.turkcell.ticketapp.viewmodel.TicketDetailViewModel
import org.koin.core.module.dsl.viewModel
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

val appModule = module {
    viewModelOf(::LoginViewModel)
    viewModelOf(::RegisterViewModel)
    viewModelOf(::EventListViewModel)
    viewModelOf(::MyTicketsViewModel)
    viewModel { params ->
        EventDetailViewModel(
            eventId = params.get(),
            eventRepository = get(),
            purchaseRepository = get(),
            savedStateHandle = get()
        )
    }
    viewModel { params ->
        TicketDetailViewModel(
            ticketId = params.get(),
            ticketRepository = get(),
            savedStateHandle = get()
        )
    }
}