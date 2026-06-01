package com.turkcell.data.repository

import com.turkcell.core.domain.Purchase
import com.turkcell.core.domain.PurchaseItem
import com.turkcell.core.domain.PurchaseRepository
import com.turkcell.core.domain.PurchaseRequest
import com.turkcell.core.domain.PurchaseStatus
import com.turkcell.core.domain.Ticket
import com.turkcell.core.domain.TicketStatus
import com.turkcell.data.dto.PurchaseDto
import com.turkcell.data.dto.PurchaseRequestDto
import com.turkcell.data.dto.PurchaseRequestItemDto
import com.turkcell.data.remote.PurchaseApi
import com.turkcell.data.util.runCatchingApi

class PurchaseRepositoryImpl(
    private val purchaseApi: PurchaseApi
) : PurchaseRepository {

    override suspend fun getMyPurchases(): Result<List<Purchase>> = runCatchingApi {
        purchaseApi.getMyPurchases()
    }.map { dtos -> dtos.map { it.toDomain() } }

    override suspend fun createPurchase(items: List<PurchaseRequest>): Result<Purchase> =
        runCatchingApi {
            purchaseApi.createPurchase(
                PurchaseRequestDto(
                    items = items.map { PurchaseRequestItemDto(it.ticketTypeId, it.quantity) }
                )
            )
        }.map { it.toDomain() }

    override suspend fun payPurchase(purchaseId: String): Result<Purchase> = runCatchingApi {
        purchaseApi.payPurchase(purchaseId)
    }.map { it.toDomain() }

    override suspend fun getPurchase(purchaseId: String): Result<Purchase> = runCatchingApi {
        purchaseApi.getPurchase(purchaseId)
    }.map { it.toDomain() }

    private fun PurchaseDto.toDomain() = Purchase(
        id = id,
        status = PurchaseStatus.fromApi(status),
        totalCents = totalCents,
        items = items.map { PurchaseItem(it.ticketTypeId, it.quantity, it.unitPriceCents) },
        tickets = tickets.map {
            Ticket(
                id = it.id,
                qrCode = it.qrCode,
                status = TicketStatus.fromApi(it.status),
                usedAt = null,
                ticketTypeName = "",
                priceCents = 0,
                eventName = "",
                venue = "",
                startsAt = ""
            )
        },
        createdAt = createdAt
    )
}
