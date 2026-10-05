package com.example.lapaksoed.data.remote

import com.google.gson.annotations.SerializedName

data class PageResponse<T>(
    val content: List<T>,
    val totalElements: Int,
    val totalPages: Int,
    val size: Int,
    val number: Int
)

data class ListingResponse(
    val id: String,
    val sellerId: String,
    val sellerName: String,
    val title: String,
    val description: String,
    val price: Double,
    val category: String,
    val itemCondition: String,
    val location: String,
    val status: String,
    val imageUrls: List<String>,
    val createdAt: String,
    val updatedAt: String
)
