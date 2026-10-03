package com.example.lapaksoed.security

import io.jsonwebtoken.Jwts
import io.jsonwebtoken.security.Keys
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service
import java.nio.charset.StandardCharsets
import java.time.Instant
import java.util.Date
import java.util.UUID

@Service
class JwtService(
    @Value("\${app.jwt.secret}") secret: String,
    @param:Value("\${app.jwt.expiration-minutes}") private val expirationMinutes: Long,
) {
    private val key = Keys.hmacShaKeyFor(secret.toByteArray(StandardCharsets.UTF_8))

    fun createToken(userId: UUID): String {
        val now = Instant.now()
        return Jwts.builder()
            .subject(userId.toString())
            .issuedAt(Date.from(now))
            .expiration(Date.from(now.plusSeconds(expirationMinutes * 60)))
            .signWith(key)
            .compact()
    }

    fun readUserId(token: String): UUID? = try {
        UUID.fromString(Jwts.parser().verifyWith(key).build().parseSignedClaims(token).payload.subject)
    } catch (_: Exception) {
        null
    }
}