package com.example.taiwanfoodfinder.ui.screens.home

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
// Import 1 thư viện duy nhất của cách mới
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(viewModel: HomeViewModel = viewModel()) {
    val isLoading = viewModel.isLoading.value
    val errorMessage = viewModel.errorMessage.value
    val restaurants = viewModel.restaurants

    Column(modifier = Modifier.fillMaxSize().padding(horizontal = 16.dp)) {
        Spacer(modifier = Modifier.height(16.dp))
        Text(text = "Explore Taiwan Food", style = MaterialTheme.typography.headlineMedium)
        Spacer(modifier = Modifier.height(16.dp))

        if (errorMessage != null && restaurants.isEmpty()) {
            // NẾU LỖI VÀ CHƯA CÓ DATA: Hiện thông báo và nút thử lại
            Column(
                modifier = Modifier.fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Text(text = errorMessage, color = MaterialTheme.colorScheme.error)
                Spacer(modifier = Modifier.height(16.dp))
                Button(onClick = { viewModel.fetchData() }) {
                    Text(text = "Try Again")
                }
            }
        } else {
            // CÁCH MỚI CỦA GOOGLE: Bọc PullToRefreshBox ra ngoài LazyColumn
            PullToRefreshBox(
                isRefreshing = isLoading,
                onRefresh = { viewModel.fetchData() }, // Gọi lại API khi kéo
                modifier = Modifier.fillMaxSize()
            ) {
                LazyColumn(modifier = Modifier.fillMaxSize()) {
                    // 👇 THÊM thuộc tính key = { it.id } VÀO ĐÂY 👇
                    items(
                        items = restaurants,
                        key = { restaurant -> restaurant.id } // Sử dụng ID duy nhất của quán ăn làm key
                    ) { restaurant ->
                        Card(modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp)) {
                            Column(modifier = Modifier.padding(16.dp)) {
                                Text(text = restaurant.name, style = MaterialTheme.typography.titleLarge)
                                Text(text = restaurant.category ?: "More", color = MaterialTheme.colorScheme.secondary)
                            }
                        }
                    }
                }
            }
        }
    }
}