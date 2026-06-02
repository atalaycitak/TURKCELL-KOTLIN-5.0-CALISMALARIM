package com.turkcell.data.remote

import kotlinx.serialization.Serializable
import retrofit2.http.Body
import retrofit2.http.POST

@Serializable
data class ScanRequestDto(val qrCode: String)

interface CheckinApi {
    @POST("/checkin/scan")
    suspend fun scanTicket(@Body request: ScanRequestDto)
}
