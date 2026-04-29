package com.example.taiwanfoodfinder.ui.screens

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import com.example.taiwanfoodfinder.ui.screens.home.HomeScreen
import com.example.taiwanfoodfinder.ui.screens.map.MapScreen
import com.example.taiwanfoodfinder.ui.screens.profile.ProfileScreen

// 1. Định nghĩa các Tab ở dưới đáy
sealed class BottomNavItem(val title: String, val icon: ImageVector) {
    object Home : BottomNavItem("Explore", Icons.Filled.Home)
    object Map : BottomNavItem("Map", Icons.Filled.Search)
    object Profile : BottomNavItem("Profile", Icons.Filled.Person)
}

@Composable
fun MainScreen(onLogout: () -> Unit) {
    // Lưu trạng thái xem Tab nào đang được chọn (Mặc định là Khám phá)
    var selectedItem by remember { mutableStateOf<BottomNavItem>(BottomNavItem.Home) }
    val items = listOf(BottomNavItem.Home, BottomNavItem.Map, BottomNavItem.Profile)

    // Scaffold là khung sườn chuẩn của Material Design
    Scaffold(
        bottomBar = {
            NavigationBar {
                items.forEach { item ->
                    NavigationBarItem(
                        icon = { Icon(item.icon, contentDescription = item.title) },
                        label = { Text(item.title) },
                        selected = selectedItem == item,
                        onClick = { selectedItem = item }
                    )
                }
            }
        }
    ) { innerPadding ->
        // innerPadding rất quan trọng: Nó là khoảng không gian mà Scaffold chừa ra
        // để nội dung của bạn không bị thanh điều hướng đè lên.

        Box(modifier = Modifier.padding(innerPadding).fillMaxSize()) {
            // Hiển thị nội dung dựa trên Tab được chọn
            when (selectedItem) {
                BottomNavItem.Home -> {
                    // Gọi HomeScreen của bạn ra đây
                    HomeScreen()
                }
                BottomNavItem.Map -> {
                    MapScreen()
                }
                BottomNavItem.Profile -> {
                    // Màn hình Cá nhân (Tạm thời để chữ)
//                    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
//                        Text("Cá nhân (Đang phát triển)", style = MaterialTheme.typography.titleLarge)
//                    }
                    ProfileScreen(onLogout = onLogout)
                }
            }
        }
    }
}