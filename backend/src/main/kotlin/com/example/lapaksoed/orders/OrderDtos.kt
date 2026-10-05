package com.example.lapaksoed.orders

import jakarta.validation.constraints.Max
import jakarta.validation.constraints.Min
import jakarta.validation.constraints.NotNull
import java.math.BigDecimal
import java.time.Instant
import java.util.UUID

data class CreateOrderRequest(
    @field:NotNull val listingId: UUID,
    @field:Min(1) @field:Max(1) val quantity: Int = 1,
    @field:NotNull val paymentMethod: PaymentMethod,
)

data class UpdateOrderStatusRequest(@field:NotNull val status: OrderStatus)

data class OrderResponse(
    val id: UUID,
    val listingId: UUID,
    val itemTitle: String,
    val itemImageUrl: String?,
    val buyerId: UUID,
    val buyerName: String,
    val sellerId: UUID,
    val sellerName: String,
    val unitPrice: BigDecimal,
    val quantity: Int,
    val totalPrice: BigDecimal,
    val paymentMethod: PaymentMethod,
    val status: OrderStatus,
    val createdAt: Instant,
    val updatedAt: Instant,
)

fun Order.toResponse() = OrderResponse(
    id = requireNotNull(id),
    listingId = requireNotNull(listing.id),
    itemTitle = itemTitle,
    itemImageUrl = itemImageUrl,
    buyerId = requireNotNull(buyer.id),
    buyerName = buyer.fullName,
    sellerId = requireNotNull(seller.id),
    sellerName = seller.fullName,
    unitPrice = unitPrice,
    quantity = quantity,
    totalPrice = totalPrice,
    paymentMethod = paymentMethod,
    status = status,
    createdAt = createdAt,
    updatedAt = updatedAt,
)