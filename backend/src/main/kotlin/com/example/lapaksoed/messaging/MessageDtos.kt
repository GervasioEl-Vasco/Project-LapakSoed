package com.example.lapaksoed.messaging

import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.Size
import java.time.Instant
import java.util.UUID

data class StartConversationRequest(val listingId: UUID)
data class SendMessageRequest(@field:NotBlank @field:Size(max = 2000) val body: String)

data class ConversationResponse(
    val id: UUID,
    val listingId: UUID,
    val listingTitle: String,
    val otherUserId: UUID,
    val otherUserName: String,
    val lastMessageAt: Instant,
)

data class MessageResponse(
    val id: UUID,
    val conversationId: UUID,
    val senderId: UUID,
    val senderName: String,
    val body: String,
    val createdAt: Instant,
)

fun Conversation.toResponse(currentUserId: UUID): ConversationResponse {
    val otherUser = if (buyer.id == currentUserId) seller else buyer
    return ConversationResponse(
        id = requireNotNull(id),
        listingId = requireNotNull(listing.id),
        listingTitle = listing.title,
        otherUserId = requireNotNull(otherUser.id),
        otherUserName = otherUser.fullName,
        lastMessageAt = lastMessageAt,
    )
}

fun Message.toResponse() = MessageResponse(
    id = requireNotNull(id),
    conversationId = requireNotNull(conversation.id),
    senderId = requireNotNull(sender.id),
    senderName = sender.fullName,
    body = body,
    createdAt = createdAt,
)