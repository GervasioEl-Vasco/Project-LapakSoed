package com.example.lapaksoed.marketplace

import com.example.lapaksoed.auth.User
import com.example.lapaksoed.auth.UserRepository
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Sort
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import org.springframework.web.server.ResponseStatusException
import java.math.BigDecimal
import java.time.Instant
import java.util.UUID

@Service
class ListingService(
    private val listingRepository: ListingRepository,
    private val userRepository: UserRepository,
) {
    private val categories = setOf(
        "Makanan", "Minuman", "Pakaian", "Barang", "Jasa Service",
        "Buku", "Elektronik", "Aksesoris", "Perlengkapan", "Lainnya",
    )

    @Transactional(readOnly = true)
    fun browse(category: String?, query: String?, minPrice: BigDecimal?, maxPrice: BigDecimal?, page: Int, size: Int): Page<ListingResponse> {
        if (minPrice != null && maxPrice != null && minPrice > maxPrice) {
            throw ResponseStatusException(HttpStatus.BAD_REQUEST, "minPrice tidak boleh lebih besar dari maxPrice")
        }
        val results = listingRepository.search(
            status = ListingStatus.AVAILABLE,
            category = category?.trim().orEmpty(),
            search = query?.trim().orEmpty(),
            minPrice = minPrice,
            maxPrice = maxPrice,
            sellerId = null,
            pageable = PageRequest.of(page.coerceAtLeast(0), size.coerceIn(1, 50), Sort.by(Sort.Direction.DESC, "createdAt")),
        )
        return results.map(Listing::toResponse)
    }

    @Transactional(readOnly = true)
    fun get(id: UUID): ListingResponse = findListing(id).toResponse()

    @Transactional
    fun create(user: User, request: ListingRequest): ListingResponse {
        val seller = userRepository.getReferenceById(requireNotNull(user.id))
        val entity = Listing(seller = seller).apply { updateFrom(request) }
        return listingRepository.save(entity).toResponse()
    }

    @Transactional
    fun update(id: UUID, user: User, request: ListingRequest): ListingResponse {
        val entity = findListing(id)
        ensureOwner(entity, user)
        entity.updateFrom(request)
        entity.updatedAt = Instant.now()
        return entity.toResponse()
    }

    @Transactional
    fun updateStatus(id: UUID, user: User, status: ListingStatus): ListingResponse {
        val entity = findListing(id)
        ensureOwner(entity, user)
        entity.status = status
        entity.updatedAt = Instant.now()
        return entity.toResponse()
    }

    @Transactional
    fun delete(id: UUID, user: User) {
        val entity = findListing(id)
        ensureOwner(entity, user)
        listingRepository.delete(entity)
    }

    @Transactional(readOnly = true)
    fun mine(user: User, page: Int, size: Int): Page<ListingResponse> = listingRepository.search(
        status = null,
        category = "",
        search = "",
        minPrice = null,
        maxPrice = null,
        sellerId = requireNotNull(user.id),
        pageable = PageRequest.of(page.coerceAtLeast(0), size.coerceIn(1, 50), Sort.by(Sort.Direction.DESC, "createdAt")),
    ).map(Listing::toResponse)

    private fun findListing(id: UUID) = listingRepository.findById(id)
        .orElseThrow { ResponseStatusException(HttpStatus.NOT_FOUND, "Barang tidak ditemukan") }

    private fun ensureOwner(listing: Listing, user: User) {
        if (listing.seller.id != user.id) throw ResponseStatusException(HttpStatus.FORBIDDEN, "Hanya pemilik barang yang dapat mengubahnya")
    }

    private fun Listing.updateFrom(request: ListingRequest) {
        val normalizedCategory = categories.firstOrNull { it.equals(request.category.trim(), ignoreCase = true) }
            ?: throw ResponseStatusException(HttpStatus.BAD_REQUEST, "Kategori tidak dikenal")
        val condition = runCatching { ItemCondition.valueOf(request.itemCondition.trim().uppercase()) }
            .getOrElse { throw ResponseStatusException(HttpStatus.BAD_REQUEST, "Kondisi barang tidak dikenal") }
        if (request.price < BigDecimal.ZERO) throw ResponseStatusException(HttpStatus.BAD_REQUEST, "Harga tidak boleh negatif")
        if (request.imageUrls.any { it.length > 2048 || !it.startsWith("https://") }) {
            throw ResponseStatusException(HttpStatus.BAD_REQUEST, "URL foto harus menggunakan HTTPS")
        }
        title = request.title.trim()
        description = request.description.trim()
        price = request.price
        category = normalizedCategory
        itemCondition = condition
        location = request.location.trim()
        imageUrls = request.imageUrls.map(String::trim).toMutableList()
    }
}