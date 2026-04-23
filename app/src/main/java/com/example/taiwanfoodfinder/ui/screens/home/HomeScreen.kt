package com.example.taiwanfoodfinder.ui.screens.home

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel

@Composable
fun HomeScreen(viewModel: HomeViewModel = viewModel()) {
    val isLoading = viewModel.isLoading.value
    val errorMessage = viewModel.errorMessage.value
    val restaurants = viewModel.restaurants

    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        Text(text = "Khám Phá Đài Nam", style = MaterialTheme.typography.headlineMedium)
        Spacer(modifier = Modifier.height(16.dp))

        if (isLoading) {
            CircularProgressIndicator()
        } else if (errorMessage != null) {
            Text(text = errorMessage, color = MaterialTheme.colorScheme.error)
        } else {
            LazyColumn {
                items(restaurants) { restaurant ->
                    Card(modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp)) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Text(text = restaurant.name, style = MaterialTheme.typography.titleLarge)
                            Text(text = restaurant.category ?: "Khác", color = MaterialTheme.colorScheme.secondary)
                        }
                    }
                }
            }
        }
    }
}