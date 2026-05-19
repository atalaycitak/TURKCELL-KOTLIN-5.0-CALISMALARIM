package com.turkcell.core.domain

data class PurchaseRequest(
    val ticketTypeId: String,
    val quantity: Int
)

interface TicketRepository {
    suspend fun getMyTickets(): Result<List<Ticket>>
    suspend fun createPurchase(items: List<PurchaseRequest>): Result<Purchase>
    suspend fun payPurchase(purchaseId: String): Result<Purchase>
}
