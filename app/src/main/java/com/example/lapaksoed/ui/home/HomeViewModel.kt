package com.example.lapaksoed.ui.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.lapaksoed.data.remote.ListingResponse
import com.example.lapaksoed.data.remote.PromotionResponse
import com.example.lapaksoed.data.repository.ListingRepository
import com.example.lapaksoed.data.repository.PromotionRepository
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

sealed class HomeState {
    object Loading : HomeState()
    data class Success(val products: List<ListingResponse>) : HomeState()
    data class Error(val message: String) : HomeState()
}

class HomeViewModel : ViewModel() {
    private val repository = ListingRepository()
    private val promotionRepository = PromotionRepository()

    private val _homeState = MutableStateFlow<HomeState>(HomeState.Loading)
    val homeState: StateFlow<HomeState> = _homeState.asStateFlow()

    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery.asStateFlow()

    private val _recommendations = MutableStateFlow<List<String>>(emptyList())
    val recommendations: StateFlow<List<String>> = _recommendations.asStateFlow()
    
    private val _isSearchActive = MutableStateFlow(false)
    val isSearchActive: StateFlow<Boolean> = _isSearchActive.asStateFlow()

    private val _selectedCategory = MutableStateFlow<String?>(null)
    val selectedCategory: StateFlow<String?> = _selectedCategory.asStateFlow()

    private val _promotions = MutableStateFlow<List<PromotionResponse>>(emptyList())
    val promotions: StateFlow<List<PromotionResponse>> = _promotions.asStateFlow()

    private var searchJob: Job? = null
    
    init {
        loadProducts()
        loadPromotions()
    }

    fun loadPromotions() {
        viewModelScope.launch {
            runCatching { promotionRepository.getPromotions() }
                .getOrNull()
                ?.takeIf { it.isSuccessful }
                ?.body()
                ?.let { _promotions.value = it }
        }
    }

    fun loadProducts(query: String? = null, category: String? = null) {
        viewModelScope.launch {
            _homeState.value = HomeState.Loading
            try {
                val response = repository.getListings(query = query, category = category)
                if (response.isSuccessful && response.body() != null) {
                    _homeState.value = HomeState.Success(response.body()!!.content)
                } else {
                    _homeState.value = HomeState.Error("Gagal memuat produk")
                }
            } catch (e: Exception) {
                _homeState.value = HomeState.Error(e.message ?: "Terjadi kesalahan")
            }
        }
    }

    fun updateSearchQuery(query: String) {
        _searchQuery.value = query
        searchJob?.cancel()
        
        if (query.isBlank()) {
            _recommendations.value = emptyList()
            return
        }

        searchJob = viewModelScope.launch {
            delay(300) // debounce
            try {
                // Fetch products for recommendation matching the query
                val response = repository.getListings(query = query, size = 10)
                if (response.isSuccessful && response.body() != null) {
                    val titles = response.body()!!.content.map { it.title }.distinct().take(5)
                    _recommendations.value = titles
                }
            } catch (e: Exception) {
                // Ignore error for recommendation
            }
        }
    }
    
    fun setSearchActive(isActive: Boolean) {
        _isSearchActive.value = isActive
        if (!isActive) {
            _recommendations.value = emptyList()
        }
    }

    fun performSearch(query: String) {
        _searchQuery.value = query
        _recommendations.value = emptyList()
        setSearchActive(false)
        loadProducts(
            query = if (query.isBlank()) null else query,
            category = _selectedCategory.value
        )
    }

    fun filterByCategory(category: String) {
        if (category == "Semua") {
            _selectedCategory.value = null
            loadProducts(
                query = if (_searchQuery.value.isBlank()) null else _searchQuery.value,
                category = null
            )
            return
        }
        if (_selectedCategory.value == category) {
            _selectedCategory.value = null
            loadProducts(
                query = if (_searchQuery.value.isBlank()) null else _searchQuery.value,
                category = null
            )
        } else {
            _selectedCategory.value = category
            loadProducts(
                query = if (_searchQuery.value.isBlank()) null else _searchQuery.value,
                category = category
            )
        }
    }
}
