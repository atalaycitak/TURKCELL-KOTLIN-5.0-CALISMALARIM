package com.turkcell.data.dto

import kotlinx.serialization.Serializable

@Serializable
data class EventSummaryDto(
    val id: String,
    val name: String,
    val place: String,
    val startsAt: String
)

@Serializable
data class TicketTypeDetailDto(
    val id: String,
    val name: String,
    val priceCents: Int,
    val event: EventSummaryDto
)

@Serializable
data class TicketDto(
    val id: String,
    val qrCode: String,
    val status: String,
    val usedAt: String? = null,
    val checkedInBy: String? = null,
    val ticketType: TicketTypeDetailDto
)
