package com.example.lapaksoed.data.repository

import com.example.lapaksoed.data.remote.ApiClient
import com.example.lapaksoed.data.remote.CreatePartnerApplicationRequest

class PartnerRepository {
    suspend fun apply(request: CreatePartnerApplicationRequest) = ApiClient.partnerApi.apply(request)
    suspend fun myApplication() = ApiClient.partnerApi.myApplication()
}
