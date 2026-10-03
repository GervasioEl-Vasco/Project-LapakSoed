package com.example.lapaksoed.marketplace

import org.springframework.data.jpa.repository.JpaRepository
import java.util.UUID

interface FavoriteRepository : JpaRepository<Favorite, UUID> {
    fun findAllByUser_IdOrderByCreatedAtDesc(userId: UUID): List<Favorite>
    fun findByUser_IdAndListing_Id(userId: UUID, listingId: UUID): Favorite?
}