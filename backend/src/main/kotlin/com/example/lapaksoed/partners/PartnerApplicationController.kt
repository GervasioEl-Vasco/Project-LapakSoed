package com.example.lapaksoed.partners

import com.example.lapaksoed.auth.User
import jakarta.validation.Valid
import org.springframework.http.HttpStatus
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/v1/partners/applications")
class PartnerApplicationController(private val partnerApplicationService: PartnerApplicationService) {
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    fun apply(
        @AuthenticationPrincipal user: User,
        @Valid @RequestBody request: CreatePartnerApplicationRequest,
    ) = partnerApplicationService.apply(user, request)

    @GetMapping("/mine")
    fun mine(@AuthenticationPrincipal user: User) = partnerApplicationService.mine(user)
}
