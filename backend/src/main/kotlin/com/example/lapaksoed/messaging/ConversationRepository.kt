package com.example.lapaksoed.messaging

import org.springframework.data.jpa.repository.JpaRepository
import java.util.UUID

interface ConversationRepository : JpaRepository<Conversation, UUID> {
    fun findByListing_IdAndBuyer_Id(listingId: UUID, buyerId: UUID): Conversation?
    fun findByBuyer_IdOrSeller_IdOrderByLastMessageAtDesc(buyerId: UUID, sellerId: UUID): List<Conversation>
}