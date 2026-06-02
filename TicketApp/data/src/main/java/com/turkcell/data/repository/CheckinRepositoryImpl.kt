package com.turkcell.data.repository

import com.turkcell.core.domain.CheckinRepository
import com.turkcell.data.remote.CheckinApi
import com.turkcell.data.remote.ScanRequestDto
import com.turkcell.data.util.runCatchingApi

class CheckinRepositoryImpl(
    private val checkinApi: CheckinApi
) : CheckinRepository {

    override suspend fun scanTicket(qrCode: String): Result<Unit> = runCatchingApi {
        checkinApi.scanTicket(ScanRequestDto(qrCode))
    }
}
