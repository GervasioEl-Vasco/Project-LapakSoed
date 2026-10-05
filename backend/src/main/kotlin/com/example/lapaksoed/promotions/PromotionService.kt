package com.example.lapaksoed.promotions

import org.springframework.beans.factory.annotation.Value
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import org.springframework.web.server.ResponseStatusException
import java.net.URI
import java.nio.charset.StandardCharsets
import java.security.MessageDigest
import java.time.Instant
import java.util.UUID

@Service
class PromotionService(
    private val promotionRepository: PromotionRepository,
    @param:Value("\${app.promotions.admin-key:}") private val configuredAdminKey: String,
) {
    @Transactional(readOnly = true)
    fun visiblePromotions(): List<PromotionResponse> =
        promotionRepository.findVisibleAt(Instant.now()).map(Promotion::toResponse)

    @Transactional(readOnly = true)
    fun allPromotions(): List<PromotionResponse> =
        promotionRepository.findAllByOrderByDisplayOrderDescStartsAtDesc().map(Promotion::toResponse)

    @Transactional
    fun create(request: UpsertPromotionRequest): PromotionResponse {
        validate(request)
        val now = Instant.now()
        return promotionRepository.save(
            Promotion(
                title = request.title.trim(),
                imageUrl = request.imageUrl.trim(),
                targetUrl = request.targetUrl.trim(),
                active = request.active,
                displayOrder = request.displayOrder,
                startsAt = request.startsAt,
                endsAt = request.endsAt,
                createdAt = now,
                updatedAt = now,
            ),
        ).toResponse()
    }

    @Transactional
    fun update(id: UUID, request: UpsertPromotionRequest): PromotionResponse {
        validate(request)
        val promotion = promotionRepository.findById(id).orElseThrow {
            ResponseStatusException(HttpStatus.NOT_FOUND, "Promosi tidak ditemukan")
        }
        promotion.title = request.title.trim()
        promotion.imageUrl = request.imageUrl.trim()
        promotion.targetUrl = request.targetUrl.trim()
        promotion.active = request.active
        promotion.displayOrder = request.displayOrder
        promotion.startsAt = request.startsAt
        promotion.endsAt = request.endsAt
        promotion.updatedAt = Instant.now()
        return promotion.toResponse()
    }

    @Transactional
    fun delete(id: UUID) {
        if (!promotionRepository.existsById(id)) {
            throw ResponseStatusException(HttpStatus.NOT_FOUND, "Promosi tidak ditemukan")
        }
        promotionRepository.deleteById(id)
    }

    fun requireAdminKey(providedKey: String?) {
        if (configuredAdminKey.isBlank()) {
            throw ResponseStatusException(
                HttpStatus.SERVICE_UNAVAILABLE,
                "Pengelolaan promosi belum diaktifkan",
            )
        }
        val providedBytes = providedKey.orEmpty().toByteArray(StandardCharsets.UTF_8)
        val configuredBytes = configuredAdminKey.toByteArray(StandardCharsets.UTF_8)
        if (!MessageDigest.isEqual(configuredBytes, providedBytes)) {
            throw ResponseStatusException(HttpStatus.UNAUTHORIZED, "Admin key tidak valid")
        }
    }

    private fun validate(request: UpsertPromotionRequest) {
        if (request.endsAt != null && !request.endsAt.isAfter(request.startsAt)) {
            throw ResponseStatusException(HttpStatus.BAD_REQUEST, "endsAt harus setelah startsAt")
        }
        validateLink(request.imageUrl, "imageUrl")
        validateLink(request.targetUrl, "targetUrl")
    }

    private fun validateLink(value: String, field: String) {
        val link = value.trim()
        val isSafeAppPath = link.startsWith("/") && !link.startsWith("//") && '\\' !in link
        val isHttpsUrl = runCatching {
            val uri = URI(link)
            uri.isAbsolute && uri.scheme.equals("https", ignoreCase = true) && !uri.host.isNullOrBlank()
        }.getOrDefault(false)
        if (!isSafeAppPath && !isHttpsUrl) {
            throw ResponseStatusException(
                HttpStatus.BAD_REQUEST,
                "$field harus berupa path aplikasi atau URL HTTPS",
            )
        }
    }
}