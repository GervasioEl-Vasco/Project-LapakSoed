package com.example.lapaksoed.ui.profile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.lapaksoed.data.remote.ApiClient
import com.example.lapaksoed.data.remote.UserResponse
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class ProfileViewModel : ViewModel() {
    private val authApi = ApiClient.authApi

    private val _userProfile = MutableStateFlow<UserResponse?>(null)
    val userProfile: StateFlow<UserResponse?> = _userProfile.asStateFlow()

    fun loadProfile() {
        viewModelScope.launch {
            try {
                val response = authApi.me()
                if (response.isSuccessful) {
                    _userProfile.value = response.body()
                }
            } catch (e: Exception) {
                // Handle error or ignore
            }
        }
    }

    fun logout() {
        ApiClient.authToken = null
        ApiClient.currentUserId = null
        _userProfile.value = null
    }
}
