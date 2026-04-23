package com.example.taiwanfoodfinder.ui.screens.home

import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.taiwanfoodfinder.data.models.RestaurantResponse
import com.example.taiwanfoodfinder.data.repository.RestaurantRepository
import kotlinx.coroutines.launch

class HomeViewModel(
    private val repository: RestaurantRepository = RestaurantRepository()
) : ViewModel() {

    val restaurants = mutableStateListOf<RestaurantResponse>()
    var isLoading = mutableStateOf(true)
    var errorMessage = mutableStateOf<String?>(null)

    init {
        fetchData()
    }

    private fun fetchData() {
        viewModelScope.launch {
            isLoading.value = true

            // ViewModel chỉ việc gọi Repository
            val result = repository.getRestaurants()

            result.onSuccess { data ->
                restaurants.clear()
                restaurants.addAll(data)
                errorMessage.value = null
            }.onFailure { error ->
                errorMessage.value = "Lỗi kết nối: ${error.localizedMessage}"
            }

            isLoading.value = false
        }
    }
}