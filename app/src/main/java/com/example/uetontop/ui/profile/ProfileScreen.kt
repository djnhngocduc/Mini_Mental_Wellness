package com.example.uetontop.ui.profile

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.uetontop.navigation.Screen
import com.example.uetontop.ui.home.BottomBar
import com.example.uetontop.ui.home.HomeHeader
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Refresh
import androidx.compose.material.icons.outlined.Logout
import androidx.compose.material.icons.outlined.ChevronRight
import androidx.compose.foundation.lazy.LazyColumn


@Composable
fun ProfileScreen(navController: NavController) {
    val bg = Color(0xFFF5F5F7)
    var notif by remember { mutableStateOf(true) }

    Scaffold(
        bottomBar = { BottomBar(navController) },
        containerColor = bg,
        contentWindowInsets = WindowInsets(0.dp)
    ) { inner ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(bg)
                .padding(inner)
        ) {
            HomeHeader(
                onProfileClick = { navController.navigate(Screen.Profile.route) },
                onChatClick = { /* ... */ },
                onBellClick = { /* ... */ },
            )
            // ---- Row: "3 days ago" + refresh button

            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(horizontal = 16.dp, vertical = 16.dp)
            ) {
                item {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            "3 ngày trước",
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            modifier = Modifier.weight(1f)
                        )
                        IconButton(onClick = {}) {
                            Icon(
                                imageVector = Icons.Outlined.Refresh,
                                contentDescription = "Refresh"
                            )
                        }
                    }
                }

                item {
                    Spacer(Modifier.height(12.dp))
                }

                // Row 1
                item {
                    Row(
                        Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        StatCard(
                            title = "Nhịp tim", value = "78", unit = "nhịp/phút",
                            container = MaterialTheme.colorScheme.errorContainer.copy(alpha = .25f),
                            onColor = MaterialTheme.colorScheme.error,
                            modifier = Modifier.weight(1f)
                        )
                        StatCard(
                            title = "Vận động", value = "24", unit = "phút",
                            container = MaterialTheme.colorScheme.primaryContainer.copy(alpha = .35f),
                            onColor = MaterialTheme.colorScheme.primary,
                            modifier = Modifier.weight(1f)
                        )
                    }
                }

                item {
                    Spacer(Modifier.height(12.dp))
                }

                // Row 2
                item {
                    Row(
                        Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        StatCard(
                            title = "Đi bộ", value = "10", unit = "km",
                            container = MaterialTheme.colorScheme.tertiaryContainer.copy(alpha = .35f),
                            onColor = MaterialTheme.colorScheme.tertiary,
                            modifier = Modifier.weight(1f)
                        )
                        StatCard(
                            title = "Ngủ", value = "8", unit = "giờ",
                            container = MaterialTheme.colorScheme.secondaryContainer.copy(alpha = .35f),
                            onColor = MaterialTheme.colorScheme.secondary,
                            modifier = Modifier.weight(1f)
                        )
                    }
                }

                item {
                    Spacer(Modifier.height(16.dp))
                }

                item {
                    SettingSwitchRow("Thông báo", notif) { notif = it }
                }

                item {
                    Spacer(Modifier.height(12.dp))
                }

                item {
                    SettingArrowRow("Ngôn ngữ")
                }

                item {
                    Spacer(Modifier.height(12.dp))
                }

                item {
                    SettingArrowRow("Đổi mật khẩu")
                }

                item {
                    Spacer(Modifier.height(16.dp))
                }

                item {
                    ElevatedCard(
                        shape = RoundedCornerShape(16.dp),
                        colors = CardDefaults.elevatedCardColors(
                            //                containerColor = MaterialTheme.colorScheme.errorContainer.copy(alpha = .35f)
                            containerColor = Color(0xFFFFE0E0)
                        ),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Row(
                            Modifier
                                .fillMaxWidth()
                                .padding(16.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                "Đăng xuất",
                                fontWeight = FontWeight.Bold,
                                modifier = Modifier.weight(1f)
                            )
                            Icon(
                                imageVector = Icons.Outlined.Logout,
                                contentDescription = null
                            )
                        }
                    }
                }

                item {
                    Spacer(Modifier.height(24.dp))
                }
            }
        }
    }
}

@Composable
private fun StatCard(
    title: String,
    value: String,
    unit: String,
    container: Color,
    onColor: Color,
    modifier: Modifier = Modifier
) {
    Card(
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = container),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp),
        modifier = modifier.height(120.dp)
    ) {
        Column(Modifier.padding(16.dp)) {
            Text(title, color = onColor)
            Spacer(Modifier.height(6.dp))
            Row(verticalAlignment = Alignment.Bottom) {
                Text(
                    value,
                    color = onColor,
                    fontWeight = FontWeight.Bold,
                    style = MaterialTheme.typography.headlineMedium
                )
                Spacer(Modifier.width(4.dp))
                Text(unit, color = onColor)
            }
        }
    }
}

@Composable
private fun SettingSwitchRow(
    title: String,
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit
) {
    ElevatedCard(shape = RoundedCornerShape(12.dp), modifier = Modifier.fillMaxWidth()) {
        Row(
            Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(title, modifier = Modifier.weight(1f))
            Switch(checked = checked, onCheckedChange = onCheckedChange)
        }
    }
}

@Composable
private fun SettingArrowRow(title: String) {
    ElevatedCard(shape = RoundedCornerShape(12.dp), modifier = Modifier.fillMaxWidth()) {
        Row(
            Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(title, modifier = Modifier.weight(1f))
            Icon(
                imageVector = Icons.Outlined.ChevronRight,
                contentDescription = null
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun PreviewProfile() {
    val navController = rememberNavController()
    MaterialTheme {
        ProfileScreen(navController)
    }
}
