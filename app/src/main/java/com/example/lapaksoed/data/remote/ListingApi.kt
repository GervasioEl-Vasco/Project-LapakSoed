package com.example.lapaksoed.data.remote

import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface ListingApi {
    @GET("listings")
    suspend fun getListings(
        @Query("category") category: String? = null,
        @Query("q") query: String? = null,
        @Query("minPrice") minPrice: Double? = null,
        @Query("maxPrice") maxPrice: Double? = null,
        @Query("page") page: Int = 0,
        @Query("size") size: Int = 20
    ): Response<PageResponse<ListingResponse>>
}
