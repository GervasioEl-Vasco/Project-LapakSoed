package com.example.lapaksoed.services

import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.JpaRepository
import java.util.UUID

interface ServiceRequestRepository : JpaRepository<ServiceRequest, UUID> {
    fun findByCustomer_IdOrderByCreatedAtDesc(customerId: UUID, pageable: Pageable): Page<ServiceRequest>
}