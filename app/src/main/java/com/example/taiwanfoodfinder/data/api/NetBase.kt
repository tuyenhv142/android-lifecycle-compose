package com.example.taiwanfoodfinder.data.api

import com.example.taiwanfoodfinder.data.models.ApiResponse
import com.example.taiwanfoodfinder.data.models.RestaurantResponse
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import  com.example.taiwanfoodfinder.data.api.ApiService

import com.example.taiwanfoodfinder.BuildConfig
import okhttp3.Interceptor
import okhttp3.OkHttpClient

object RetrofitClient {
    // Máy ảo Android phải dùng 10.0.2.2 thay vì localhost
    private const val BASE_URL = BuildConfig.Base_URL

    private val authInterceptor = Interceptor { chain ->
        val originalRequest = chain.request()
        val requestBuilder = originalRequest.newBuilder()

        // TRƯỜNG HỢP 1: Nếu hàm API đã tự truyền sẵn một Token khác (hoặc API Key khác)
        // -> Trạm kiểm soát sẽ tôn trọng và cho qua luôn, không ghi đè.
        if (originalRequest.header("Authorization") != null || originalRequest.header("x-api-key") != null) {
            return@Interceptor chain.proceed(originalRequest)
        }

        // TRƯỜNG HỢP 2: Nếu hàm API được đánh dấu là "No-Auth" (Không cần Token)
        // -> Trạm kiểm soát gỡ cái cờ "No-Auth" ra cho sạch sẽ rồi cho đi tiếp, KHÔNG nhét Token.
        if (originalRequest.header("No-Auth") == "true") {
            requestBuilder.removeHeader("No-Auth")
            return@Interceptor chain.proceed(requestBuilder.build())
        }

        // TRƯỜNG HỢP 3: Mặc định (Các API bình thường)
        // -> Lấy Token từ két sắt và nhét vào
        val token = TokenManager.getToken()
        if (!token.isNullOrEmpty()) {
            requestBuilder.header("Authorization", "Bearer $token")
        }

        chain.proceed(requestBuilder.build())
    }

    private val okHttpClient = OkHttpClient.Builder()
        .addInterceptor(authInterceptor)
        .authenticator(TokenAuthenticator())
        .build()
    val apiService: ApiService by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create()) // Tự động parse JSON
            .build()
            .create(ApiService::class.java)
    }
}