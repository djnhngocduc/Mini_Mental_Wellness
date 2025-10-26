package com.example.uetontop.ui.home

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material.icons.filled.Mic
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.material3.Icon
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.compose.rememberNavController
import com.example.uetontop.navigation.Screen

@Composable
fun HomeScreen(navController: NavController) {
    val pageBg = Color(0xFFF5F5F5)

    Scaffold(
        bottomBar = { BottomBar(navController) },
        containerColor = pageBg,
        contentWindowInsets = WindowInsets(0.dp)
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
        ) {
            HomeHeader(
                onProfileClick = { navController.navigate(Screen.Profile.route) },
                onCalendarClick = { navController.navigate(Screen.Schedule.route) },
                onChatClick = { navController.navigate(Screen.Chat.route) },
                onBellClick = { /* open notifications */ },
            )

            // ---------- Search ----------
            Spacer(Modifier.height(8.dp))
            Box(Modifier.padding(horizontal = 16.dp)) {
                OutlinedTextField(
                    value = "",
                    onValueChange = {},
                    placeholder = { Text("Tìm kiếm") },
                    leadingIcon = {
                        Icon(
                            imageVector = Icons.Default.Search,
                            contentDescription = "Text search"
                        )
                    },
                    trailingIcon = {
                        Icon(
                            imageVector = Icons.Default.Mic,
                            contentDescription = "Voice search"
                        )
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .shadow(2.dp, RoundedCornerShape(28.dp), clip = false),
                    shape = RoundedCornerShape(28.dp),
                    singleLine = true
                )
            }

            Spacer(Modifier.height(12.dp))

            // ---------- Today card ----------
            TodayCard(
                modifier = Modifier
                    .padding(horizontal = 16.dp)
            )

            Spacer(Modifier.height(20.dp))

            // ---------- My plan ----------
            Text(
                "My plan",
                style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold),
                modifier = Modifier.padding(horizontal = 16.dp)
            )

            Spacer(Modifier.height(12.dp))

            MyPlanGrid(
                items = listOf(
                    PlanUi(
                        title = "Get reminders for your relaxing time.\n",
                        bg = Color(0xFFFFE1E1),
                        fg = Color(0xFFB52222),
                        emoji = "🔔"
                    ),
                    PlanUi(
                        title = "Learn about people's mental health practices",
                        bg = Color(0xFFDDE2FF),
                        fg = Color(0xFF2C3BAA),
                        emoji = "🔎"
                    ),
                    PlanUi(
                        title = "Keep track of what\nmatters every day",
                        bg = Color(0xFFE8D9FF),
                        fg = Color(0xFF6B3FF3),
                        emoji = "💜"
                    ),
                    PlanUi(
                        title = "Set reminders for\nsleep & focus",
                        bg = Color(0xFFFFE3C6),
                        fg = Color(0xFF8A4A00),
                        emoji = "🧭"
                    ),
                ),
                onClick = { /* TODO */ },
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
            )
        }
    }
}

/* -------------------- TODAY -------------------- */

@Composable
private fun TodayCard(modifier: Modifier = Modifier) {
    var selected by remember { mutableIntStateOf(-1) }
    ElevatedCard(
        modifier = modifier.fillMaxWidth(),
        colors = CardDefaults.elevatedCardColors(containerColor = Color.White),
        elevation = CardDefaults.elevatedCardElevation(defaultElevation = 1.dp),
        shape = RoundedCornerShape(14.dp)
    ) {
        Column(Modifier.padding(16.dp)) {
            Text(
                "Today",
                style = MaterialTheme.typography.titleLarge.copy(
                    fontWeight = FontWeight.ExtraBold,
                    color = Color(0xFF1F2A5A)
                )
            )
            Spacer(Modifier.height(4.dp))
            Text("How are you feeling?", style = MaterialTheme.typography.bodyMedium)

            Spacer(Modifier.height(12.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                val moods = listOf("😞", "😕", "😐", "🙂", "😄")
                moods.forEachIndexed { i, emoji ->
                    MoodButton(
                        emoji = emoji,
                        selected = selected == i,
                        onClick = { selected = i }
                    )
                }
            }
        }
    }
}

@Composable
private fun MoodButton(
    emoji: String,
    selected: Boolean,
    onClick: () -> Unit
) {
    val bg = if (selected) Color(0xFFEAE7FF) else Color(0xFFF4F5FA)
    val border = if (selected) Color(0xFF6F5BFF) else Color(0xFFE6E6ED)

    Surface(
        onClick = onClick,
        shape = CircleShape,
        color = bg,
        border = BorderStroke(1.dp, border),
        modifier = Modifier.size(44.dp),
        tonalElevation = 0.dp
    ) {
        Box(contentAlignment = Alignment.Center, modifier = Modifier.fillMaxSize()) {
            Text(emoji, fontSize = 20.sp, textAlign = TextAlign.Center)
        }
    }
}

/* -------------------- MY PLAN GRID -------------------- */

private data class PlanUi(
    val title: String,
    val bg: Color,
    val fg: Color,
    val emoji: String
)

@Composable
private fun MyPlanGrid(
    items: List<PlanUi>,
    onClick: (PlanUi) -> Unit,
    modifier: Modifier = Modifier
) {
    LazyVerticalGrid(
        modifier = modifier,
        columns = GridCells.Fixed(2),
        horizontalArrangement = Arrangement.spacedBy(12.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp),
        contentPadding = PaddingValues(bottom = 16.dp)
    ) {
        items(items) { item ->
            PlanCardV2(item = item, onClick = { onClick(item) })
        }
    }
}

@Composable
private fun PlanCardV2(item: PlanUi, onClick: () -> Unit) {
    Card(
        onClick = onClick,
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = item.bg),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp),
        modifier = Modifier
            .fillMaxWidth()
            .heightIn(min = 140.dp)
    ) {
        Column(Modifier.padding(14.dp)) {
            // icon tròn góc trái
            Box(
                modifier = Modifier
                    .size(36.dp)
                    .clip(CircleShape)
                    .background(Color.White.copy(alpha = .75f)),
                contentAlignment = Alignment.Center
            ) {
                Text(item.emoji, fontSize = 18.sp)
            }

            Spacer(Modifier.height(10.dp))
            Text(
                item.title,
                color = item.fg,
                style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.SemiBold),
                maxLines = 3,
                overflow = TextOverflow.Ellipsis
            )

            Spacer(Modifier.weight(1f))

            // learn more
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    "Learn more",
                    color = item.fg,
                    style = MaterialTheme.typography.labelLarge
                )
                Spacer(Modifier.width(6.dp))
                Icon(
                    Icons.Default.ArrowForward,
                    contentDescription = null,
                    tint = item.fg,
                    modifier = Modifier.size(16.dp)
                )
            }
        }
    }
}


@Preview(showBackground = true)
@Composable
private fun PreviewHome() {
    val navController = rememberNavController()
    MaterialTheme {
        HomeScreen(navController)
    }
}

