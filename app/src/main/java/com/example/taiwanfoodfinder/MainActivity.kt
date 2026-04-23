package com.example.taiwanfoodfinder

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.lifecycle.lifecycleScope
import com.example.taiwanfoodfinder.data.api.RetrofitClient
import com.example.taiwanfoodfinder.data.api.TokenManager
import com.example.taiwanfoodfinder.data.models.AuthRequest
import com.example.taiwanfoodfinder.ui.screens.home.HomeScreen
import com.google.gson.Gson
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        TokenManager.init(applicationContext)
        // 2. Logic kiểm tra Token
        val currentToken = TokenManager.getToken()

        if (currentToken.isNullOrEmpty()) {
            // Nếu chưa có Token -> Gọi API đổi API Key lấy Token
            fetchTokenAndStartApp()
        }

        setContent {
            // Hiển thị trực tiếp HomeScreen
            HomeScreen()
        }
    }

    private fun fetchTokenAndStartApp() {
        // Mở một luồng chạy ngầm để gọi API
        var body = AuthRequest(BuildConfig.MY_API_KEY)
//        val jsonString = Gson().toJson(body)
        Log.d("API_RESPONSE", body.toString())
        lifecycleScope.launch {
            try {
                // Gọi lên server gửi theo cái MY_API_KEY bí mật
                var requestBody = AuthRequest(BuildConfig.MY_API_KEY)

                val response = RetrofitClient.apiService.auth(requestBody)
                if (response.success) {
                    // Thành công! Lưu token vào két sắt
                    TokenManager.saveToken(response.data.preAuthToken)
                    println("Lấy Token thành công: ${response.data.preAuthToken}")

                    // Lưu ý: Sau khi có token, bạn có thể trigger ViewModel load lại dữ liệu ở đây
                }
            } catch (e: Exception) {
                e.printStackTrace()
                println("Lỗi khi lấy Token: ${e.message}")
            }
        }
    }
}