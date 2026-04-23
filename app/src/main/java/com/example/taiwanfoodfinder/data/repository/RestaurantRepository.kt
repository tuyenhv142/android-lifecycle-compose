package com.example.taiwanfoodfinder.data.repository

import com.example.taiwanfoodfinder.data.api.RetrofitClient
import com.example.taiwanfoodfinder.data.models.RestaurantResponse

class RestaurantRepository {
    // Trả về Result của Kotlin để dễ dàng bắt lỗi Success/Failure
    suspend fun getRestaurants(): Result<List<RestaurantResponse>> {
        return try {
            val response = RetrofitClient.apiService.getAllRestaurants()
            if (response.success) {
                Result.success(response.data)
            } else {
                Result.failure(Exception("API trả về lỗi"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}