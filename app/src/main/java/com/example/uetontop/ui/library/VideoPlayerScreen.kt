package com.example.uetontop.ui.library
import android.net.Uri
import androidx.activity.compose.BackHandler
import androidx.annotation.OptIn
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.viewinterop.AndroidView
import androidx.media3.common.MediaItem
import androidx.media3.common.util.UnstableApi
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.ui.PlayerView

@ExperimentalMaterial3Api
@OptIn(UnstableApi::class, ExperimentalMaterial3Api::class, ExperimentalMaterial3Api::class)
@Composable
fun VideoPlayerScreen(videoUrl: String, onBack: () -> Unit) {
    val context = LocalContext.current

    // 1) Tạo player và chuẩn bị media
    val exoPlayer = remember(videoUrl) {
        ExoPlayer.Builder(context).build().apply {
            if (videoUrl.isNotBlank()) {
                val item = MediaItem.fromUri(Uri.parse(videoUrl))
                setMediaItem(item)
            }
            prepare()
            playWhenReady = true
        }
    }

    // 2) Giải phóng tài nguyên khi rời màn
    DisposableEffect(exoPlayer) {
        onDispose { exoPlayer.release() }
    }

    BackHandler { onBack() }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Playing") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(
                            painterResource(android.R.drawable.ic_media_previous),
                            contentDescription = "Back"
                        )
                    }
                }
            )
        }
    ) { inner ->
        if (videoUrl.isBlank()) {
            // Trường hợp tham số URL không truyền đúng
            Box(
                Modifier
                    .padding(inner)
                    .fillMaxSize()
                    .background(Color.Black),
                contentAlignment = Alignment.Center
            ) { Text("No video URL", color = Color.White) }
        } else {
            // 3) GẮN player cho PlayerView (lưu ý dùng exoPlayer, không phải 'player = player')
            AndroidView(
                modifier = Modifier
                    .padding(inner)
                    .fillMaxSize(),
                factory = { ctx ->
                    PlayerView(ctx).apply {
                        useController = true
                        setShowBuffering(PlayerView.SHOW_BUFFERING_WHEN_PLAYING)
                        this.player = exoPlayer          // <— quan trọng
                    }
                },
                update = { view -> view.player = exoPlayer } // đảm bảo update lại nếu recomposed
            )
        }
    }
}






