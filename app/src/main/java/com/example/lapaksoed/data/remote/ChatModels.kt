package com.example.lapaksoed.data.remote

import java.util.UUID

data class StartConversationRequest(val listingId: UUID)
data class SendMessageRequest(val body: String)

data class ConversationResponse(
    val id: UUID,
    val listingId: UUID,
    val listingTitle: String,
    val otherUserId: UUID,
    val otherUserName: String,
    val lastMessageAt: String
)

data class MessageResponse(
    val id: UUID,
    val conversationId: UUID,
    val senderId: UUID,
    val senderName: String,
    val body: String,
    val createdAt: String
)
