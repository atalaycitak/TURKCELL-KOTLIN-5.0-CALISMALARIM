package com.turkcell.data.remote

import com.turkcell.data.dto.PurchaseDto
import com.turkcell.data.dto.PurchaseRequestDto
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface PurchaseApi {
    @GET("/me/purchases")
    suspend fun getMyPurchases(): List<PurchaseDto>

    @POST("/purchases")
    suspend fun createPurchase(@Body body: PurchaseRequestDto): PurchaseDto

    @POST("/purchases/{id}/pay")
    suspend fun payPurchase(@Path("id") id: String): PurchaseDto

    @GET("/purchases/{id}")
    suspend fun getPurchase(@Path("id") id: String): PurchaseDto
}
