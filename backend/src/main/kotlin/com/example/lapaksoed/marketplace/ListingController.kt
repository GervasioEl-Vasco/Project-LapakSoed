package com.example.lapaksoed.marketplace

import com.example.lapaksoed.auth.User
import jakarta.validation.Valid
import org.springframework.data.domain.Page
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PatchMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import java.math.BigDecimal
import java.util.UUID

@RestController
@RequestMapping("/api/v1/listings")
class ListingController(private val listingService: ListingService) {
    @GetMapping
    fun browse(
        @RequestParam(required = false) category: String?,
        @RequestParam(required = false, name = "q") query: String?,
        @RequestParam(required = false) minPrice: BigDecimal?,
        @RequestParam(required = false) maxPrice: BigDecimal?,
        @RequestParam(defaultValue = "0") page: Int,
        @RequestParam(defaultValue = "20") size: Int,
    ): Page<ListingResponse> = listingService.browse(category, query, minPrice, maxPrice, page, size)

    @GetMapping("/{id}")
    fun get(@PathVariable id: UUID) = listingService.get(id)

    @GetMapping("/mine")
    fun mine(@AuthenticationPrincipal user: User, @RequestParam(defaultValue = "0") page: Int, @RequestParam(defaultValue = "20") size: Int) =
        listingService.mine(user, page, size)

    @PostMapping
    fun create(@AuthenticationPrincipal user: User, @Valid @RequestBody request: ListingRequest) = listingService.create(user, request)

    @PutMapping("/{id}")
    fun update(@PathVariable id: UUID, @AuthenticationPrincipal user: User, @Valid @RequestBody request: ListingRequest) =
        listingService.update(id, user, request)

    @PatchMapping("/{id}/status")
    fun updateStatus(@PathVariable id: UUID, @AuthenticationPrincipal user: User, @Valid @RequestBody request: ListingStatusRequest) =
        listingService.updateStatus(id, user, request.status)

    @DeleteMapping("/{id}")
    fun delete(@PathVariable id: UUID, @AuthenticationPrincipal user: User) = listingService.delete(id, user)
}