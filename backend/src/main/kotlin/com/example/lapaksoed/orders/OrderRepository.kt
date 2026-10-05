package com.example.lapaksoed.orders

import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.JpaRepository
import java.util.UUID

interface OrderRepository : JpaRepository<Order, UUID> {
    fun findByBuyer_IdOrSeller_IdOrderByCreatedAtDesc(buyerId: UUID, sellerId: UUID, pageable: Pageable): Page<Order>
}