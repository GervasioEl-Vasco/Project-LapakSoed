package com.example.lapaksoed.ui.auth

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.lapaksoed.data.remote.LoginRequest
import com.example.lapaksoed.data.remote.RegisterRequest
import com.example.lapaksoed.data.repository.AuthRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

sealed class AuthState {
    object Idle : AuthState()
    object Loading : AuthState()
    data class Success(val message: String) : AuthState()
    data class Error(val error: String) : AuthState()
}

class AuthViewModel : ViewModel() {
    private val repository = AuthRepository()

    private val _authState = MutableStateFlow<AuthState>(AuthState.Idle)
    val authState: StateFlow<AuthState> = _authState.asStateFlow()

    fun login(request: LoginRequest) {
        if (request.email.isBlank() || request.password.isBlank()) {
            _authState.value = AuthState.Error("Email and Password cannot be empty")
            return
        }
        viewModelScope.launch {
            _authState.value = AuthState.Loading
            try {
                val response = repository.login(request)
                if (response.isSuccessful && response.body() != null) {
                    val authData = response.body()!!
                    com.example.lapaksoed.data.remote.ApiClient.authToken = authData.accessToken
                    com.example.lapaksoed.data.remote.ApiClient.currentUserId = authData.user.id
                    _authState.value = AuthState.Success("Login Successful")
                } else {
                    _authState.value = AuthState.Error("Login failed: ${response.code()}")
                }
            } catch (e: Exception) {
                _authState.value = AuthState.Error(e.message ?: "Network error")
            }
        }
    }

    fun register(request: RegisterRequest, confirmPass: String) {
        if (request.email.isBlank() || request.password.isBlank() || request.fullName.isBlank() || request.nim.isBlank()) {
            _authState.value = AuthState.Error("All fields are required")
            return
        }
        if (request.password != confirmPass) {
            _authState.value = AuthState.Error("Passwords do not match")
            return
        }
        
        viewModelScope.launch {
            _authState.value = AuthState.Loading
            try {
                val response = repository.register(request)
                if (response.isSuccessful && response.body() != null) {
                    val authData = response.body()!!
                    com.example.lapaksoed.data.remote.ApiClient.authToken = authData.accessToken
                    com.example.lapaksoed.data.remote.ApiClient.currentUserId = authData.user.id
                    _authState.value = AuthState.Success("Registration Successful")
                } else {
                    _authState.value = AuthState.Error("Registration failed: ${response.code()}")
                }
            } catch (e: Exception) {
                _authState.value = AuthState.Error(e.message ?: "Network error")
            }
        }
    }
    
    fun resetState() {
        _authState.value = AuthState.Idle
    }
}
