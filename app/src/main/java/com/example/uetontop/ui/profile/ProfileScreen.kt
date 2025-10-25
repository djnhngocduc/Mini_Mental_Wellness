package com.example.uetontop.ui.profile

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.uetontop.R


@Composable
fun ProfileScreen(
    onRefresh: () -> Unit = {}   // <-- callback khi bấm refresh
) {
    val bg = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = .25f)
    var notif by remember { mutableStateOf(true) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(bg)
            .padding(horizontal = 16.dp, vertical = 16.dp)
    ) {
        // ---- Row: "3 days ago" + refresh button
        Row(
            Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                "3 days ago",
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.weight(1f)
            )
            IconButton(onClick = onRefresh) {
                // Không cần material-icons-extended: dùng icon hệ thống
                Icon(
                    painter = painterResource(android.R.drawable.ic_popup_sync),
                    contentDescription = "Refresh"
                )
            }
        }
        Spacer(Modifier.height(12.dp))

        // Row 1
        Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(12.dp)) {
            StatCard(
                title = "Heart Rate", value = "78", unit = "bpm",
                container = MaterialTheme.colorScheme.errorContainer.copy(alpha = .25f),
                onColor = MaterialTheme.colorScheme.error,
                modifier = Modifier.weight(1f)
            )
            StatCard(
                title = "Exercise", value = "24", unit = "min",
                container = MaterialTheme.colorScheme.primaryContainer.copy(alpha = .35f),
                onColor = MaterialTheme.colorScheme.primary,
                modifier = Modifier.weight(1f)
            )
        }
        Spacer(Modifier.height(12.dp))

        // Row 2
        Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(12.dp)) {
            StatCard(
                title = "Walking", value = "10", unit = "km",
                container = MaterialTheme.colorScheme.tertiaryContainer.copy(alpha = .35f),
                onColor = MaterialTheme.colorScheme.tertiary,
                modifier = Modifier.weight(1f)
            )
            StatCard(
                title = "Sleep", value = "8", unit = "hrs",
                container = MaterialTheme.colorScheme.secondaryContainer.copy(alpha = .35f),
                onColor = MaterialTheme.colorScheme.secondary,
                modifier = Modifier.weight(1f)
            )
        }

        Spacer(Modifier.height(16.dp))

        SettingSwitchRow("Notifications", notif) { notif = it }
        Spacer(Modifier.height(12.dp))
        SettingArrowRow("Language")
        Spacer(Modifier.height(12.dp))
        SettingArrowRow("Change Password")

        Spacer(Modifier.height(16.dp))

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
                Text("Sign Out", fontWeight = FontWeight.Bold, modifier = Modifier.weight(1f))
                Icon(
                    painterResource(android.R.drawable.ic_menu_revert),
                    contentDescription = null
                )
            }
        }
        Spacer(Modifier.height(24.dp))
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
    ElevatedCard(
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.elevatedCardColors(containerColor = container),
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
            Text("›") // dùng ký tự mũi tên, không cần phụ thuộc icon
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun PreviewProfile() {
    MaterialTheme { ProfileScreen() }
}
