package com.example.lapaksoed.data.remote

data class RegisterRequest(
    val email: String,
    val password: String,
    val fullName: String,
    val nim: String
)

data class LoginRequest(
    val email: String,
    val password: String
)

data class UserResponse(
    val id: String,
    val email: String,
    val fullName: String,
    val nim: String,
    val createdAt: String
)

data class AuthResponse(
    val accessToken: String,
    val tokenType: String,
    val user: UserResponse
)
