package com.example.lapaksoed.data.repository

import com.example.lapaksoed.data.remote.ApiClient
import com.example.lapaksoed.data.remote.LoginRequest
import com.example.lapaksoed.data.remote.RegisterRequest

class AuthRepository {
    private val authApi = ApiClient.authApi

    suspend fun login(request: LoginRequest) = authApi.login(request)
    suspend fun register(request: RegisterRequest) = authApi.register(request)
}
