package com.example.lapaksoed.services

import com.example.lapaksoed.orders.PaymentMethod
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.NotNull
import jakarta.validation.constraints.Size
import java.time.Instant
import java.util.UUID

data class CreateServiceRequest(
    @field:NotNull val deviceCategory: DeviceCategory,
    @field:NotBlank @field:Size(max = 2000) val complaint: String,
    @field:NotBlank @field:Size(max = 300) val pickupLocation: String,
    @field:NotNull val paymentMethod: PaymentMethod,
)

data class ServiceRequestResponse(
    val id: UUID,
    val deviceCategory: DeviceCategory,
    val complaint: String,
    val pickupLocation: String,
    val paymentMethod: PaymentMethod,
    val status: ServiceRequestStatus,
    val createdAt: Instant,
    val updatedAt: Instant,
)

fun ServiceRequest.toResponse() = ServiceRequestResponse(
    id = requireNotNull(id),
    deviceCategory = deviceCategory,
    complaint = complaint,
    pickupLocation = pickupLocation,
    paymentMethod = paymentMethod,
    status = status,
    createdAt = createdAt,
    updatedAt = updatedAt,
)