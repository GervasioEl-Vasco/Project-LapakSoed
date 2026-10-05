package com.example.lapaksoed.promotions

import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.NotNull
import jakarta.validation.constraints.Size
import java.time.Instant
import java.util.UUID

data class UpsertPromotionRequest(
    @field:NotBlank @field:Size(max = 120) val title: String,
    @field:NotBlank @field:Size(max = 2048) val imageUrl: String,
    @field:NotBlank @field:Size(max = 2048) val targetUrl: String,
    val active: Boolean = true,
    val displayOrder: Int = 0,
    @field:NotNull val startsAt: Instant,
    val endsAt: Instant? = null,
)

data class PromotionResponse(
    val id: UUID,
    val title: String,
    val imageUrl: String,
    val targetUrl: String,
    val active: Boolean,
    val displayOrder: Int,
    val startsAt: Instant,
    val endsAt: Instant?,
)

fun Promotion.toResponse() = PromotionResponse(
    id = requireNotNull(id),
    title = title,
    imageUrl = imageUrl,
    targetUrl = targetUrl,
    active = active,
    displayOrder = displayOrder,
    startsAt = startsAt,
    endsAt = endsAt,
)