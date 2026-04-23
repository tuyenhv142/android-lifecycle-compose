package com.example.taiwanfoodfinder.data.models


class AuthRequest(val ApiKey: String){
    override fun toString(): String {
        return "AuthRequest(ApiKey='$ApiKey')"
    }
}