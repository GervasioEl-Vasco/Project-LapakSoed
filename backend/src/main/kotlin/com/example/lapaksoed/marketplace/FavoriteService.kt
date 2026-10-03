package com.example.lapaksoed.marketplace

import com.example.lapaksoed.auth.User
import com.example.lapaksoed.auth.UserRepository
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import org.springframework.web.server.ResponseStatusException
import java.util.UUID

@Service
class FavoriteService(
    private val favoriteRepository: FavoriteRepository,
    private val listingRepository: ListingRepository,
    private val userRepository: UserRepository,
) {
    @Transactional(readOnly = true)
    fun list(user: User): List<ListingResponse> = favoriteRepository
        .findAllByUser_IdOrderByCreatedAtDesc(requireNotNull(user.id))
        .map { it.listing.toResponse() }

    @Transactional
    fun add(user: User, listingId: UUID) {
        val userId = requireNotNull(user.id)
        if (favoriteRepository.findByUser_IdAndListing_Id(userId, listingId) != null) return
        val listing = listingRepository.findById(listingId)
            .orElseThrow { ResponseStatusException(HttpStatus.NOT_FOUND, "Barang tidak ditemukan") }
        if (listing.seller.id == userId) throw ResponseStatusException(HttpStatus.BAD_REQUEST, "Tidak dapat menyimpan barang sendiri")
        favoriteRepository.save(
            Favorite(
                user = userRepository.getReferenceById(userId),
                listing = listing,
            ),
        )
    }

    @Transactional
    fun remove(user: User, listingId: UUID) {
        favoriteRepository.findByUser_IdAndListing_Id(requireNotNull(user.id), listingId)?.let(favoriteRepository::delete)
    }
}