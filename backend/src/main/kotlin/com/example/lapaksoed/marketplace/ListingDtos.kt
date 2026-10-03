package com.example.lapaksoed.marketplace

import jakarta.validation.constraints.DecimalMin
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.NotNull
import jakarta.validation.constraints.Size
import java.math.BigDecimal
import java.time.Instant
import java.util.UUID

data class ListingRequest(
    @field:NotBlank @field:Size(max = 120) val title: String,
    @field:NotBlank @field:Size(max = 2000) val description: String,
    @field:NotNull @field:DecimalMin("0.0") val price: BigDecimal,
    @field:NotBlank val category: String,
    @field:NotBlank val itemCondition: String,
    @field:NotBlank @field:Size(max = 160) val location: String,
    @field:Size(max = 8) val imageUrls: List<String> = emptyList(),
)

data class ListingResponse(
    val id: UUID,
    val sellerId: UUID,
    val sellerName: String,
    val title: String,
    val description: String,
    val price: BigDecimal,
    val category: String,
    val itemCondition: ItemCondition,
    val location: String,
    val status: ListingStatus,
    val imageUrls: List<String>,
    val createdAt: Instant,
    val updatedAt: Instant,
)

data class ListingStatusRequest(@field:NotNull val status: ListingStatus)

fun Listing.toResponse() = ListingResponse(
    id = requireNotNull(id),
    sellerId = requireNotNull(seller.id),
    sellerName = seller.fullName,
    title = title,
    description = description,
    price = price,
    category = category,
    itemCondition = itemCondition,
    location = location,
    status = status,
    imageUrls = imageUrls.toList(),
    createdAt = createdAt,
    updatedAt = updatedAt,
)