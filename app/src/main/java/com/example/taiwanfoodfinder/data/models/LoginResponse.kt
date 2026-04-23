package com.example.taiwanfoodfinder.data.models

data class LoginResponse(
    var accessToken: String,
    var user : User
)

data class User(
    var id: Int,
    var name: String,
    var imageUrl: String,
)
