package com.turkcell.data.repository

import com.turkcell.core.domain.Event
import com.turkcell.core.domain.EventRepository
import com.turkcell.core.domain.TicketType
import com.turkcell.data.remote.EventApi
import com.turkcell.data.util.runCatchingApi

class EventRepositoryImpl(
    private val eventApi: EventApi
) : EventRepository {

    override suspend fun getUpcomingEvents(): Result<List<Event>> = runCatchingApi {
        eventApi.getEvents(upcoming = true)
    }.map { dtoList ->
        dtoList.map { dto ->
            Event(
                id = dto.id,
                name = dto.name,
                description = dto.description,
                venue = dto.venue,
                startsAt = dto.startsAt,
                endsAt = dto.endsAt,
                ticketTypes = dto.ticketTypes.map { tt ->
                    TicketType(
                        id = tt.id,
                        name = tt.name,
                        priceCents = tt.priceCents,
                        capacity = tt.capacity,
                        soldCount = tt.soldCount,
                        remaining = tt.remaining
                    )
                }
            )
        }
    }

    override suspend fun getEventById(id: String): Result<Event> = runCatchingApi {
        eventApi.getEventById(id)
    }.map { dto ->
        Event(
            id = dto.id,
            name = dto.name,
            description = dto.description,
            venue = dto.venue,
            startsAt = dto.startsAt,
            endsAt = dto.endsAt,
            ticketTypes = dto.ticketTypes.map { tt ->
                TicketType(
                    id = tt.id,
                    name = tt.name,
                    priceCents = tt.priceCents,
                    capacity = tt.capacity,
                    soldCount = tt.soldCount,
                    remaining = tt.remaining
                )
            }
        )
    }
}
