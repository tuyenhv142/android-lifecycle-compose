package com.example.taiwanfoodfinder.data.api

import android.content.Context
import android.content.SharedPreferences

object TokenManager {
    private const val PREF_NAME = "AuthPrefs"
    private const val KEY_TOKEN = "jwt_token"
    private var prefs: SharedPreferences? = null

    // Hàm này sẽ được gọi khi App vừa bật lên
    fun init(context: Context) {
        prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
    }

    // Lưu Token sau khi đăng nhập thành công
    fun saveToken(token: String) {
        prefs?.edit()?.putString(KEY_TOKEN, token)?.apply()
    }

    // Lấy Token ra để nhét vào API
    fun getToken(): String? {
        return prefs?.getString(KEY_TOKEN, null)
    }

    // Xóa Token khi người dùng Đăng xuất (Logout)
    fun clearToken() {
        prefs?.edit()?.remove(KEY_TOKEN)?.apply()
    }
}