package com.example.lapaksoed.services

import com.example.lapaksoed.auth.User
import jakarta.validation.Valid
import org.springframework.data.domain.Page
import org.springframework.http.HttpStatus
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/v1/service-requests")
class ServiceRequestController(private val serviceRequestService: ServiceRequestService) {
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    fun create(@AuthenticationPrincipal user: User, @Valid @RequestBody request: CreateServiceRequest) =
        serviceRequestService.create(user, request)

    @GetMapping("/mine")
    fun history(
        @AuthenticationPrincipal user: User,
        @RequestParam(defaultValue = "0") page: Int,
        @RequestParam(defaultValue = "20") size: Int,
    ): Page<ServiceRequestResponse> = serviceRequestService.history(user, page, size)
}