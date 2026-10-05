package com.example.lapaksoed.data.remote

import retrofit2.Response
import retrofit2.http.*
import java.util.UUID

interface ChatApi {
    @GET("conversations")
    suspend fun getConversations(): Response<List<ConversationResponse>>

    @POST("conversations")
    suspend fun startConversation(@Body request: StartConversationRequest): Response<ConversationResponse>

    @GET("conversations/{id}/messages")
    suspend fun getMessages(
        @Path("id") id: UUID,
        @Query("page") page: Int = 0,
        @Query("size") size: Int = 50
    ): Response<List<MessageResponse>>

    @POST("conversations/{id}/messages")
    suspend fun sendMessage(
        @Path("id") id: UUID,
        @Body request: SendMessageRequest
    ): Response<MessageResponse>
}
