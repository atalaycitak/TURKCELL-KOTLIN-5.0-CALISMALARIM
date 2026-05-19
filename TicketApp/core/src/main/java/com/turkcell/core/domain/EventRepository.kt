package com.turkcell.core.domain

interface EventRepository {
    suspend fun getUpcomingEvents(): Result<List<Event>>
    suspend fun getEventById(id: String): Result<Event>
}
