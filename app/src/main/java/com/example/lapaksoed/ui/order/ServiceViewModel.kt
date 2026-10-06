package com.example.lapaksoed.ui.order

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.lapaksoed.data.remote.CreateServiceRequest
import com.example.lapaksoed.data.remote.ServiceRequestResponse
import com.example.lapaksoed.data.repository.ServiceRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import org.json.JSONObject

sealed interface ServiceSubmissionState {
    data object Idle : ServiceSubmissionState
    data object Loading : ServiceSubmissionState
    data class Success(val request: ServiceRequestResponse) : ServiceSubmissionState
    data class Error(val message: String) : ServiceSubmissionState
}

class ServiceViewModel : ViewModel() {
    private val repository = ServiceRepository()

    private val _deviceCategory = MutableStateFlow("SMARTPHONE")
    val deviceCategory: StateFlow<String> = _deviceCategory.asStateFlow()

    private val _complaint = MutableStateFlow("")
    val complaint: StateFlow<String> = _complaint.asStateFlow()

    private val _pickupLocation = MutableStateFlow("")
    val pickupLocation: StateFlow<String> = _pickupLocation.asStateFlow()

    private val _paymentMethod = MutableStateFlow("BANK_TRANSFER")
    val paymentMethod: StateFlow<String> = _paymentMethod.asStateFlow()

    private val _submission = MutableStateFlow<ServiceSubmissionState>(ServiceSubmissionState.Idle)
    val submission: StateFlow<ServiceSubmissionState> = _submission.asStateFlow()

    fun setDeviceCategory(value: String) { _deviceCategory.value = value }
    fun setComplaint(value: String) { _complaint.value = value }
    fun setPickupLocation(value: String) { _pickupLocation.value = value }
    fun setPaymentMethod(value: String) { _paymentMethod.value = value }

    fun submit() {
        val location = _pickupLocation.value.trim()
        val complaint = _complaint.value.trim()
        if (complaint.isBlank() || location.isBlank()) {
            _submission.value = ServiceSubmissionState.Error("Lengkapi keluhan dan lokasi pengambilan terlebih dahulu.")
            return
        }

        viewModelScope.launch {
            _submission.value = ServiceSubmissionState.Loading
            try {
                val response = repository.create(
                    CreateServiceRequest(
                        deviceCategory = _deviceCategory.value,
                        complaint = complaint,
                        pickupLocation = location,
                        paymentMethod = _paymentMethod.value,
                    )
                )
                val request = response.body()
                if (response.isSuccessful && request != null) {
                    _submission.value = ServiceSubmissionState.Success(request)
                } else {
                    val message = response.errorBody()?.string()?.let { body ->
                        runCatching { JSONObject(body).optString("message") }.getOrNull()
                    }
                    _submission.value = ServiceSubmissionState.Error(
                        message?.takeIf { it.isNotBlank() }
                            ?: "Permintaan servis gagal dibuat (${response.code()})."
                    )
                }
            } catch (error: Exception) {
                _submission.value = ServiceSubmissionState.Error(
                    error.message ?: "Koneksi ke server gagal."
                )
            }
        }
    }
}
