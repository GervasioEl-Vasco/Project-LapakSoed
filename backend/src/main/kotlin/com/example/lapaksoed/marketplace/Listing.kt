package com.example.lapaksoed.marketplace

import com.example.lapaksoed.auth.User
import jakarta.persistence.CollectionTable
import jakarta.persistence.Column
import jakarta.persistence.ElementCollection
import jakarta.persistence.Entity
import jakarta.persistence.EnumType
import jakarta.persistence.Enumerated
import jakarta.persistence.FetchType
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.JoinColumn
import jakarta.persistence.ManyToOne
import jakarta.persistence.OrderColumn
import jakarta.persistence.Table
import java.math.BigDecimal
import java.time.Instant
import java.util.UUID

enum class ListingStatus { AVAILABLE, RESERVED, SOLD }
enum class ItemCondition { NEW, LIKE_NEW, GOOD, FAIR }

@Entity
@Table(name = "listings")
class Listing(
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    var id: UUID? = null,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "seller_id", nullable = false)
    var seller: User = User(),

    @Column(nullable = false, length = 120)
    var title: String = "",

    @Column(nullable = false, length = 2000)
    var description: String = "",

    @Column(nullable = false, precision = 12, scale = 2)
    var price: BigDecimal = BigDecimal.ZERO,

    @Column(nullable = false, length = 40)
    var category: String = "",

    @Enumerated(EnumType.STRING)
    @Column(name = "item_condition", nullable = false, length = 20)
    var itemCondition: ItemCondition = ItemCondition.GOOD,

    @Column(nullable = false, length = 160)
    var location: String = "",

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    var status: ListingStatus = ListingStatus.AVAILABLE,

    @ElementCollection
    @CollectionTable(name = "listing_images", joinColumns = [JoinColumn(name = "listing_id")])
    @OrderColumn(name = "image_order")
    @Column(name = "image_url", nullable = false, length = 2048)
    var imageUrls: MutableList<String> = mutableListOf(),

    @Column(name = "created_at", nullable = false)
    var createdAt: Instant = Instant.now(),

    @Column(name = "updated_at", nullable = false)
    var updatedAt: Instant = Instant.now(),
)