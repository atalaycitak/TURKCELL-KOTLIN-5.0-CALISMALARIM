package com.turkcell.core.domain

data class PurchaseRequest(
    val ticketTypeId: String,
    val quantity: Int
)

interface PurchaseRepository {
    suspend fun createPurchase(items: List<PurchaseRequest>): Result<Purchase>
    suspend fun payPurchase(purchaseId: String): Result<Purchase>
    suspend fun getPurchase(purchaseId: String): Result<Purchase>
}
