package com.turkcell.data.remote

import com.turkcell.data.dto.PurchaseDto
import com.turkcell.data.dto.PurchaseRequestDto
import com.turkcell.data.dto.TicketDto
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface TicketApi {
    @GET("/me/tickets")
    suspend fun getMyTickets(): List<TicketDto>

    @GET("/me/tickets/{id}")
    suspend fun getTicketById(@Path("id") id: String): TicketDto

    @POST("/purchases")
    suspend fun createPurchase(@Body body: PurchaseRequestDto): PurchaseDto

    @POST("/purchases/{id}/pay")
    suspend fun payPurchase(@Path("id") id: String): PurchaseDto
}
