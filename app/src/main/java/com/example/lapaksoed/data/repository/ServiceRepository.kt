package com.example.lapaksoed.data.repository

import com.example.lapaksoed.data.remote.ApiClient
import com.example.lapaksoed.data.remote.CreateServiceRequest

class ServiceRepository {
    suspend fun create(request: CreateServiceRequest) = ApiClient.serviceApi.create(request)
}
