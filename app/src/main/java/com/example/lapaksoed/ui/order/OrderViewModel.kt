package com.example.lapaksoed.ui.order

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.lapaksoed.data.remote.CreateOrderRequest
import com.example.lapaksoed.data.remote.ListingResponse
import com.example.lapaksoed.data.remote.OrderResponse
import com.example.lapaksoed.data.repository.OrderRepository
import kotlinx.coroutines.launch
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import org.json.JSONObject

class OrderViewModel : ViewModel() {
    private val orderRepository = OrderRepository()

    private val _selectedListing = MutableStateFlow<ListingResponse?>(null)
    val selectedListing: StateFlow<ListingResponse?> = _selectedListing.asStateFlow()

    private val _quantity = MutableStateFlow(1)
    val quantity: StateFlow<Int> = _quantity.asStateFlow()

    private val _paymentMethod = MutableStateFlow("")
    val paymentMethod: StateFlow<String> = _paymentMethod.asStateFlow()

    private val _submissionState = MutableStateFlow<OrderSubmissionState>(OrderSubmissionState.Idle)
    val submissionState: StateFlow<OrderSubmissionState> = _submissionState.asStateFlow()

    fun setListing(listing: ListingResponse) {
        _selectedListing.value = listing
        _quantity.value = 1
        _paymentMethod.value = ""
        _submissionState.value = OrderSubmissionState.Idle
    }

    fun updateQuantity(newQuantity: Int) {
        if (newQuantity == 1) {
            _quantity.value = newQuantity
        }
    }

    fun setPaymentMethod(method: String) {
        _paymentMethod.value = method
    }

    fun placeOrder() {
        val listing = _selectedListing.value
        val selectedMethod = _paymentMethod.value
        if (listing == null || selectedMethod.isBlank()) {
            _submissionState.value = OrderSubmissionState.Error("Pilih metode pembayaran terlebih dahulu.")
            return
        }

        val apiMethod = when (selectedMethod) {
            "Transfer Bank (BNI)" -> "BANK_TRANSFER"
            "Dana" -> "DANA"
            "Gopay" -> "GOPAY"
            "QRis" -> "QRIS"
            "Cash on Delivery" -> "CASH_ON_DELIVERY"
            else -> selectedMethod
        }

        viewModelScope.launch {
            _submissionState.value = OrderSubmissionState.Loading
            try {
                val response = orderRepository.createOrder(
                    CreateOrderRequest(listingId = java.util.UUID.fromString(listing.id), quantity = 1, paymentMethod = apiMethod)
                )
                val order = response.body()
                if (response.isSuccessful && order != null) {
                    _submissionState.value = OrderSubmissionState.Success(order)
                } else {
                    val responseMessage = response.errorBody()?.string()?.let { body ->
                        runCatching { JSONObject(body).optString("message") }.getOrNull()
                            ?.takeIf(String::isNotBlank)
                    }
                    _submissionState.value = OrderSubmissionState.Error(
                        responseMessage ?: when (response.code()) {
                            401 -> "Sesi login sudah berakhir. Silakan masuk kembali."
                            404 -> "Barang tidak ditemukan. Muat ulang katalog dan coba lagi."
                            409 -> "Barang ini sudah dipesan orang lain. Pilih barang lain."
                            else -> "Pesanan gagal dibuat (${response.code()}). Coba lagi."
                        }
                    )
                }
            } catch (error: Exception) {
                _submissionState.value = OrderSubmissionState.Error(error.message ?: "Koneksi ke server gagal.")
            }
        }
    }
}

sealed class OrderSubmissionState {
    data object Idle : OrderSubmissionState()
    data object Loading : OrderSubmissionState()
    data class Success(val order: OrderResponse) : OrderSubmissionState()
    data class Error(val message: String) : OrderSubmissionState()
}
