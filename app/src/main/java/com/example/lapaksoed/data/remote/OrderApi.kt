package com.example.lapaksoed.data.remote

import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query
import java.util.UUID

data class OrderResponse(
    val id: UUID,
    val listingId: UUID,
    val itemTitle: String,
    val itemImageUrl: String?,
    val buyerId: UUID,
    val buyerName: String,
    val sellerId: UUID,
    val sellerName: String,
    val unitPrice: Double,
    val quantity: Int,
    val totalPrice: Double,
    val paymentMethod: String,
    val status: String,
    val createdAt: String,
    val updatedAt: String
)

interface OrderApi {
    @retrofit2.http.POST("orders")
    suspend fun createOrder(@retrofit2.http.Body request: CreateOrderRequest): Response<OrderResponse>

    @GET("orders/mine")
    suspend fun getMyOrders(
        @Query("page") page: Int = 0,
        @Query("size") size: Int = 20
    ): Response<PageResponse<OrderResponse>>
}

data class CreateOrderRequest(
    val listingId: UUID,
    val quantity: Int = 1,
    val paymentMethod: String
)
