package com.example.lapaksoed.orders

import com.example.lapaksoed.auth.User
import com.example.lapaksoed.marketplace.Listing
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
import java.math.BigDecimal
import java.time.Instant
import java.util.UUID

enum class OrderStatus { NEW, IN_PROGRESS, COMPLETED, CANCELLED }
enum class PaymentMethod { BANK_TRANSFER, DANA, GOPAY, QRIS, CASH_ON_DELIVERY }

@Entity
@Table(name = "orders")
class Order(
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    var id: UUID? = null,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "listing_id", nullable = false)
    var listing: Listing = Listing(),

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "buyer_id", nullable = false)
    var buyer: User = User(),

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "seller_id", nullable = false)
    var seller: User = User(),

    @Column(name = "item_title", nullable = false, length = 120)
    var itemTitle: String = "",

    @Column(name = "item_image_url", length = 2048)
    var itemImageUrl: String? = null,

    @Column(name = "unit_price", nullable = false, precision = 12, scale = 2)
    var unitPrice: BigDecimal = BigDecimal.ZERO,

    @Column(nullable = false)
    var quantity: Int = 1,

    @Column(name = "total_price", nullable = false, precision = 12, scale = 2)
    var totalPrice: BigDecimal = BigDecimal.ZERO,

    @Enumerated(EnumType.STRING)
    @Column(name = "payment_method", nullable = false, length = 30)
    var paymentMethod: PaymentMethod = PaymentMethod.BANK_TRANSFER,

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    var status: OrderStatus = OrderStatus.NEW,

    @Column(name = "created_at", nullable = false)
    var createdAt: Instant = Instant.now(),

    @Column(name = "updated_at", nullable = false)
    var updatedAt: Instant = Instant.now(),
)