package com.example.lapaksoed.orders

import com.example.lapaksoed.auth.User
import com.example.lapaksoed.auth.UserRepository
import com.example.lapaksoed.marketplace.ListingRepository
import com.example.lapaksoed.marketplace.ListingStatus
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Sort
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import org.springframework.web.server.ResponseStatusException
import java.time.Instant
import java.util.UUID

@Service
class OrderService(
    private val orderRepository: OrderRepository,
    private val listingRepository: ListingRepository,
    private val userRepository: UserRepository,
) {
    @Transactional
    fun create(buyer: User, request: CreateOrderRequest): OrderResponse {
        val listing = listingRepository.findByIdForUpdate(request.listingId)
            .orElseThrow { ResponseStatusException(HttpStatus.NOT_FOUND, "Barang tidak ditemukan") }
        if (listing.seller.id == buyer.id) {
            throw ResponseStatusException(HttpStatus.BAD_REQUEST, "Tidak dapat memesan barang sendiri")
        }
        if (listing.status != ListingStatus.AVAILABLE) {
            throw ResponseStatusException(HttpStatus.CONFLICT, "Barang sudah dipesan atau terjual")
        }

        val buyerReference = userRepository.getReferenceById(requireNotNull(buyer.id))
        val order = Order(
            listing = listing,
            buyer = buyerReference,
            seller = listing.seller,
            itemTitle = listing.title,
            itemImageUrl = listing.imageUrls.firstOrNull(),
            unitPrice = listing.price,
            quantity = request.quantity,
            totalPrice = listing.price.multiply(request.quantity.toBigDecimal()),
            paymentMethod = request.paymentMethod,
        )
        listing.status = ListingStatus.RESERVED
        listing.updatedAt = Instant.now()
        return orderRepository.save(order).toResponse()
    }

    @Transactional(readOnly = true)
    fun history(user: User, page: Int, size: Int): Page<OrderResponse> {
        val userId = requireNotNull(user.id)
        return orderRepository.findByBuyer_IdOrSeller_IdOrderByCreatedAtDesc(
            userId,
            userId,
            PageRequest.of(page.coerceAtLeast(0), size.coerceIn(1, 50), Sort.by(Sort.Direction.DESC, "createdAt")),
        ).map(Order::toResponse)
    }

    @Transactional
    fun updateStatus(user: User, orderId: UUID, requestedStatus: OrderStatus): OrderResponse {
        val order = orderRepository.findById(orderId)
            .orElseThrow { ResponseStatusException(HttpStatus.NOT_FOUND, "Pesanan tidak ditemukan") }
        val userId = requireNotNull(user.id)
        val isBuyer = order.buyer.id == userId
        val isSeller = order.seller.id == userId
        if (!isBuyer && !isSeller) {
            throw ResponseStatusException(HttpStatus.FORBIDDEN, "Anda bukan peserta pesanan ini")
        }

        val allowed = when (requestedStatus) {
            OrderStatus.CANCELLED -> (isBuyer || isSeller) && order.status in setOf(OrderStatus.NEW, OrderStatus.IN_PROGRESS)
            OrderStatus.IN_PROGRESS -> isSeller && order.status == OrderStatus.NEW
            OrderStatus.COMPLETED -> isSeller && order.status == OrderStatus.IN_PROGRESS
            OrderStatus.NEW -> false
        }
        if (!allowed) throw ResponseStatusException(HttpStatus.CONFLICT, "Perubahan status pesanan tidak diizinkan")

        order.status = requestedStatus
        order.updatedAt = Instant.now()
        if (requestedStatus == OrderStatus.CANCELLED) {
            val listing = listingRepository.findByIdForUpdate(requireNotNull(order.listing.id)).orElse(null)
            if (listing?.status == ListingStatus.RESERVED) {
                listing.status = ListingStatus.AVAILABLE
                listing.updatedAt = Instant.now()
            }
        } else if (requestedStatus == OrderStatus.COMPLETED) {
            order.listing.status = ListingStatus.SOLD
            order.listing.updatedAt = Instant.now()
        }
        return order.toResponse()
    }
}