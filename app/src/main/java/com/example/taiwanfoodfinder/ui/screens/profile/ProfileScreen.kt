package com.example.taiwanfoodfinder.ui.screens.profile

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.example.taiwanfoodfinder.data.api.TokenManager

@Composable
fun ProfileScreen(
    onLogout: () -> Unit // Cực kỳ quan trọng: Callback để báo cho màn hình cha
) {
    val currentUser = TokenManager.getUser()
    val avatarUrl = currentUser?.imageUrl
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        // 1. Phần Header: Avatar và Thông tin người dùng
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 32.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Avatar giả lập (Sau này có thể xài AsyncImage của Coil để load ảnh thật)
            if (!avatarUrl.isNullOrEmpty()) {
                // NẾU CÓ LINK ẢNH: Dùng thư viện Coil để tải và cắt tròn
                AsyncImage(
                    model = avatarUrl,
                    contentDescription = "Avatar",
                    modifier = Modifier
                        .size(100.dp)
                        .clip(CircleShape)
                        .background(MaterialTheme.colorScheme.primaryContainer),
                    contentScale = ContentScale.Crop // Cực kỳ quan trọng: Giúp ảnh lấp đầy hình tròn mà không bị méo
                )
            } else {
                // NẾU KHÔNG CÓ LINK (Hoặc User mới chưa cập nhật ảnh): Hiện Icon mặc định
                Box(
                    modifier = Modifier
                        .size(100.dp)
                        .clip(CircleShape)
                        .background(MaterialTheme.colorScheme.primaryContainer),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.Person,
                        contentDescription = "Avatar Placeholder",
                        modifier = Modifier.size(60.dp),
                        tint = MaterialTheme.colorScheme.onPrimaryContainer
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = currentUser?.name ?:"", // Chỗ này sau lấy từ API User Info
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Bold
            )
//            Text(
//                text = "hoangmit@example.com",
//                style = MaterialTheme.typography.bodyMedium,
//                color = MaterialTheme.colorScheme.secondary
//            )
        }

        Divider(thickness = 1.dp, color = MaterialTheme.colorScheme.surfaceVariant)

        // 2. Danh sách các tính năng (Menu)
        Column(modifier = Modifier.padding(16.dp)) {
            ProfileMenuItem(icon = Icons.Default.FavoriteBorder, title = "Quán ăn đã lưu") {}
            ProfileMenuItem(icon = Icons.Default.Star, title = "Đánh giá của tôi") {}
            ProfileMenuItem(icon = Icons.Default.Settings, title = "Cài đặt tài khoản") {}
            ProfileMenuItem(icon = Icons.Default.Share, title = "Hỗ trợ & Trợ giúp") {}
        }

        Spacer(modifier = Modifier.weight(1f)) // Đẩy nút Đăng xuất xuống đáy

        // 3. Nút Đăng Xuất
        Button(
            onClick = {
                // Bước 1: Xóa Token trong két sắt
                TokenManager.clearUserToken()
                TokenManager.clearUser()
                // Bước 2: Bắn tín hiệu ra ngoài để Compose vẽ lại màn hình Login
                onLogout()
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(24.dp)
                .height(50.dp),
            colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.error),
            shape = RoundedCornerShape(12.dp)
        ) {
            Icon(Icons.Default.ExitToApp, contentDescription = null, modifier = Modifier.padding(end = 8.dp))
            if (currentUser?.id != null){
                Text("Logout", fontSize = MaterialTheme.typography.titleMedium.fontSize)
            }else{
                Text("Login", fontSize = MaterialTheme.typography.titleMedium.fontSize)
            }
//            Text("Logout", fontSize = MaterialTheme.typography.titleMedium.fontSize)
        }
    }
}

// Hàm hỗ trợ vẽ từng dòng Menu cho đẹp và tái sử dụng được
@Composable
fun ProfileMenuItem(icon: ImageVector, title: String, onClick: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(8.dp))
            .clickable { onClick() }
            .padding(vertical = 16.dp, horizontal = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = icon,
            contentDescription = title,
            tint = MaterialTheme.colorScheme.primary
        )
        Spacer(modifier = Modifier.width(16.dp))
        Text(
            text = title,
            style = MaterialTheme.typography.bodyLarge,
            fontWeight = FontWeight.Medium
        )
        Spacer(modifier = Modifier.weight(1f))
        Icon(
            imageVector = Icons.Default.FavoriteBorder,
            contentDescription = null,
            tint = Color.Gray
        )
    }
}