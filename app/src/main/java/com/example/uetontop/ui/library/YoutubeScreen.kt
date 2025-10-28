package com.example.uetontop.ui.library

import android.content.Intent
import android.net.Uri
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.viewinterop.AndroidView
import androidx.lifecycle.compose.LocalLifecycleOwner
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.PlayerConstants
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.AbstractYouTubePlayerListener
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.views.YouTubePlayerView

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun YouTubeScreen(
    videoId: String,          // chỉ ID (vd: "GViVk4RVJYE"), không phải full URL
    onBack: () -> Unit
) {
    val lifecycleOwner = LocalLifecycleOwner.current

    BackHandler { onBack() }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Đang phát") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(
                            painter = painterResource(android.R.drawable.ic_media_previous),
                            contentDescription = null
                        )
                    }
                }
            )
        }
    ) { inner ->
        AndroidView(
            modifier = Modifier
                .fillMaxSize()
                .padding(inner),
            factory = { ctx ->
                YouTubePlayerView(ctx).apply {
                    // gắn lifecycle để tự pause/release
                    lifecycleOwner.lifecycle.addObserver(this)

                    addYouTubePlayerListener(object : AbstractYouTubePlayerListener() {
                        override fun onReady(player: YouTubePlayer) {
                            player.loadVideo(videoId, 0f)   // phát ngay từ đầu
                        }

                        override fun onError(
                            player: YouTubePlayer,
                            error: PlayerConstants.PlayerError
                        ) {
                            // Fallback mở app YouTube hoặc trình duyệt khi video bị chặn embed / lỗi 15
                            val app = Intent(
                                Intent.ACTION_VIEW,
                                Uri.parse("vnd.youtube:$videoId")
                            )
                            val web = Intent(
                                Intent.ACTION_VIEW,
                                Uri.parse("https://www.youtube.com/watch?v=$videoId")
                            )
                            try {
                                ctx.startActivity(app)
                            } catch (_: Exception) {
                                ctx.startActivity(web)
                            }
                        }
                    })
                }
            }
        )
    }
}
