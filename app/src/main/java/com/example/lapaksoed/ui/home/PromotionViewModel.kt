package com.example.lapaksoed.ui.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.lapaksoed.data.remote.PromotionResponse
import com.example.lapaksoed.data.repository.PromotionRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

sealed class PromotionDetailState {
    data object Loading : PromotionDetailState()
    data class Success(val promotion: PromotionResponse) : PromotionDetailState()
    data object NotFound : PromotionDetailState()
    data class Error(val message: String) : PromotionDetailState()
}

class PromotionViewModel : ViewModel() {
    private val repository = PromotionRepository()
    private val _state = MutableStateFlow<PromotionDetailState>(PromotionDetailState.Loading)
    val state: StateFlow<PromotionDetailState> = _state.asStateFlow()

    fun load(promotionId: String) {
        viewModelScope.launch {
            _state.value = PromotionDetailState.Loading
            try {
                val response = repository.getPromotions()
                if (!response.isSuccessful) {
                    _state.value = PromotionDetailState.Error("Promosi tidak dapat dimuat (${response.code()}).")
                    return@launch
                }
                val promotion = response.body().orEmpty().firstOrNull { it.id.toString() == promotionId }
                _state.value = promotion?.let(PromotionDetailState::Success) ?: PromotionDetailState.NotFound
            } catch (error: Exception) {
                _state.value = PromotionDetailState.Error(error.message ?: "Koneksi ke server gagal.")
            }
        }
    }
}
