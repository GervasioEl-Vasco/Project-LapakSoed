package com.example.lapaksoed.partners

import com.example.lapaksoed.auth.User
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.EnumType
import jakarta.persistence.Enumerated
import jakarta.persistence.FetchType
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.JoinColumn
import jakarta.persistence.OneToOne
import jakarta.persistence.Table
import java.time.Instant
import java.util.UUID

enum class PartnerApplicationStatus { PENDING, APPROVED, REJECTED }

@Entity
@Table(name = "partner_applications")
class PartnerApplication(
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    var id: UUID? = null,

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false, unique = true)
    var user: User = User(),

    @Column(name = "business_name", nullable = false, length = 120)
    var businessName: String = "",

    @Column(name = "contact_phone", nullable = false, length = 30)
    var contactPhone: String = "",

    @Column(nullable = false, length = 1000)
    var description: String = "",

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    var status: PartnerApplicationStatus = PartnerApplicationStatus.PENDING,

    @Column(name = "created_at", nullable = false)
    var createdAt: Instant = Instant.now(),

    @Column(name = "updated_at", nullable = false)
    var updatedAt: Instant = Instant.now(),
)
