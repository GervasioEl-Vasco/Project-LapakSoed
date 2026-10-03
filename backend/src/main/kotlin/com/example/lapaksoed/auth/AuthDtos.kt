package com.example.lapaksoed.auth

import jakarta.validation.constraints.Email
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.Size
import java.time.Instant
import java.util.UUID

data class RegisterRequest(
    @field:Email
    @field:NotBlank
    @field:Size(max = 254)
    val email: String,
    @field:NotBlank
    @field:Size(min = 8, max = 72)
    val password: String,
    @field:NotBlank
    @field:Size(max = 100)
    val fullName: String,
    @field:NotBlank
    @field:Size(max = 30)
    val nim: String,
)

data class LoginRequest(
    @field:Email
    @field:NotBlank
    val email: String,
    @field:NotBlank
    val password: String,
)

data class UserResponse(
    val id: UUID,
    val email: String,
    val fullName: String,
    val nim: String,
    val createdAt: Instant,
)

data class AuthResponse(val accessToken: String, val tokenType: String = "Bearer", val user: UserResponse)

fun User.toResponse() = UserResponse(
    id = requireNotNull(id),
    email = email,
    fullName = fullName,
    nim = nim,
    createdAt = createdAt,
)