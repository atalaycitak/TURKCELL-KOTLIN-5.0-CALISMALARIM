package com.turkcell.data.dto

import kotlinx.serialization.Serializable

@Serializable
data class PurchaseRequestItemDto(
    val ticketTypeId: String,
    val quantity: Int
)

@Serializable
data class PurchaseRequestDto(
    val items: List<PurchaseRequestItemDto>
)

@Serializable
data class PurchaseItemDto(
    val id: String,
    val ticketTypeId: String,
    val quantity: Int,
    val unitPriceCents: Int
)

@Serializable
data class PurchaseTicketDto(
    val id: String,
    val qrCode: String,
    val status: String,
    val ticketTypeId: String
)

@Serializable
data class PurchaseDto(
    val id: String,
    val userId: String? = null,
    val status: String,
    val totalCents: Int,
    val createdAt: String? = null,
    val paidAt: String? = null,
    val items: List<PurchaseItemDto> = emptyList(),
    val tickets: List<PurchaseTicketDto> = emptyList()
)
