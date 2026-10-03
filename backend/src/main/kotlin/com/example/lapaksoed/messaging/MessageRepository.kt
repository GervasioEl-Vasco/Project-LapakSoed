package com.example.lapaksoed.messaging

import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.JpaRepository
import java.util.UUID

interface MessageRepository : JpaRepository<Message, UUID> {
    fun findByConversation_IdOrderByCreatedAtAsc(conversationId: UUID, pageable: Pageable): List<Message>
}