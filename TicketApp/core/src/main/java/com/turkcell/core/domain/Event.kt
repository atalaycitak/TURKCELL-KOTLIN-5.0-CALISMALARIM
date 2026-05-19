package com.turkcell.core.domain

data class TicketType(
    val id: String,
    val name: String,
    val priceCents: Int,
    val capacity: Int,
    val soldCount: Int,
    val remaining: Int
)

data class Event(
    val id: String,
    val name: String,
    val description: String,
    val venue: String,
    val startsAt: String,
    val endsAt: String,
    val ticketTypes: List<TicketType>
)
