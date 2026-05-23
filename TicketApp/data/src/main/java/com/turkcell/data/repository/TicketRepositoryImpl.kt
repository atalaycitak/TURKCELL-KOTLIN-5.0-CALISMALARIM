package com.turkcell.data.repository

import com.turkcell.core.domain.Purchase
import com.turkcell.core.domain.PurchaseItem
import com.turkcell.core.domain.PurchaseRequest
import com.turkcell.core.domain.Ticket
import com.turkcell.core.domain.TicketRepository
import com.turkcell.data.dto.PurchaseDto
import com.turkcell.data.dto.PurchaseRequestDto
import com.turkcell.data.dto.PurchaseRequestItemDto
import com.turkcell.data.remote.TicketApi
import com.turkcell.data.util.runCatchingApi

class TicketRepositoryImpl(
    private val ticketApi: TicketApi
) : TicketRepository {

    override suspend fun getMyTickets(): Result<List<Ticket>> = runCatchingApi {
        ticketApi.getMyTickets()
    }.map { dtoList ->
        dtoList.map { it.toDomainTicket() }
    }

    override suspend fun getTicketById(id: String): Result<Ticket> = runCatchingApi {
        ticketApi.getTicketById(id)
    }.map { it.toDomainTicket() }

    private fun com.turkcell.data.dto.TicketDto.toDomainTicket() = Ticket(
        id = id,
        qrCode = qrCode,
        status = status,
        usedAt = usedAt,
        ticketTypeName = ticketType.name,
        priceCents = ticketType.priceCents,
        eventName = ticketType.event.name,
        venue = ticketType.event.place,
        startsAt = ticketType.event.startsAt
    )

    override suspend fun createPurchase(items: List<PurchaseRequest>): Result<Purchase> = runCatchingApi {
        ticketApi.createPurchase(
            PurchaseRequestDto(
                items = items.map { PurchaseRequestItemDto(it.ticketTypeId, it.quantity) }
            )
        )
    }.map { it.toDomain() }

    override suspend fun payPurchase(purchaseId: String): Result<Purchase> = runCatchingApi {
        ticketApi.payPurchase(purchaseId)
    }.map { it.toDomain() }

    private fun PurchaseDto.toDomain() = Purchase(
        id = id,
        status = status,
        totalCents = totalCents,
        items = items.map { PurchaseItem(it.ticketTypeId, it.quantity, it.unitPriceCents) },
        tickets = tickets.map { Ticket(
            id = it.id,
            qrCode = it.qrCode,
            status = it.status,
            usedAt = null,
            ticketTypeName = "",
            priceCents = 0,
            eventName = "",
            venue = "",
            startsAt = ""
        )}
    )
}
