package com.example.taiwanfoodfinder.data.api

import com.example.taiwanfoodfinder.data.models.ApiResponse
import com.example.taiwanfoodfinder.data.models.AuthRequest
import com.example.taiwanfoodfinder.data.models.AuthResponse
import com.example.taiwanfoodfinder.data.models.RestaurantResponse
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.POST

interface ApiService {

    @Headers("No-Auth: true")
    @POST("api/auth/token")
    suspend fun auth(@Body authRequest: AuthRequest): ApiResponse<AuthResponse>


    @GET("api/restaurants")
    suspend fun getAllRestaurants(): ApiResponse<List<RestaurantResponse>>
}