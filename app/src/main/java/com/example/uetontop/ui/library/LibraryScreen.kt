package com.example.uetontop.ui.library

import androidx.annotation.DrawableRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.uetontop.R
import com.example.uetontop.navigation.Screen
import com.example.uetontop.ui.home.BottomBar
import com.example.uetontop.ui.home.HomeHeader

@Composable
fun LibraryScreen(navController: NavController) {
    val bg = Color(0xFFF5F5F7)

    Scaffold(
        bottomBar = { BottomBar(navController) },
        containerColor = bg,
        contentWindowInsets = WindowInsets(0.dp)
    ) { inner ->
        Column(
            modifier = Modifier
                .padding(inner)
                .fillMaxSize()
                .background(bg)
        ) {
            HomeHeader(
                onProfileClick = { navController.navigate(Screen.Profile.route) },
                onChatClick = { /* ... */ },
                onBellClick = { /* ... */ },
            )

            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .background(bg)
                    .padding(horizontal = 16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp),
                contentPadding = PaddingValues(
                    top = 16.dp,
                    bottom = inner.calculateBottomPadding() + 16.dp
                )
            ) {
                item { SectionTitle("Exercise") }
                items(sampleItems) { LibraryItemRow(it) }
                item { Spacer(Modifier.height(16.dp)) }
                item { SectionTitle("Podcast") }
                items(sampleItems) { LibraryItemRow(it) }
            }
        }
    }
}

@Composable
private fun SectionTitle(text: String) {
    Text(
        text = text,
        style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold),
        modifier = Modifier.padding(vertical = 8.dp)
    )
}

private data class LibraryUi(
    val category: String,
    val title: String,
    val subtitle: String,
    @DrawableRes val imageRes: Int
)

private val sampleItems = listOf(
    LibraryUi("UI/UX Design", "A Simple Trick For Creating Color Palettes Quickly", "Six steps to creating a color palette", R.drawable.art),
    LibraryUi("Art", "Six steps to creating a color palette", "Quick ideas to start", R.drawable.colors),
    LibraryUi("Colors", "Creating Color Palettes from images", "Tips and tools", R.drawable.leaves)
)

@Composable
private fun LibraryItemRow(item: LibraryUi) {
    Row(
        Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Image(
            painter = painterResource(item.imageRes),
            contentDescription = null,
            modifier = Modifier
                .size(84.dp)
                .clip(RoundedCornerShape(12.dp)),
            contentScale = ContentScale.Crop
        )
        Spacer(Modifier.width(12.dp))
        Column(Modifier.weight(1f)) {
            Text(item.category, color = MaterialTheme.colorScheme.onSurfaceVariant, style = MaterialTheme.typography.labelMedium)
            Text(item.title, fontWeight = FontWeight.Bold, style = MaterialTheme.typography.bodyLarge)
            Text(item.subtitle, color = MaterialTheme.colorScheme.onSurfaceVariant, style = MaterialTheme.typography.labelMedium)
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun PreviewLibrary() {
    val navController = rememberNavController()
    MaterialTheme(colorScheme = lightColorScheme()) {
        LibraryScreen(navController)
    }
}
