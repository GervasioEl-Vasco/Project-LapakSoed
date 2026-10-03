package com.example.lapaksoed.marketplace

import com.example.lapaksoed.auth.User
import org.springframework.http.HttpStatus
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestController
import java.util.UUID

@RestController
@RequestMapping("/api/v1/favorites")
class FavoriteController(private val favoriteService: FavoriteService) {
    @GetMapping
    fun list(@AuthenticationPrincipal user: User) = favoriteService.list(user)

    @PostMapping("/{listingId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    fun add(@AuthenticationPrincipal user: User, @PathVariable listingId: UUID) = favoriteService.add(user, listingId)

    @DeleteMapping("/{listingId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    fun remove(@AuthenticationPrincipal user: User, @PathVariable listingId: UUID) = favoriteService.remove(user, listingId)
}