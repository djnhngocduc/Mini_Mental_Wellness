package com.example.uetontop.ui.home

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.uetontop.navigation.Screen

data class Psychologist(
    val name: String,
    val specialization: String,
    val contact: String,
    val availableTime: String
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ScheduleScreen(navController: NavController) {
    val experts = listOf(
        Psychologist(
            "Dr. Nguyễn Minh Anh",
            "Tâm lý học hành vi",
            "minhanh@vnu.edu.vn | 0909 123 456",
            "Thứ 2: 9h–11h, Thứ 4: 14h–16h"
        ),
        Psychologist(
            "ThS. Trần Đức Huy",
            "Tư vấn học đường",
            "duchuy@vnu.edu.vn | 0908 555 888",
            "Thứ 3: 10h–12h, Thứ 5: 15h–17h"
        ),
        Psychologist(
            "TS. Lê Mai Phương",
            "Liệu pháp nhận thức – hành vi (CBT)",
            "maiphuong@vnu.edu.vn | 0912 222 777",
            "Thứ 6: 8h–11h"
        )
    )

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Lịch & Liên hệ", color = Color.White) },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back",
                            tint = Color.White
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary
                )
            )
        },
        containerColor = Color(0xFFF5F5F5)
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier
                .padding(innerPadding)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items(experts) { expert ->
                ExpertCard(expert)
            }
        }
    }
}

@Composable
fun ExpertCard(expert: Psychologist) {
    Surface(
        shape = RoundedCornerShape(16.dp),
        tonalElevation = 1.dp,
        color = Color.White,
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            Text(expert.name, style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold))
            Text(expert.specialization, style = MaterialTheme.typography.bodyMedium, color = Color.Gray)
            Text(expert.contact, style = MaterialTheme.typography.bodySmall)
            Spacer(Modifier.height(4.dp))
            Text(
                "⏰ Lịch rảnh: ${expert.availableTime}",
                style = MaterialTheme.typography.bodySmall.copy(color = Color(0xFF4A90E2))
            )
        }
    }
}
