package com.example.lapaksoed.data.repository

import com.example.lapaksoed.data.remote.ApiClient

class PromotionRepository {
    suspend fun getPromotions() = ApiClient.promotionApi.getPromotions()
}
