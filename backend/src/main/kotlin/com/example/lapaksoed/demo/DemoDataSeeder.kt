package com.example.lapaksoed.demo

import com.example.lapaksoed.auth.User
import com.example.lapaksoed.auth.UserRepository
import com.example.lapaksoed.marketplace.ItemCondition
import com.example.lapaksoed.marketplace.Listing
import com.example.lapaksoed.marketplace.ListingRepository
import com.example.lapaksoed.promotions.Promotion
import com.example.lapaksoed.promotions.PromotionRepository
import org.springframework.boot.CommandLineRunner
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Transactional
import java.math.BigDecimal
import java.time.Instant

@Component
@ConditionalOnProperty(prefix = "app.demo-data", name = ["enabled"], havingValue = "true")
class DemoDataSeeder(
    private val userRepository: UserRepository,
    private val listingRepository: ListingRepository,
    private val promotionRepository: PromotionRepository,
    private val passwordEncoder: PasswordEncoder,
) : CommandLineRunner {
    @Transactional
    override fun run(vararg args: String) {
        val seller = userRepository.findByEmailIgnoreCase(DEMO_SELLER_EMAIL)
            ?: userRepository.save(
                User(
                    email = DEMO_SELLER_EMAIL,
                    passwordHash = passwordEncoder.encode(DEMO_PASSWORD),
                    fullName = "Penjual Demo LapakSoed",
                    nim = "DEMO-SELLER-001",
                ),
            )
        val buyer = userRepository.findByEmailIgnoreCase(DEMO_BUYER_EMAIL)
            ?: userRepository.save(
                User(
                    email = DEMO_BUYER_EMAIL,
                    passwordHash = passwordEncoder.encode(DEMO_PASSWORD),
                    fullName = "Pembeli Demo LapakSoed",
                    nim = "DEMO-BUYER-001",
                ),
            )

        val existingTitles = listingRepository.findAll()
            .filter { it.seller.id == seller.id }
            .map { it.title }
            .toSet()
        val now = Instant.now()
        demoListings.filterNot { it.title in existingTitles }.forEach { demo ->
            listingRepository.save(
                Listing(
                    seller = seller,
                    title = demo.title,
                    description = demo.description,
                    price = BigDecimal(demo.price),
                    category = demo.category,
                    itemCondition = demo.condition,
                    location = "Purwokerto, UNSOED",
                    imageUrls = mutableListOf(demo.imageUrl),
                    createdAt = now,
                    updatedAt = now,
                ),
            )
        }

        val existingPromotions = promotionRepository.findAll()
        demoPromotions.forEachIndexed { index, promo ->
            val existing = existingPromotions.firstOrNull { it.title == promo.title }
            if (existing != null) {
                existing.imageUrl = promo.imageUrl
                existing.targetUrl = promo.targetUrl
                existing.active = true
                existing.displayOrder = demoPromotions.size - index
                existing.updatedAt = now
            } else {
                promotionRepository.save(
                    Promotion(
                    title = promo.title,
                    imageUrl = promo.imageUrl,
                    targetUrl = promo.targetUrl,
                    active = true,
                    displayOrder = demoPromotions.size - index,
                    startsAt = now.minusSeconds(60),
                    endsAt = now.plusSeconds(60L * 60 * 24 * 365),
                    createdAt = now,
                    updatedAt = now,
                    ),
                )
            }
        }

        logger.info(
            "Demo data ready: seller={}, buyer={}, listings={}, promotions={}",
            seller.email,
            buyer.email,
            listingRepository.count(),
            promotionRepository.count(),
        )
    }

    private data class DemoListing(
        val title: String,
        val description: String,
        val price: String,
        val category: String,
        val condition: ItemCondition,
        val imageUrl: String,
    )

    private data class DemoPromotion(val title: String, val imageUrl: String, val targetUrl: String)

    private val demoListings = listOf(
        DemoListing("Buku Algoritma dan Pemrograman", "Buku kuliah, kondisi rapi dan lengkap.", "45000", "Buku", ItemCondition.GOOD, "https://images.unsplash.com/photo-1544947950-fa07a98d237f"),
        DemoListing("Kalkulator Scientific", "Kalkulator scientific untuk kebutuhan kuliah.", "85000", "Perlengkapan", ItemCondition.LIKE_NEW, "https://images.unsplash.com/photo-1509228468518-180dd4864904"),
        DemoListing("Headphone Kabel", "Headphone berfungsi normal, suara jernih.", "55000", "Elektronik", ItemCondition.GOOD, "https://images.unsplash.com/photo-1505740420928-5e560c06d30e"),
        DemoListing("Tas Kuliah", "Tas kuliah dengan beberapa kompartemen.", "70000", "Pakaian", ItemCondition.GOOD, "https://images.unsplash.com/photo-1553062407-98eeb64c6a62"),
        DemoListing("Botol Minum Stainless", "Menjaga minuman tetap dingin atau hangat.", "35000", "Minuman", ItemCondition.LIKE_NEW, "https://images.unsplash.com/photo-1602143407151-7111542de6e8"),
        DemoListing("Piring dan Alat Makan", "Paket alat makan kos, masih layak pakai.", "25000", "Barang", ItemCondition.GOOD, "https://images.unsplash.com/photo-1490645935967-10de6ba17061"),
        DemoListing("Gantungan Kunci Unsoed", "Aksesoris kecil untuk teman atau koleksi.", "12000", "Aksesoris", ItemCondition.LIKE_NEW, "https://images.unsplash.com/photo-1523275335684-37898b6baf30"),
        DemoListing("Paket Snack Belajar", "Paket camilan untuk menemani belajar.", "18000", "Makanan", ItemCondition.NEW, "https://images.unsplash.com/photo-1621939514649-280e2aa7f1d1"),
        DemoListing("Jasa Servis Laptop", "Pemeriksaan dan konsultasi ringan area Purwokerto.", "50000", "Jasa Service", ItemCondition.NEW, "https://images.unsplash.com/photo-1581092918056-0c4c3acd3789"),
        DemoListing("Barang Campuran Kos", "Perlengkapan kos yang masih berfungsi baik.", "20000", "Lainnya", ItemCondition.FAIR, "https://images.unsplash.com/photo-1494438639946-1ebd1d20bf85"),
    )

    private val demoPromotions = listOf(
        DemoPromotion("Promo Mahasiswa Baru", "https://images.unsplash.com/photo-1523240795612-9a054b0db644", "/promotions/mahasiswa-baru"),
        DemoPromotion("Minggu Hemat Anak Kos", "https://images.unsplash.com/photo-1542838132-92c53300491e", "/promotions/minggu-hemat"),
        DemoPromotion("Barang Pilihan Minggu Ini", "https://images.unsplash.com/photo-1490312278390-ab64016e0aa9", "/promotions/pilihan-minggu-ini"),
    )

    private companion object {
        const val DEMO_SELLER_EMAIL = "penjual.demo@mhs.unsoed.ac.id"
        const val DEMO_BUYER_EMAIL = "pembeli.demo@mhs.unsoed.ac.id"
        const val DEMO_PASSWORD = "LapakDemo#2026"
        val logger = org.slf4j.LoggerFactory.getLogger(DemoDataSeeder::class.java)
    }
}
