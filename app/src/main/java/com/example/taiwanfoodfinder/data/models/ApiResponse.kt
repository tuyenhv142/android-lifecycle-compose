package com.example.taiwanfoodfinder.data.models

data class ApiResponse<T> (
    val success: Boolean,
    val data : T
)
