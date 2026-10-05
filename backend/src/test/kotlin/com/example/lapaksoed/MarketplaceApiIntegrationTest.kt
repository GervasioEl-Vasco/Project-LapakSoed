package com.example.lapaksoed

import com.fasterxml.jackson.databind.ObjectMapper
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.MediaType
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class MarketplaceApiIntegrationTest @Autowired constructor(
    private val mockMvc: MockMvc,
    private val objectMapper: ObjectMapper,
) {
    @Test
    fun `student can submit and view a single partner application`() {
        val registration = mockMvc.perform(
            post("/api/v1/auth/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content("""{"email":"partner-${System.nanoTime()}@mhs.unsoed.ac.id","password":"rahasia123","fullName":"Calon Mitra","nim":"${System.nanoTime()}"}"""),
        ).andExpect(status().isOk).andReturn()
        val token = objectMapper.readTree(registration.response.contentAsString).get("accessToken").asText()
        val requestBody = """{"businessName":"Lapak Buku Kampus","contactPhone":"081234567890","description":"Menjual buku dan perlengkapan kuliah."}"""

        mockMvc.perform(
            post("/api/v1/partners/applications")
                .header("Authorization", "Bearer $token")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBody),
        ).andExpect(status().isCreated)
            .andExpect(jsonPath("$.businessName").value("Lapak Buku Kampus"))
            .andExpect(jsonPath("$.status").value("PENDING"))

        mockMvc.perform(get("/api/v1/partners/applications/mine").header("Authorization", "Bearer $token"))
            .andExpect(status().isOk)
            .andExpect(jsonPath("$.contactPhone").value("081234567890"))

        mockMvc.perform(
            post("/api/v1/partners/applications")
                .header("Authorization", "Bearer $token")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBody),
        ).andExpect(status().isConflict)
    }

    @Test
    fun `public can read active promotions and only admin key can manage them`() {
        val requestBody = """{"title":"Promo kampus","imageUrl":"https://cdn.example.org/promo.png","targetUrl":"/promotions/kampus","active":true,"displayOrder":10,"startsAt":"2025-01-01T00:00:00Z","endsAt":"2099-01-01T00:00:00Z"}"""

        mockMvc.perform(
            post("/api/v1/admin/promotions")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBody),
        ).andExpect(status().isUnauthorized)

        val created = mockMvc.perform(
            post("/api/v1/admin/promotions")
                .header("X-Admin-Key", "test-promotion-admin-key")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBody),
        ).andExpect(status().isCreated)
            .andExpect(jsonPath("$.title").value("Promo kampus"))
            .andReturn()
        val promotionId = objectMapper.readTree(created.response.contentAsString).get("id").asText()

        mockMvc.perform(get("/api/v1/promotions"))
            .andExpect(status().isOk)
            .andExpect(jsonPath("$[0].id").value(promotionId))
            .andExpect(jsonPath("$[0].targetUrl").value("/promotions/kampus"))

        mockMvc.perform(
            put("/api/v1/admin/promotions/$promotionId")
                .header("X-Admin-Key", "test-promotion-admin-key")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBody.replace("\"active\":true", "\"active\":false")),
        ).andExpect(status().isOk)

        mockMvc.perform(get("/api/v1/promotions"))
            .andExpect(status().isOk)
            .andExpect(jsonPath("$[?(@.id == '$promotionId')]").doesNotExist())

        mockMvc.perform(
            delete("/api/v1/admin/promotions/$promotionId")
                .header("X-Admin-Key", "test-promotion-admin-key"),
        ).andExpect(status().isNoContent)
    }

    @Test
    fun `user can register authenticate and publish a listing`() {
        val registration = mockMvc.perform(
            post("/api/v1/auth/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content("""{"email":"student-${System.nanoTime()}@mhs.unsoed.ac.id","password":"rahasia123","fullName":"Mahasiswa Unsoed","nim":"${System.nanoTime()}"}"""),
        ).andExpect(status().isOk)
            .andExpect(jsonPath("$.accessToken").isNotEmpty)
            .andReturn()

        val token = objectMapper.readTree(registration.response.contentAsString).get("accessToken").asText()

        mockMvc.perform(get("/api/v1/auth/me").header("Authorization", "Bearer $token"))
            .andExpect(status().isOk)
            .andExpect(jsonPath("$.fullName").value("Mahasiswa Unsoed"))

        val partnerRequest = """{"businessName":"Lapak Demo Mahasiswa","contactPhone":"081234567890","description":"Menjual perlengkapan kuliah dan kebutuhan kos."}"""
        mockMvc.perform(
            post("/api/v1/partners/applications")
                .header("Authorization", "Bearer $token")
                .contentType(MediaType.APPLICATION_JSON)
                .content(partnerRequest),
        ).andExpect(status().isCreated)
            .andExpect(jsonPath("$.businessName").value("Lapak Demo Mahasiswa"))
            .andExpect(jsonPath("$.status").value("PENDING"))

        mockMvc.perform(get("/api/v1/partners/applications/mine").header("Authorization", "Bearer $token"))
            .andExpect(status().isOk)
            .andExpect(jsonPath("$.contactPhone").value("081234567890"))

        mockMvc.perform(
            post("/api/v1/partners/applications")
                .header("Authorization", "Bearer $token")
                .contentType(MediaType.APPLICATION_JSON)
                .content(partnerRequest),
        ).andExpect(status().isConflict)

        val listing = mockMvc.perform(
            post("/api/v1/listings")
                .header("Authorization", "Bearer $token")
                .contentType(MediaType.APPLICATION_JSON)
                .content(
                    """{"title":"Buku kuliah","description":"Kondisi bagus","price":25000,"category":"Buku","itemCondition":"GOOD","location":"Purwokerto","imageUrls":[]}""",
                ),
        ).andExpect(status().isOk)
            .andExpect(jsonPath("$.status").value("AVAILABLE"))
            .andReturn()

        val listingId = objectMapper.readTree(listing.response.contentAsString).get("id").asText()
        mockMvc.perform(get("/api/v1/listings").param("q", "kuliah"))
            .andExpect(status().isOk)
            .andExpect(jsonPath("$.content[0].id").value(listingId))

        mockMvc.perform(get("/api/v1/listings/mine").header("Authorization", "Bearer $token"))
            .andExpect(status().isOk)
            .andExpect(jsonPath("$.content.length()").value(1))

        mockMvc.perform(get("/api/v1/listings/mine"))
            .andExpect(status().isUnauthorized)

        mockMvc.perform(
            patch("/api/v1/listings/$listingId/status")
                .header("Authorization", "Bearer $token")
                .contentType(MediaType.APPLICATION_JSON)
                .content("""{"status":"RESERVED"}"""),
        ).andExpect(status().isOk)

        mockMvc.perform(get("/api/v1/listings").param("q", "kuliah"))
            .andExpect(status().isOk)
            .andExpect(jsonPath("$.content.length()").value(0))

        mockMvc.perform(get("/api/v1/listings/mine").header("Authorization", "Bearer $token"))
            .andExpect(jsonPath("$.content[0].status").value("RESERVED"))

        val buyerRegistration = mockMvc.perform(
            post("/api/v1/auth/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content("""{"email":"buyer-${System.nanoTime()}@mhs.unsoed.ac.id","password":"rahasia123","fullName":"Pembeli Unsoed","nim":"${System.nanoTime()}"}"""),
        ).andExpect(status().isOk).andReturn()
        val buyerToken = objectMapper.readTree(buyerRegistration.response.contentAsString).get("accessToken").asText()

        mockMvc.perform(post("/api/v1/favorites/$listingId").header("Authorization", "Bearer $buyerToken"))
            .andExpect(status().isNoContent)
        mockMvc.perform(get("/api/v1/favorites").header("Authorization", "Bearer $buyerToken"))
            .andExpect(status().isOk)
            .andExpect(jsonPath("$[0].id").value(listingId))

        val conversation = mockMvc.perform(
            post("/api/v1/conversations")
                .header("Authorization", "Bearer $buyerToken")
                .contentType(MediaType.APPLICATION_JSON)
                .content("""{"listingId":"$listingId"}"""),
        ).andExpect(status().isOk).andReturn()
        val conversationId = objectMapper.readTree(conversation.response.contentAsString).get("id").asText()

        mockMvc.perform(
            post("/api/v1/conversations/$conversationId/messages")
                .header("Authorization", "Bearer $buyerToken")
                .contentType(MediaType.APPLICATION_JSON)
                .content("""{"body":"Apakah barangnya masih ada?"}"""),
        ).andExpect(status().isCreated)

        mockMvc.perform(
            get("/api/v1/conversations/$conversationId/messages").header("Authorization", "Bearer $token"),
        ).andExpect(status().isOk)
            .andExpect(jsonPath("$[0].body").value("Apakah barangnya masih ada?"))

        val orderListing = mockMvc.perform(
            post("/api/v1/listings")
                .header("Authorization", "Bearer $token")
                .contentType(MediaType.APPLICATION_JSON)
                .content("""{"title":"Headset","description":"Masih bagus","price":75000,"category":"Elektronik","itemCondition":"GOOD","location":"Purwokerto","imageUrls":[]}"""),
        ).andExpect(status().isOk).andReturn()
        val orderListingId = objectMapper.readTree(orderListing.response.contentAsString).get("id").asText()

        val createdOrder = mockMvc.perform(
            post("/api/v1/orders")
                .header("Authorization", "Bearer $buyerToken")
                .contentType(MediaType.APPLICATION_JSON)
                .content("""{"listingId":"$orderListingId","quantity":1,"paymentMethod":"QRIS"}"""),
        ).andExpect(status().isCreated)
            .andExpect(jsonPath("$.status").value("NEW"))
            .andExpect(jsonPath("$.totalPrice").value(75000))
            .andReturn()
        val orderId = objectMapper.readTree(createdOrder.response.contentAsString).get("id").asText()

        mockMvc.perform(get("/api/v1/orders/mine").header("Authorization", "Bearer $buyerToken"))
            .andExpect(status().isOk)
            .andExpect(jsonPath("$.content[0].paymentMethod").value("QRIS"))

        mockMvc.perform(
            patch("/api/v1/orders/$orderId/status")
                .header("Authorization", "Bearer $token")
                .contentType(MediaType.APPLICATION_JSON)
                .content("""{"status":"IN_PROGRESS"}"""),
        ).andExpect(status().isOk)
            .andExpect(jsonPath("$.status").value("IN_PROGRESS"))

        mockMvc.perform(
            patch("/api/v1/orders/$orderId/status")
                .header("Authorization", "Bearer $token")
                .contentType(MediaType.APPLICATION_JSON)
                .content("""{"status":"COMPLETED"}"""),
        ).andExpect(status().isOk)
            .andExpect(jsonPath("$.status").value("COMPLETED"))

        val cancelListing = mockMvc.perform(
            post("/api/v1/listings")
                .header("Authorization", "Bearer $token")
                .contentType(MediaType.APPLICATION_JSON)
                .content("""{"title":"Buku UTS","description":"Buku catatan","price":15000,"category":"Buku","itemCondition":"GOOD","location":"Purwokerto","imageUrls":[]}"""),
        ).andExpect(status().isOk).andReturn()
        val cancelListingId = objectMapper.readTree(cancelListing.response.contentAsString).get("id").asText()
        val cancellableOrder = mockMvc.perform(
            post("/api/v1/orders")
                .header("Authorization", "Bearer $buyerToken")
                .contentType(MediaType.APPLICATION_JSON)
                .content("""{"listingId":"$cancelListingId","quantity":1,"paymentMethod":"CASH_ON_DELIVERY"}"""),
        ).andExpect(status().isCreated).andReturn()
        val cancellableOrderId = objectMapper.readTree(cancellableOrder.response.contentAsString).get("id").asText()

        mockMvc.perform(
            patch("/api/v1/orders/$cancellableOrderId/status")
                .header("Authorization", "Bearer $buyerToken")
                .contentType(MediaType.APPLICATION_JSON)
                .content("""{"status":"CANCELLED"}"""),
        ).andExpect(status().isOk)
            .andExpect(jsonPath("$.status").value("CANCELLED"))

        mockMvc.perform(get("/api/v1/listings/$cancelListingId"))
            .andExpect(status().isOk)
            .andExpect(jsonPath("$.status").value("AVAILABLE"))

        val serviceRequest = mockMvc.perform(
            post("/api/v1/service-requests")
                .header("Authorization", "Bearer $buyerToken")
                .contentType(MediaType.APPLICATION_JSON)
                .content("""{"deviceCategory":"SMARTPHONE","complaint":"LCD rusak","pickupLocation":"Purwokerto","paymentMethod":"DANA"}"""),
        ).andExpect(status().isCreated)
            .andExpect(jsonPath("$.status").value("NEW"))
            .andExpect(jsonPath("$.paymentMethod").value("DANA"))
            .andReturn()
        val serviceRequestId = objectMapper.readTree(serviceRequest.response.contentAsString).get("id").asText()

        mockMvc.perform(get("/api/v1/service-requests/mine").header("Authorization", "Bearer $buyerToken"))
            .andExpect(status().isOk)
            .andExpect(jsonPath("$.content[0].id").value(serviceRequestId))
    }
}