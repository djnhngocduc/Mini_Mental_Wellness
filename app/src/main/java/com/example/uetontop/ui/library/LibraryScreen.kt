package com.example.uetontop.ui.library

import androidx.annotation.DrawableRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
                    .padding(horizontal = 16.dp)
                    ,
                verticalArrangement = Arrangement.spacedBy(12.dp),
                contentPadding = PaddingValues(
                    top = 16.dp,
                    bottom = inner.calculateBottomPadding() + 16.dp
                )
            ) {
                item { SectionTitle("Bài tập") }
                items(sampleItemsExercise) { item ->
                    LibraryItemRow(item) { clicked ->
                        val url = clicked.videoUrl
                        if (url.isYouTubeLink()) {
                            val id = extractYouTubeId(url)
                            navController.navigate(Screen.YouTube.routeWithId(id))
                        } else {
                            navController.navigate(Screen.VideoPlayer.routeWithUrl(url))
                        }
                    }
                }

                item { Spacer(Modifier.height(16.dp)) }
                item { SectionTitle("Podcast") }
                items(sampleItemsPodcast) { item ->
                    LibraryItemRow(item) { clicked ->
                        val url = clicked.videoUrl
                        if (url.isYouTubeLink()) {
                            val id = extractYouTubeId(url)
                            navController.navigate(Screen.YouTube.routeWithId(id))
                        } else {
                            navController.navigate(Screen.VideoPlayer.routeWithUrl(url))
                        }
                    }
                }

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
    @DrawableRes val imageRes: Int,
    val videoUrl: String
)

//private val sampleItemsExercise = listOf(
//    LibraryUi("Breathwork", "Box Breathing 4-4-4-4", "Calm your nerves in 5 minutes", R.drawable.art, "https://www.youtube.com/watch?v=GViVk4RVJYE"),
//    LibraryUi("Art", "Six steps to creating a color palette", "Quick ideas to start", R.drawable.colors, "https://commondatastorage.googleapis.com/gtv-videos-bucket/sample/BigBuckBunny.mp4"),
//    LibraryUi("Colors", "Creating Color Palettes from images", "Tips and tools", R.drawable.leaves, "https://commondatastorage.googleapis.com/gtv-videos-bucket/sample/BigBuckBunny.mp4")
//)
//
//private val sampleItemsPodcast = listOf(
//    LibraryUi("UI/UX Design", "A Simple Trick For Creating Color Palettes Quickly", "Six steps to creating a color palette", R.drawable.art, "https://commondatastorage.googleapis.com/gtv-videos-bucket/sample/BigBuckBunny.mp4"),
//    LibraryUi("Art", "Six steps to creating a color palette", "Quick ideas to start", R.drawable.colors, "https://commondatastorage.googleapis.com/gtv-videos-bucket/sample/BigBuckBunny.mp4"),
//    LibraryUi("Colors", "Creating Color Palettes from images", "Tips and tools", R.drawable.leaves, "https://commondatastorage.googleapis.com/gtv-videos-bucket/sample/BigBuckBunny.mp4")
//)

private val sampleItemsExercise = listOf(
    LibraryUi(
        "Kỹ thuật thở",
        "Thở hộp 4-4-4-4",
        "Xoa dịu hệ thần kinh trong 5 phút",
        R.drawable.art,
        "https://www.youtube.com/watch?v=GViVk4RVJYE"
    ),
    LibraryUi(
        category = "Giãn cơ chánh niệm",
        title = "Thả lỏng cổ & vai",
        subtitle = "Giải tỏa căng cứng do ngồi bàn làm việc trong 6 phút",
        imageRes = R.drawable.colors,
        videoUrl = "https://www.youtube.com/watch?v=ez6Rt_hW9xQ"
    ),
    LibraryUi(
        category = "Thiền",
        title = "Quét cơ thể 10 phút",
        subtitle = "Thả lỏng và quan sát cơ thể từ đầu đến chân",
        imageRes = R.drawable.leaves,
        videoUrl = "https://commondatastorage.googleapis.com/gtv-videos-bucket/sample/Sintel.mp4"
    )
)

private val sampleItemsPodcast = listOf(
    LibraryUi(
        category = "Khoa học về căng thẳng",
        title = "Vì sao hơi thở làm dịu não bộ",
        subtitle = "Sinh lý học của sự bình tâm",
        imageRes = R.drawable.art,
        videoUrl = "https://commondatastorage.googleapis.com/gtv-videos-bucket/sample/BigBuckBunny.mp4"
    ),
    LibraryUi(
        category = "Chánh niệm",
        title = "Thiền cho người mới bắt đầu",
        subtitle = "Bắt đầu chỉ trong 7 phút",
        imageRes = R.drawable.colors,
        videoUrl = "https://commondatastorage.googleapis.com/gtv-videos-bucket/sample/ElephantsDream.mp4"
    ),
    LibraryUi(
        category = "Ngủ ngon & thư giãn",
        title = "Tiếng mưa nhẹ + kể chuyện",
        subtitle = "Thư giãn dịu nhẹ trước giờ đi ngủ",
        imageRes = R.drawable.leaves,
        videoUrl = "https://commondatastorage.googleapis.com/gtv-videos-bucket/sample/Sintel.mp4"
    )
)

private fun String.isYouTubeLink() =
    contains("youtube.com", ignoreCase = true) || contains("youtu.be", ignoreCase = true)

private fun extractYouTubeId(url: String): String {
    val uri = android.net.Uri.parse(url)
    uri.getQueryParameter("v")?.let { return it }   // youtube.com/watch?v=ID
    uri.lastPathSegment?.let { return it }          // youtu.be/ID
    val regex = "(?<=v=)[^&#?]+|(?<=be/)[^&#?]+".toRegex()
    return regex.find(url)?.value ?: url
}




@Composable
private fun LibraryItemRow(item: LibraryUi,
                           onClick: (LibraryUi) -> Unit = {}) {
    Row(
        Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
            .clip(RoundedCornerShape(12.dp))
            .clickable { onClick(item) },
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
