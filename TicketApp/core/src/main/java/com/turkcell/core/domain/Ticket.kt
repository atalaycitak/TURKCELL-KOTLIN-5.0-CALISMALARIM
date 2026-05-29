package com.turkcell.core.domain

data class Ticket(
    val id: String,
    val qrCode: String,
    val status: TicketStatus,
    val usedAt: String?,
    val ticketTypeName: String,
    val priceCents: Int,
    val eventName: String,
    val venue: String,
    val startsAt: String
)
