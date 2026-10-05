package com.example.lapaksoed.data.remote

import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import java.util.UUID

data class CreatePartnerApplicationRequest(
    val businessName: String,
    val contactPhone: String,
    val description: String
)

data class PartnerApplicationResponse(
    val id: UUID,
    val businessName: String,
    val contactPhone: String,
    val description: String,
    val status: String,
    val createdAt: String,
    val updatedAt: String
)

interface PartnerApi {
    @POST("partners/applications")
    suspend fun apply(@Body request: CreatePartnerApplicationRequest): Response<PartnerApplicationResponse>

    @GET("partners/applications/mine")
    suspend fun myApplication(): Response<PartnerApplicationResponse?>
}
