package com.example.lapaksoed.data.repository

import com.example.lapaksoed.data.remote.ApiClient
import com.example.lapaksoed.data.remote.ListingResponse
import com.example.lapaksoed.data.remote.PageResponse
import retrofit2.Response

class ListingRepository {
    private val api = ApiClient.listingApi

    suspend fun getListings(
        category: String? = null,
        query: String? = null,
        page: Int = 0,
        size: Int = 20
    ): Response<PageResponse<ListingResponse>> {
        return api.getListings(category = category, query = query, page = page, size = size)
    }
}
