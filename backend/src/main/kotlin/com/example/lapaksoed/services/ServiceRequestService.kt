package com.example.lapaksoed.services

import com.example.lapaksoed.auth.User
import com.example.lapaksoed.auth.UserRepository
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Sort
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.time.Instant

@Service
class ServiceRequestService(
    private val serviceRequestRepository: ServiceRequestRepository,
    private val userRepository: UserRepository,
) {
    @Transactional
    fun create(customer: User, request: CreateServiceRequest): ServiceRequestResponse {
        val now = Instant.now()
        val serviceRequest = ServiceRequest(
            customer = userRepository.getReferenceById(requireNotNull(customer.id)),
            deviceCategory = request.deviceCategory,
            complaint = request.complaint.trim(),
            pickupLocation = request.pickupLocation.trim(),
            paymentMethod = request.paymentMethod,
            createdAt = now,
            updatedAt = now,
        )
        return serviceRequestRepository.save(serviceRequest).toResponse()
    }

    @Transactional(readOnly = true)
    fun history(customer: User, page: Int, size: Int): Page<ServiceRequestResponse> =
        serviceRequestRepository.findByCustomer_IdOrderByCreatedAtDesc(
            requireNotNull(customer.id),
            PageRequest.of(page.coerceAtLeast(0), size.coerceIn(1, 50), Sort.by(Sort.Direction.DESC, "createdAt")),
        ).map(ServiceRequest::toResponse)
}