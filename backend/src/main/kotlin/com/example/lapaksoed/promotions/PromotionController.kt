package com.example.lapaksoed.promotions

import jakarta.validation.Valid
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestHeader
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestController
import java.util.UUID

@RestController
@RequestMapping("/api/v1/promotions")
class PromotionController(private val promotionService: PromotionService) {
    @GetMapping
    fun listVisible() = promotionService.visiblePromotions()
}

@RestController
@RequestMapping("/api/v1/admin/promotions")
class PromotionAdminController(private val promotionService: PromotionService) {
    @GetMapping
    fun listAll(@RequestHeader("X-Admin-Key", required = false) adminKey: String?) =
        promotionService.run { requireAdminKey(adminKey); allPromotions() }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    fun create(
        @RequestHeader("X-Admin-Key", required = false) adminKey: String?,
        @Valid @RequestBody request: UpsertPromotionRequest,
    ) = promotionService.run { requireAdminKey(adminKey); create(request) }

    @PutMapping("/{id}")
    fun update(
        @RequestHeader("X-Admin-Key", required = false) adminKey: String?,
        @PathVariable id: UUID,
        @Valid @RequestBody request: UpsertPromotionRequest,
    ) = promotionService.run { requireAdminKey(adminKey); update(id, request) }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    fun delete(
        @RequestHeader("X-Admin-Key", required = false) adminKey: String?,
        @PathVariable id: UUID,
    ) = promotionService.run { requireAdminKey(adminKey); delete(id) }
}