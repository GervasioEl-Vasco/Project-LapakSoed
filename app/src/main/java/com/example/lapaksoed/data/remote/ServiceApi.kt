package com.example.lapaksoed.data.remote

import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query
import java.util.UUID

data class CreateServiceRequest(
    val deviceCategory: String,
    val complaint: String,
    val pickupLocation: String,
    val paymentMethod: String,
)

data class ServiceRequestResponse(
    val id: UUID,
    val deviceCategory: String,
    val complaint: String,
    val pickupLocation: String,
    val paymentMethod: String,
    val status: String,
    val createdAt: String,
    val updatedAt: String,
)

interface ServiceApi {
    @POST("service-requests")
    suspend fun create(@Body request: CreateServiceRequest): Response<ServiceRequestResponse>

    @GET("service-requests/mine")
    suspend fun getMine(
        @Query("page") page: Int = 0,
        @Query("size") size: Int = 20,
    ): Response<PageResponse<ServiceRequestResponse>>
}
