package com.example.taiwanfoodfinder.data.models

data class RestaurantResponse(
    val id: Int,
    val placeId: String?,
    val name: String,
    val address: String?,
    val city: String,
    val lat: Double,
    val lng: Double,
    val rating: Double?,
    val category: String?,
    val imageUrl: String?
)
