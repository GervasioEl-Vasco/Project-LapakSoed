package com.example.lapaksoed.services

import com.example.lapaksoed.auth.User
import com.example.lapaksoed.orders.PaymentMethod
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.EnumType
import jakarta.persistence.Enumerated
import jakarta.persistence.FetchType
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.JoinColumn
import jakarta.persistence.ManyToOne
import jakarta.persistence.Table
import java.time.Instant
import java.util.UUID

enum class DeviceCategory { SMARTPHONE, LAPTOP_PC, TABLET, OTHER_COMPONENT }
enum class ServiceRequestStatus { NEW, IN_PROGRESS, COMPLETED, CANCELLED }

@Entity
@Table(name = "service_requests")
class ServiceRequest(
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    var id: UUID? = null,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "customer_id", nullable = false)
    var customer: User = User(),

    @Enumerated(EnumType.STRING)
    @Column(name = "device_category", nullable = false, length = 30)
    var deviceCategory: DeviceCategory = DeviceCategory.SMARTPHONE,

    @Column(nullable = false, length = 2000)
    var complaint: String = "",

    @Column(name = "pickup_location", nullable = false, length = 300)
    var pickupLocation: String = "",

    @Enumerated(EnumType.STRING)
    @Column(name = "payment_method", nullable = false, length = 30)
    var paymentMethod: PaymentMethod = PaymentMethod.BANK_TRANSFER,

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    var status: ServiceRequestStatus = ServiceRequestStatus.NEW,

    @Column(name = "created_at", nullable = false)
    var createdAt: Instant = Instant.now(),

    @Column(name = "updated_at", nullable = false)
    var updatedAt: Instant = Instant.now(),
)