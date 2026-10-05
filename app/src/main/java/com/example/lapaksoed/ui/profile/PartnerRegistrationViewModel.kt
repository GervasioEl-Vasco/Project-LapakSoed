package com.example.lapaksoed.ui.profile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.lapaksoed.data.remote.CreatePartnerApplicationRequest
import com.example.lapaksoed.data.remote.PartnerApplicationResponse
import com.example.lapaksoed.data.repository.PartnerRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import org.json.JSONObject

sealed class PartnerRegistrationState {
    data object Loading : PartnerRegistrationState()
    data object Ready : PartnerRegistrationState()
    data class Existing(val application: PartnerApplicationResponse) : PartnerRegistrationState()
    data object Submitting : PartnerRegistrationState()
    data class Submitted(val application: PartnerApplicationResponse) : PartnerRegistrationState()
    data class Error(val message: String) : PartnerRegistrationState()
}

class PartnerRegistrationViewModel : ViewModel() {
    private val repository = PartnerRepository()
    private val _state = MutableStateFlow<PartnerRegistrationState>(PartnerRegistrationState.Loading)
    val state: StateFlow<PartnerRegistrationState> = _state.asStateFlow()

    init { loadMyApplication() }

    fun loadMyApplication() {
        viewModelScope.launch {
            _state.value = PartnerRegistrationState.Loading
            try {
                val response = repository.myApplication()
                if (response.isSuccessful) {
                    val application = response.body()
                    _state.value = application?.let(PartnerRegistrationState::Existing) ?: PartnerRegistrationState.Ready
                } else {
                    _state.value = PartnerRegistrationState.Error("Tidak dapat memeriksa pengajuan (${response.code()}).")
                }
            } catch (error: Exception) {
                _state.value = PartnerRegistrationState.Error(error.message ?: "Koneksi ke server gagal.")
            }
        }
    }

    fun submit(businessName: String, contactPhone: String, description: String) {
        if (businessName.isBlank() || contactPhone.isBlank() || description.isBlank()) {
            _state.value = PartnerRegistrationState.Error("Semua kolom wajib diisi.")
            return
        }
        if (contactPhone.count(Char::isDigit) < 8) {
            _state.value = PartnerRegistrationState.Error("Nomor telepon minimal 8 digit.")
            return
        }
        viewModelScope.launch {
            _state.value = PartnerRegistrationState.Submitting
            try {
                val response = repository.apply(
                    CreatePartnerApplicationRequest(businessName.trim(), contactPhone.trim(), description.trim())
                )
                val application = response.body()
                if (response.isSuccessful && application != null) {
                    _state.value = PartnerRegistrationState.Submitted(application)
                } else {
                    val serverMessage = response.errorBody()?.string()?.let { body ->
                        runCatching { JSONObject(body).optString("message") }.getOrNull()?.takeIf(String::isNotBlank)
                    }
                    _state.value = PartnerRegistrationState.Error(
                        serverMessage ?: if (response.code() == 409) "Pengajuan mitra Anda sudah terdaftar." else "Pengajuan gagal (${response.code()})."
                    )
                }
            } catch (error: Exception) {
                _state.value = PartnerRegistrationState.Error(error.message ?: "Koneksi ke server gagal.")
            }
        }
    }
}
