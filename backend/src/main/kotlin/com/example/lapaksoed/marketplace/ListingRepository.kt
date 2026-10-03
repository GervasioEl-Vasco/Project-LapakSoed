package com.example.lapaksoed.marketplace

import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import java.math.BigDecimal
import java.util.UUID

interface ListingRepository : JpaRepository<Listing, UUID> {
    @Query(
        """SELECT l FROM Listing l WHERE (:status IS NULL OR l.status = :status)
        AND (:category IS NULL OR LOWER(l.category) = LOWER(:category))
        AND (:search IS NULL OR LOWER(l.title) LIKE LOWER(CONCAT('%', :search, '%')) OR LOWER(l.description) LIKE LOWER(CONCAT('%', :search, '%')))
        AND (:minPrice IS NULL OR l.price >= :minPrice)
        AND (:maxPrice IS NULL OR l.price <= :maxPrice)
        AND (:sellerId IS NULL OR l.seller.id = :sellerId)""",
    )
    fun search(
        @Param("status") status: ListingStatus?,
        @Param("category") category: String?,
        @Param("search") search: String?,
        @Param("minPrice") minPrice: BigDecimal?,
        @Param("maxPrice") maxPrice: BigDecimal?,
        @Param("sellerId") sellerId: UUID?,
        pageable: Pageable,
    ): Page<Listing>
}