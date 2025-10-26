package com.example.uetontop.ui

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.uetontop.R
import com.example.uetontop.navigation.Screen

@Composable
fun OnboardingScreen1(navController: NavController) {
    val bg = Color(0xFFF8FAFD)          // trắng hơi ấm
    val slab = Color(0xFFF0F2F6)        // mảng xám nhạt phần dưới

    // Vẽ mảng nền chéo trước rồi đặt nội dung lên
    Box(
        modifier = Modifier
            .fillMaxSize()
            .systemBarsPadding()        // trừ status/navigation bar
            .background(slab)
    ) {
        // Tấm nền vát chéo ~ 58% chiều cao
        Canvas(modifier = Modifier.fillMaxSize()) {
            val h = size.height
            val w = size.width
            val path = Path().apply {
                moveTo(0f, 0f)
                lineTo(w, 0f)
                lineTo(w, h * 0.55f)
                lineTo(0f, h * 0.45f)
                close()
            }
            drawPath(path, color = bg)
        }

        // Nội dung
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 24.dp),
            horizontalAlignment = Alignment.Start
        ) {

            // Illustration (dùng ảnh local của bạn)
            Image(
                painter = painterResource(R.drawable.screen1 /* thay id ảnh của bạn */),
                contentDescription = null,
                modifier = Modifier
                    .padding(top = 8.dp)
                    .size(width = 340.dp, height = 457.dp),
                contentScale = ContentScale.Fit
            )

            Spacer(Modifier.height(56.dp))

            Column {
                Text(
                    text = "Welcome to",
                    color = Color(0xFF111827),
                    fontSize = 29.sp,
                    fontWeight = FontWeight.ExtraBold,
                    letterSpacing = (-0.2).sp,
                    textAlign = TextAlign.Start
                )

                Spacer(Modifier.height(4.dp)) // chỉnh 4–12dp tùy ý

                Text(
                    text = "Mini mental wellness",
                    color = Color(0xFF111827),
                    fontSize = 29.sp,
                    fontWeight = FontWeight.ExtraBold,
                    letterSpacing = (-0.2).sp,
                    textAlign = TextAlign.Start
                )
            }

            Spacer(Modifier.weight(1f))

            // Nút mũi tên dưới-phải
            Box(modifier = Modifier.fillMaxSize()) {
                FloatingActionButton(
                    onClick = { navController.navigate(Screen.Onboarding2.route) },
                    shape = CircleShape,
                    containerColor = Color(0xFF0F172A),   // đen xanh
                    modifier = Modifier
                        .align(Alignment.BottomEnd)
                        .padding(end = 24.dp, bottom = 40.dp)
                        .size(56.dp)
                        .shadow(10.dp, CircleShape, clip = false)
                ) {
                    Icon(
                        imageVector = Icons.Filled.ArrowForward,
                        contentDescription = "Next",
                        tint = Color.White
                    )
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun PreviewScreen1() {
    val navController = rememberNavController()
    MaterialTheme {
        OnboardingScreen1(navController)
    }
}
