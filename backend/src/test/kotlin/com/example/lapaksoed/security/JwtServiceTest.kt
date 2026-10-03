package com.example.lapaksoed.security

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNull
import org.junit.jupiter.api.Test
import java.util.UUID

class JwtServiceTest {
    private val jwtService = JwtService("test-secret-that-is-long-enough-for-hmac-sha-256", 15)

    @Test
    fun `token round trips user id`() {
        val userId = UUID.randomUUID()

        assertEquals(userId, jwtService.readUserId(jwtService.createToken(userId)))
    }

    @Test
    fun `invalid token is rejected`() {
        assertNull(jwtService.readUserId("not-a-jwt"))
    }
}