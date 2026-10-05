package com.example.lapaksoed.marketplace

import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.Lock
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import jakarta.persistence.LockModeType
import java.math.BigDecimal
import java.util.Optional
import java.util.UUID

interface ListingRepository : JpaRepository<Listing, UUID> {
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("SELECT l FROM Listing l WHERE l.id = :id")
    fun findByIdForUpdate(@Param("id") id: UUID): Optional<Listing>

    @Query(
        """SELECT l FROM Listing l WHERE (:status IS NULL OR l.status = :status)
        AND (:category = '' OR LOWER(l.category) = LOWER(:category))
        AND (:search = '' OR LOWER(l.title) LIKE LOWER(CONCAT('%', :search, '%')) OR LOWER(l.description) LIKE LOWER(CONCAT('%', :search, '%')))
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