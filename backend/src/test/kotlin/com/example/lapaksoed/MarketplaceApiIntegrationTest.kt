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
    }
}