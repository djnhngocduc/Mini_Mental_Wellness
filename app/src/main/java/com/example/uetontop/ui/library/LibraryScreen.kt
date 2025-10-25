package com.example.uetontop.ui.library

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import com.example.uetontop.R
import androidx.navigation.compose.rememberNavController

@Composable
fun LibraryScreen(navController: NavHostController) {
    val bg = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = .25f)

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(bg)
            .padding(horizontal = 16.dp),
        contentPadding = PaddingValues(vertical = 16.dp)
    ) {
        item { SectionTitle("Exercise") }
        items(sampleItems) { LibraryItemRow(it) }
        item { Spacer(Modifier.height(16.dp)) }
        item { SectionTitle("Podcast") }
        items(sampleItems) { LibraryItemRow(it) }
        item { Spacer(Modifier.height(24.dp)) }
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
    val subtitle: String
)

private val sampleItems = listOf(
    LibraryUi("UI/UX Design", "A Simple Trick For Creating Color Palettes Quickly", "Six steps to creating a color palette"),
    LibraryUi("Art", "Six steps to creating a color palette", "Quick ideas to start"),
    LibraryUi("Colors", "Creating Color Palettes from images", "Tips and tools")
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
            painter = painterResource(R.mipmap.ic_launcher),
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
    MaterialTheme { LibraryScreen(rememberNavController()) }
}
