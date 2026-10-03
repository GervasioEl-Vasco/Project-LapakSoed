package com.example.lapaksoed.messaging

import com.example.lapaksoed.auth.User
import jakarta.validation.Valid
import org.springframework.http.HttpStatus
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestController
import java.util.UUID

@RestController
@RequestMapping("/api/v1/conversations")
class ConversationController(private val conversationService: ConversationService) {
    @PostMapping
    fun start(@AuthenticationPrincipal user: User, @Valid @RequestBody request: StartConversationRequest) =
        conversationService.start(user, request.listingId)

    @GetMapping
    fun list(@AuthenticationPrincipal user: User) = conversationService.list(user)

    @GetMapping("/{id}/messages")
    fun messages(
        @AuthenticationPrincipal user: User,
        @PathVariable id: UUID,
        @RequestParam(defaultValue = "0") page: Int,
        @RequestParam(defaultValue = "50") size: Int,
    ) = conversationService.messages(user, id, page, size)

    @PostMapping("/{id}/messages")
    @ResponseStatus(HttpStatus.CREATED)
    fun send(@AuthenticationPrincipal user: User, @PathVariable id: UUID, @Valid @RequestBody request: SendMessageRequest) =
        conversationService.send(user, id, request)
}