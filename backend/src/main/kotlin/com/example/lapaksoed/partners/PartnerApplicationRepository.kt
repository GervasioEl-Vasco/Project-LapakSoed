package com.example.lapaksoed.partners

import org.springframework.data.jpa.repository.JpaRepository
import java.util.UUID

interface PartnerApplicationRepository : JpaRepository<PartnerApplication, UUID> {
    fun findByUser_Id(userId: UUID): PartnerApplication?
}
