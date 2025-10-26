package com.example.uetontop.ui.home

import androidx.annotation.DrawableRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.example.uetontop.R

@Composable
fun HomeHeader(
    onProfileClick: () -> Unit = {},
    onChatClick: () -> Unit = {},
    onBellClick: () -> Unit = {},
    @DrawableRes avatarRes: Int = R.drawable.people
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color.White)
            .statusBarsPadding()
            .padding(horizontal = 12.dp, vertical = 6.dp),
        verticalAlignment = Alignment.CenterVertically

    ) {
        // Avatar bên trái
        ElevatedCard(
            onClick = onProfileClick,
            shape = CircleShape,
            colors = CardDefaults.elevatedCardColors(containerColor = Color(0xFFF1F1F1)),
            elevation = CardDefaults.elevatedCardElevation(defaultElevation = 0.dp),
        ) {
            Image(
                painter = painterResource(avatarRes),
                contentDescription = "Avatar",
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .size(34.dp)
                    .clip(CircleShape)
            )

        }

        Spacer(Modifier.weight(1f))

        // Nút tròn viền mảnh (chat)
        CircularIconButton(
            icon = {
                Icon(
                    imageVector = Icons.Default.Person,
                    contentDescription = "Chat"
                )
            },
            onClick = onChatClick
        )

        Spacer(Modifier.width(12.dp))
        // Nút tròn viền mảnh (chuông)

        CircularIconButton(
            icon = {
                Icon(
                    imageVector = Icons.Default.Notifications,
                    contentDescription = "Notifications"
                )
            },
            onClick = onBellClick
        )
    }
}

@Composable
private fun CircularIconButton(
    icon: @Composable () -> Unit,
    onClick: () -> Unit
) {
    // giống mockup: vòng tròn viền mảnh, nền trắng
    Surface(
        shape = CircleShape,
        tonalElevation = 0.dp,
        shadowElevation = 0.dp,
        color = Color.White,
        modifier = Modifier
            .size(36.dp)
            .border(1.dp, Color(0xFFDFDFDF), CircleShape)
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .clickable(onClick = onClick),
            contentAlignment = Alignment.Center
        ) {
            icon()
        }
    }
}