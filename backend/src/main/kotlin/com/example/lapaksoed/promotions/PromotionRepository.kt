package com.example.lapaksoed.promotions

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import java.time.Instant
import java.util.UUID

interface PromotionRepository : JpaRepository<Promotion, UUID> {
    @Query(
        """
        select promotion from Promotion promotion
        where promotion.active = true
          and promotion.startsAt <= :now
          and (promotion.endsAt is null or promotion.endsAt > :now)
        order by promotion.displayOrder desc, promotion.startsAt desc
        """,
    )
    fun findVisibleAt(@Param("now") now: Instant): List<Promotion>

    fun findAllByOrderByDisplayOrderDescStartsAtDesc(): List<Promotion>
}