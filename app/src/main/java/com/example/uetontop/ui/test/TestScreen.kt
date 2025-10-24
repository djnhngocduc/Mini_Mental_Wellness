package com.example.uetontop.ui.test

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.uetontop.navigation.Screen
import com.example.uetontop.ui.home.BottomBar
import com.example.uetontop.ui.home.HomeHeader

@Composable
fun TestScreen(navController: NavController) {
    val tests = remember {
        listOf(
            "Test 1: BECK",
            "Test 2: PHQ-9",
            "Test 3: DASS 21",
            "..",
            "..."
        )
    }
    var selected by remember { mutableStateOf<String?>(null) }

    Scaffold(
        bottomBar = { BottomBar(navController) },
        containerColor = Color(0xFFF5F5F5)
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .padding(16.dp)
                .fillMaxSize()
        ) {
            HomeHeader(
                onProfileClick = { navController.navigate(Screen.Profile.route) },
                onChatClick = { /* TODO: open chat/inbox */ },
                onBellClick = { /* TODO: open notifications */ }
            )

            // Nội dung scroll
            LazyColumn(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                contentPadding = PaddingValues(bottom = 16.dp) // chừa chỗ cho nút
            ) {
                item {
                    Spacer(Modifier.height(12.dp))
                    Text(
                        "Select the appropriate test",
                        style = MaterialTheme.typography.headlineSmall.copy(
                            fontWeight = FontWeight.SemiBold,
                            color = Color(0xFF1E2330)
                        )
                    )
                    Spacer(Modifier.height(6.dp))
                    Text(
                        "Please choose the mental health test that is right for you so we can better understand your current condition.",
                        style = MaterialTheme.typography.bodyMedium.copy(color = Color(0xFF8D93A1)),
                        lineHeight = MaterialTheme.typography.bodyMedium.lineHeight
                    )
                    Spacer(Modifier.height(12.dp))
                }

                items(tests) { label ->
                    TestCard(
                        text = label,
                        selected = selected == label,
                        onClick = { selected = label }
                    )
                    Spacer(Modifier.height(12.dp))
                }
            }

            // Nút Next cố định phía dưới
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color(0xFFF5F5F7))
                    .padding(16.dp),
                contentAlignment = Alignment.Center
            ) {
                GradientButton(
                    text = "Next",
                    enabled = selected != null,
                    onClick = {
                        // TODO: điều hướng sang màn câu hỏi của test đã chọn
                        // ví dụ:
                        // navController.navigate(Screen.TestDetail.route + "?name=${selected}")
                    }
                )
            }
        }
    }
}

@Composable
private fun TestCard(text: String, selected: Boolean, onClick: () -> Unit) {
    val borderColor = if (selected) Color(0xFF9D7BFF) else Color(0xFFE2E2E8)
    val shape = RoundedCornerShape(16.dp)
    val interaction = remember { MutableInteractionSource() }

    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .shadow(
                if (selected) 6.dp else 3.dp,
                shape,
                clip = false
            )
            .clip(shape)
            .border(1.dp, borderColor, shape)
            .clickable(
                interactionSource = interaction,
                indication = null,
                onClick = onClick),
        color = Color(0xFFFDFDFE),
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 16.dp, horizontal = 20.dp),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = text,
                style = MaterialTheme.typography.bodyLarge.copy(
                    fontWeight = FontWeight.Medium,
                    color = Color(0xFF1E2330)
                )
            )
        }
    }
}

@Composable
private fun GradientButton(
    text: String,
    enabled: Boolean,
    onClick: () -> Unit
) {
    val shape = RoundedCornerShape(16.dp)
    val brush = Brush.linearGradient(listOf(Color(0xFFB38BFF), Color(0xFF9D7BFF)))
    val disabledBrush = Brush.linearGradient(listOf(Color(0xFFE8E8ED), Color(0xFFE0E0E6)))

    Surface(
        onClick = onClick,
        enabled = enabled,
        shape = shape,
        shadowElevation = 2.dp,
        color = Color.Transparent
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(48.dp)
                .background(if (enabled) brush else disabledBrush, shape = shape),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = text,
                style = MaterialTheme.typography.titleMedium.copy(
                    fontWeight = FontWeight.SemiBold,
                    color = if (enabled) Color.White else Color(0xFF9A9AA5)
                ),
                textAlign = TextAlign.Center
            )
        }
    }
}