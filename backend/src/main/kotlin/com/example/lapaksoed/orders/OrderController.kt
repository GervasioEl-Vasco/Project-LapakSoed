package com.example.lapaksoed.orders

import com.example.lapaksoed.auth.User
import jakarta.validation.Valid
import org.springframework.data.domain.Page
import org.springframework.http.HttpStatus
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PatchMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestController
import java.util.UUID

@RestController
@RequestMapping("/api/v1/orders")
class OrderController(private val orderService: OrderService) {
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    fun create(@AuthenticationPrincipal user: User, @Valid @RequestBody request: CreateOrderRequest) =
        orderService.create(user, request)

    @GetMapping("/mine")
    fun history(
        @AuthenticationPrincipal user: User,
        @RequestParam(defaultValue = "0") page: Int,
        @RequestParam(defaultValue = "20") size: Int,
    ): Page<OrderResponse> = orderService.history(user, page, size)

    @PatchMapping("/{orderId}/status")
    fun updateStatus(
        @AuthenticationPrincipal user: User,
        @PathVariable orderId: UUID,
        @Valid @RequestBody request: UpdateOrderStatusRequest,
    ) = orderService.updateStatus(user, orderId, request.status)
}