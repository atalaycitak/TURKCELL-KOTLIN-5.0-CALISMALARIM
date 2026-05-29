package com.turkcell.core.domain

data class PurchaseItem(
    val ticketTypeId: String,
    val quantity: Int,
    val unitPriceCents: Int
)

data class Purchase(
    val id: String,
    val status: PurchaseStatus,
    val totalCents: Int,
    val items: List<PurchaseItem>,
    val tickets: List<Ticket>
)
