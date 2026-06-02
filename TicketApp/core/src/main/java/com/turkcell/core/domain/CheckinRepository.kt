package com.turkcell.core.domain

interface CheckinRepository {
    suspend fun scanTicket(qrCode: String): Result<Unit>
}
