package com.example.taiwanfoodfinder.ui.screens.map

import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.taiwanfoodfinder.data.models.RestaurantResponse
import com.example.taiwanfoodfinder.data.repository.RestaurantRepository
import kotlinx.coroutines.launch

class MapViewModel(
    private val repository: RestaurantRepository = RestaurantRepository()
) : ViewModel() {

    val restaurants = mutableStateListOf<RestaurantResponse>()
    var isLoading = mutableStateOf(true)
    var errorMessage = mutableStateOf<String?>(null)
    var searchQuery = mutableStateOf("")
    var selectedRestaurant = mutableStateOf<RestaurantResponse?>(null)

    val filteredRestaurants: List<RestaurantResponse>
        get() {
            val query = searchQuery.value.trim().lowercase()
            if (query.isEmpty()) return restaurants
            return restaurants.filter {
                it.name.lowercase().contains(query) ||
                        it.city.lowercase().contains(query) ||
                        (it.category?.lowercase()?.contains(query) == true) ||
                        (it.address?.lowercase()?.contains(query) == true)
            }
        }

    init {
        fetchData()
    }

    fun fetchData() {
        viewModelScope.launch {
            isLoading.value = true
            errorMessage.value = null
            val result = repository.getRestaurants()
            result.onSuccess { data ->
                restaurants.clear()
                restaurants.addAll(data)
            }.onFailure { error ->
                errorMessage.value = "Lỗi kết nối: ${error.localizedMessage}"
            }
            isLoading.value = false
        }
    }
}
