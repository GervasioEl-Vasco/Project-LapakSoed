package com.example.lapaksoed.data.remote

import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object ApiClient {
    // For a physical USB-connected phone, run: adb reverse tcp:8080 tcp:8080
    private const val BASE_URL = "http://127.0.0.1:8080/api/v1/"
    
    var authToken: String? = null
    var currentUserId: String? = null

    private val loggingInterceptor = HttpLoggingInterceptor().apply {
        level = HttpLoggingInterceptor.Level.BODY
    }

    private val authInterceptor = okhttp3.Interceptor { chain ->
        val requestBuilder = chain.request().newBuilder()
        authToken?.let {
            requestBuilder.addHeader("Authorization", "Bearer $it")
        }
        chain.proceed(requestBuilder.build())
    }

    private val okHttpClient = OkHttpClient.Builder()
        .addInterceptor(loggingInterceptor)
        .addInterceptor(authInterceptor)
        .build()

    val retrofit: Retrofit = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .client(okHttpClient)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    val authApi: AuthApi = retrofit.create(AuthApi::class.java)
    val listingApi: ListingApi = retrofit.create(ListingApi::class.java)
    val chatApi: ChatApi = retrofit.create(ChatApi::class.java)
    val orderApi: OrderApi = retrofit.create(OrderApi::class.java)
    val promotionApi: PromotionApi = retrofit.create(PromotionApi::class.java)
    val partnerApi: PartnerApi = retrofit.create(PartnerApi::class.java)
}
