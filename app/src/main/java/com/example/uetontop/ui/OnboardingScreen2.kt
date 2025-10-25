package com.example.uetontop.ui

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.uetontop.navigation.Screen


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun OnboardingScreen2(navController: NavController) {
    var selectedGender by remember { mutableStateOf("Male") }
    var height by remember { mutableStateOf(180f) }
    var weight by remember { mutableStateOf(80f) }

    Scaffold(
        bottomBar = {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                contentAlignment = Alignment.CenterEnd
            ) {
                FloatingActionButton(
                    onClick = { navController.navigate(Screen.Home.route) },
                    containerColor = Color.Black
                ) {
                    Icon(Icons.Default.ArrowForward, contentDescription = "Next", tint = Color.White)
                }
            }
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .padding(horizontal = 24.dp, vertical = 16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                "Give us some basic information",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                color = Color.Black
            )
            Spacer(Modifier.height(24.dp))

            // Giới tính
            Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                GenderButton("Male", selectedGender == "Male") { selectedGender = "Male" }
                GenderButton("Female", selectedGender == "Female") { selectedGender = "Female" }
            }

            Spacer(Modifier.height(24.dp))

            Text("Height", fontWeight = FontWeight.Medium)
            SliderWithLabel(value = height, onValueChange = { height = it }, min = 50f, max = 500f, unit = "cm")

            Spacer(Modifier.height(16.dp))

            Text("Weight", fontWeight = FontWeight.Medium)
            SliderWithLabel(value = weight, onValueChange = { weight = it }, min = 20f, max = 200f, unit = "kg")
        }
    }
}

@Composable
fun GenderButton(label: String, selected: Boolean, onClick: () -> Unit) {
    val bg = if (selected) Color(0xFFE0E0E0) else Color.White
    val border = if (selected) Color.Black else Color(0xFFDDDDDD)

    Surface(
        modifier = Modifier // 👈 thêm dấu chấm
            .height(80.dp)
            .padding(4.dp)
            .clickable(onClick = onClick),
        shape = MaterialTheme.shapes.medium,
        color = bg, // 👈 dùng color đúng cú pháp Material 3
        border = BorderStroke(1.dp, border)
    ) {
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier.fillMaxSize()
        ) {
            Text(label, color = Color.Black)
        }
    }
}


@Composable
fun SliderWithLabel(
    value: Float,
    onValueChange: (Float) -> Unit,
    min: Float,
    max: Float,
    unit: String
) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text("${value.toInt()}$unit", fontSize = 16.sp, color = Color.Black)
        Slider(
            value = value,
            onValueChange = onValueChange,
            valueRange = min..max,
            modifier = Modifier.padding(horizontal = 8.dp)
        )
    }
}
