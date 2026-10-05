package com.example.lapaksoed.ui.order

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.lapaksoed.data.remote.OrderResponse
import com.example.lapaksoed.data.repository.OrderRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

sealed class OrdersState {
    data object Loading : OrdersState()
    data class Success(val orders: List<OrderResponse>) : OrdersState()
    data class Error(val message: String) : OrdersState()
}

class OrdersViewModel : ViewModel() {
    private val repository = OrderRepository()
    private val _state = MutableStateFlow<OrdersState>(OrdersState.Loading)
    val state: StateFlow<OrdersState> = _state.asStateFlow()

    fun loadOrders() {
        viewModelScope.launch {
            _state.value = OrdersState.Loading
            try {
                val response = repository.getMyOrders()
                if (response.isSuccessful) {
                    _state.value = OrdersState.Success(response.body()?.content.orEmpty())
                } else {
                    _state.value = OrdersState.Error("Tidak dapat memuat pesanan (${response.code()})")
                }
            } catch (error: Exception) {
                _state.value = OrdersState.Error(error.message ?: "Koneksi ke server gagal")
            }
        }
    }
}
