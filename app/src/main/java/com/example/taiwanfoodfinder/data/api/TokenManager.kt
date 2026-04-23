package com.example.taiwanfoodfinder.data.api

import android.content.Context
import android.content.SharedPreferences

object TokenManager {
    private const val PREF_NAME = "AuthPrefs"
    private const val KEY_AUTH_TOKEN = "jwt_auth_token"
    private const val KEY_USER_TOKEN = "jwt_user_token"
    private var prefs: SharedPreferences? = null

    // Hàm này sẽ được gọi khi App vừa bật lên
    fun init(context: Context) {
        prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
    }

    // Lưu Token sau khi đăng nhập thành công
    fun saveToken(token: String) {
        prefs?.edit()?.putString(KEY_AUTH_TOKEN, token)?.apply()
    }

    // Lấy Token ra để nhét vào API
    fun getToken(): String? {
        return prefs?.getString(KEY_AUTH_TOKEN, null)
    }

    // Xóa Token khi người dùng Đăng xuất (Logout)
    fun clearToken() {
        prefs?.edit()?.remove(KEY_AUTH_TOKEN)?.apply()
    }

    //Save User

    fun saveUserToken(token: String) {
        prefs?.edit()?.putString(KEY_USER_TOKEN, token)?.apply()
    }

    fun getUserToken(): String? {
        return prefs?.getString(KEY_USER_TOKEN, null)
    }

    fun clearUserToken() {
        prefs?.edit()?.remove(KEY_USER_TOKEN)?.apply()

    }
}