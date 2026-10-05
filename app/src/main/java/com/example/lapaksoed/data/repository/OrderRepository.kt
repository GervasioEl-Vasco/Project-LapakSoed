package com.example.lapaksoed.data.repository

import com.example.lapaksoed.data.remote.ApiClient
import com.example.lapaksoed.data.remote.CreateOrderRequest

class OrderRepository {
    suspend fun createOrder(request: CreateOrderRequest) = ApiClient.orderApi.createOrder(request)
    suspend fun getMyOrders() = ApiClient.orderApi.getMyOrders()
}
