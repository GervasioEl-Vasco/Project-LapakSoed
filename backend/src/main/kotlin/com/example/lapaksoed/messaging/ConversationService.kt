package com.example.lapaksoed.messaging

import com.example.lapaksoed.auth.User
import com.example.lapaksoed.auth.UserRepository
import com.example.lapaksoed.marketplace.ListingRepository
import org.springframework.data.domain.PageRequest
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import org.springframework.web.server.ResponseStatusException
import java.time.Instant
import java.util.UUID

@Service
class ConversationService(
    private val conversationRepository: ConversationRepository,
    private val messageRepository: MessageRepository,
    private val listingRepository: ListingRepository,
    private val userRepository: UserRepository,
) {
    @Transactional
    fun start(user: User, listingId: UUID): ConversationResponse {
        val buyerId = requireNotNull(user.id)
        val listing = listingRepository.findById(listingId)
            .orElseThrow { ResponseStatusException(HttpStatus.NOT_FOUND, "Barang tidak ditemukan") }
        if (listing.seller.id == buyerId) throw ResponseStatusException(HttpStatus.BAD_REQUEST, "Tidak dapat memulai chat pada barang sendiri")
        val conversation = conversationRepository.findByListing_IdAndBuyer_Id(listingId, buyerId)
            ?: conversationRepository.save(
                Conversation(
                    listing = listing,
                    buyer = userRepository.getReferenceById(buyerId),
                    seller = listing.seller,
                ),
            )
        return conversation.toResponse(buyerId)
    }

    @Transactional(readOnly = true)
    fun list(user: User): List<ConversationResponse> {
        val userId = requireNotNull(user.id)
        return conversationRepository.findByBuyer_IdOrSeller_IdOrderByLastMessageAtDesc(userId, userId)
            .map { it.toResponse(userId) }
    }

    @Transactional(readOnly = true)
    fun messages(user: User, conversationId: UUID, page: Int, size: Int): List<MessageResponse> {
        val conversation = getConversationForUser(conversationId, user)
        return messageRepository.findByConversation_IdOrderByCreatedAtAsc(
            requireNotNull(conversation.id),
            PageRequest.of(page.coerceAtLeast(0), size.coerceIn(1, 100)),
        ).map(Message::toResponse)
    }

    @Transactional
    fun send(user: User, conversationId: UUID, request: SendMessageRequest): MessageResponse {
        val conversation = getConversationForUser(conversationId, user)
        val now = Instant.now()
        conversation.lastMessageAt = now
        val message = messageRepository.save(
            Message(
                conversation = conversation,
                sender = userRepository.getReferenceById(requireNotNull(user.id)),
                body = request.body.trim(),
                createdAt = now,
            ),
        )
        return message.toResponse()
    }

    private fun getConversationForUser(id: UUID, user: User): Conversation {
        val conversation = conversationRepository.findById(id)
            .orElseThrow { ResponseStatusException(HttpStatus.NOT_FOUND, "Percakapan tidak ditemukan") }
        val userId = requireNotNull(user.id)
        if (conversation.buyer.id != userId && conversation.seller.id != userId) {
            throw ResponseStatusException(HttpStatus.FORBIDDEN, "Anda bukan peserta percakapan ini")
        }
        return conversation
    }
}