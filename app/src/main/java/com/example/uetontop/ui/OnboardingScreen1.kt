package com.example.uetontop.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.uetontop.R
import com.example.uetontop.navigation.Screen

@Composable
fun OnboardingScreen1(navController: NavController) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        contentAlignment = Alignment.BottomCenter
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier.fillMaxSize()
        ) {
            Spacer(Modifier.height(40.dp))

            // Ảnh minh họa (thay bằng ảnh trong drawable nếu bạn có)
            Image(
                painter = painterResource(id = R.drawable.ic_launcher_background), // đặt ảnh ở res/drawable
                contentDescription = null,
                modifier = Modifier.size(250.dp)
            )

            Text(
                "Welcome to\nMini mental wellness",
                fontSize = 22.sp,
                color = Color.Black,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(top = 32.dp),
                lineHeight = 28.sp
            )

            Spacer(Modifier.weight(1f))

            // Nút mũi tên
            FloatingActionButton(
                onClick = { navController.navigate(Screen.Onboarding2.route) },
                containerColor = Color.Black
            ) {
                Icon(Icons.Default.ArrowForward, contentDescription = "Next", tint = Color.White)
            }
        }
    }
}
