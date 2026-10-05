package com.example.lapaksoed.data.repository

import com.example.lapaksoed.data.remote.ApiClient
import com.example.lapaksoed.data.remote.SendMessageRequest
import com.example.lapaksoed.data.remote.StartConversationRequest
import java.util.UUID

class ChatRepository {
    private val chatApi = ApiClient.chatApi

    suspend fun getConversations() = chatApi.getConversations()
    
    suspend fun startConversation(listingId: UUID) = chatApi.startConversation(StartConversationRequest(listingId))
    
    suspend fun getMessages(conversationId: UUID, page: Int = 0) = chatApi.getMessages(conversationId, page, 50)
    
    suspend fun sendMessage(conversationId: UUID, body: String) = chatApi.sendMessage(conversationId, SendMessageRequest(body))
}
