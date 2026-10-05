package com.example.lapaksoed.ui.order

import androidx.lifecycle.ViewModel
import com.example.lapaksoed.data.remote.ListingResponse
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class OrderViewModel : ViewModel() {
    private val _selectedListing = MutableStateFlow<ListingResponse?>(null)
    val selectedListing: StateFlow<ListingResponse?> = _selectedListing.asStateFlow()

    private val _quantity = MutableStateFlow(1)
    val quantity: StateFlow<Int> = _quantity.asStateFlow()

    private val _deviceCategory = MutableStateFlow("")
    val deviceCategory: StateFlow<String> = _deviceCategory.asStateFlow()

    private val _complaint = MutableStateFlow("")
    val complaint: StateFlow<String> = _complaint.asStateFlow()

    private val _pickupLocation = MutableStateFlow("")
    val pickupLocation: StateFlow<String> = _pickupLocation.asStateFlow()

    private val _paymentMethod = MutableStateFlow("")
    val paymentMethod: StateFlow<String> = _paymentMethod.asStateFlow()

    fun setListing(listing: ListingResponse) {
        _selectedListing.value = listing
        _quantity.value = 1
        _deviceCategory.value = ""
        _complaint.value = ""
        _pickupLocation.value = ""
        _paymentMethod.value = ""
    }

    fun updateQuantity(newQuantity: Int) {
        if (newQuantity >= 1) {
            _quantity.value = newQuantity
        }
    }

    fun setDeviceCategory(category: String) {
        _deviceCategory.value = category
    }

    fun setComplaint(complaint: String) {
        _complaint.value = complaint
    }

    fun setPickupLocation(location: String) {
        _pickupLocation.value = location
    }

    fun setPaymentMethod(method: String) {
        _paymentMethod.value = method
    }
}
