package com.example.taiwanfoodfinder.data.api

import com.example.taiwanfoodfinder.BuildConfig
import com.example.taiwanfoodfinder.data.models.AuthRequest
import kotlinx.coroutines.runBlocking
import okhttp3.Authenticator
import okhttp3.Request
import okhttp3.Response
import okhttp3.Route
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class TokenAuthenticator : Authenticator {

    // Tạo một Retrofit "sạch" không bị dính Interceptor/Authenticator
    // Để tránh lỗi vòng lặp vô tận (Circular Dependency)
    private val refreshApiService: ApiService by lazy {
        Retrofit.Builder()
            .baseUrl(BuildConfig.Base_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ApiService::class.java)
    }

    override fun authenticate(route: Route?, response: Response): Request? {
        // 1. Chỉ chạy khi mã lỗi là 401 (Hết hạn Token)
        if (response.code() != 401) return null

        // 2. Chống lặp vô tận: Nếu thử xin lại mà vẫn 401 thì bỏ cuộc
        if (retryCount(response) >= 2) {
            TokenManager.clearToken()
            // TODO: Đá người dùng văng ra màn hình Đăng nhập
            return null
        }

        // 3. Khóa luồng để tránh gọi API xin token nhiều lần cùng lúc
        synchronized(this) {
            val currentToken = TokenManager.getToken()
            val requestToken = response.request().header("Authorization")?.removePrefix("Bearer ")

            // Nếu token đã được làm mới bởi 1 request khác thì lấy xài luôn
            if (currentToken != null && currentToken != requestToken) {
                return response.request().newBuilder()
                    .header("Authorization", "Bearer $currentToken")
                    .build()
            }

            // 4. Bắt đầu đi xin Token mới
            val newToken = fetchNewToken()

            return if (newToken != null) {
                TokenManager.saveToken(newToken)
                // Lấy Request cũ bị lỗi, thay Token mới và gửi đi lại
                response.request().newBuilder()
                    .header("Authorization", "Bearer $newToken")
                    .build()
            } else {
                TokenManager.clearToken()
                null
            }
        }
    }

    // Hàm gọi API đồng bộ để lấy Token
    private fun fetchNewToken(): String? {
        return try {
            runBlocking {
                val requestBody = AuthRequest(BuildConfig.MY_API_KEY)
                // Dùng cái apiService "sạch" vừa tạo ở trên để gọi
                val apiResponse = refreshApiService.auth(requestBody)

                if (apiResponse.success) {
                    apiResponse.data.preAuthToken
                } else {
                    null
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }

    // Đếm số lần đã thử lại
    private fun retryCount(response: Response): Int {
        var count = 1
        var priorResponse = response.priorResponse()
        while (priorResponse != null) {
            count++
            priorResponse = priorResponse.priorResponse()
        }
        return count
    }
}