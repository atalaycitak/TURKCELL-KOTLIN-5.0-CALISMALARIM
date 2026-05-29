package com.turkcell.data.repository

import com.turkcell.core.domain.Ticket
import com.turkcell.core.domain.TicketRepository
import com.turkcell.core.domain.TicketStatus
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
        status = TicketStatus.fromApi(status),
        usedAt = usedAt,
        ticketTypeName = ticketType.name,
        priceCents = ticketType.priceCents,
        eventName = ticketType.event.name,
        venue = ticketType.event.place,
        startsAt = ticketType.event.startsAt
    )
}

