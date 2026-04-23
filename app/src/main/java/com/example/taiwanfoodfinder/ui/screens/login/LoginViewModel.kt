package com.example.taiwanfoodfinder.ui.screens.login

import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.taiwanfoodfinder.data.api.RetrofitClient
import com.example.taiwanfoodfinder.data.api.TokenManager
import com.example.taiwanfoodfinder.data.models.LoginRequest
import kotlinx.coroutines.launch

class LoginViewModel : ViewModel(){
    var username = mutableStateOf("")
    var password = mutableStateOf("")

    var isloading = mutableStateOf(false)
    var errorMess = mutableStateOf<String?>(null)

    fun login(onSucess: () -> Unit){
        var currentUsername  = username.value.trim()
        var currentPassword = password.value.trim()

        if (currentUsername.isEmpty() || currentPassword.isEmpty()){
            errorMess.value = "Please enter both username and password"
            return
        }

        viewModelScope.launch {
            isloading.value = true
            errorMess.value = null

            try {
                val request = LoginRequest(currentUsername, currentPassword)

                val response = RetrofitClient.apiService.login(request)

                if (response.success){
                    TokenManager.saveUserToken(response.data.accessToken)
                    Log.d("API_RESPONSE", response.data.accessToken)
                    onSucess()
                }else{
                    errorMess.value = "Username or password is incorrect"
                }
            }catch (e: Exception){
                e.printStackTrace()
                errorMess.value = "Login failed"
            }finally {
                isloading.value = false

            }
        }
    }
}