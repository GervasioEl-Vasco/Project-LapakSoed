package com.example.lapaksoed.partners

import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.Pattern
import jakarta.validation.constraints.Size
import java.time.Instant
import java.util.UUID

data class CreatePartnerApplicationRequest(
    @field:NotBlank @field:Size(max = 120) val businessName: String,
    @field:NotBlank @field:Size(min = 8, max = 30)
    @field:Pattern(regexp = "[+0-9() -]+", message = "Nomor telepon hanya boleh berisi angka dan tanda +()-")
    val contactPhone: String,
    @field:NotBlank @field:Size(max = 1000) val description: String,
)

data class PartnerApplicationResponse(
    val id: UUID,
    val businessName: String,
    val contactPhone: String,
    val description: String,
    val status: PartnerApplicationStatus,
    val createdAt: Instant,
    val updatedAt: Instant,
)

fun PartnerApplication.toResponse() = PartnerApplicationResponse(
    id = requireNotNull(id),
    businessName = businessName,
    contactPhone = contactPhone,
    description = description,
    status = status,
    createdAt = createdAt,
    updatedAt = updatedAt,
)
