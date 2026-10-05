package com.example.lapaksoed.data.remote

import retrofit2.Response
import retrofit2.http.GET
import java.util.UUID

data class PromotionResponse(
    val id: UUID,
    val title: String,
    val imageUrl: String,
    val targetUrl: String,
    val active: Boolean,
    val displayOrder: Int,
    val startsAt: String,
    val endsAt: String?
)

interface PromotionApi {
    @GET("promotions")
    suspend fun getPromotions(): Response<List<PromotionResponse>>
}
