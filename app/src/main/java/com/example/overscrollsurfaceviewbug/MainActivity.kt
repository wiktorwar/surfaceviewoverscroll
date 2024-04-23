package com.example.overscrollsurfaceviewbug

import android.os.Bundle
import android.view.LayoutInflater
import android.view.SurfaceView
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.media3.common.C.VIDEO_SCALING_MODE_SCALE_TO_FIT
import androidx.media3.common.C.VideoScalingMode
import androidx.media3.common.MediaItem
import androidx.media3.common.util.UnstableApi
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.ui.AspectRatioFrameLayout
import com.example.overscrollsurfaceviewbug.ui.theme.OverscrollSurfaceViewBugTheme

class MainActivity : ComponentActivity() {
    private var surfaceView: SurfaceView? = null
    private var player: ExoPlayer? = null

    @androidx.annotation.OptIn(UnstableApi::class)
    @OptIn(ExperimentalFoundationApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            OverscrollSurfaceViewBugTheme {
                val pagerState = rememberPagerState { 2 }
                // A surface container using the 'background' color from the theme
                HorizontalPager(state = pagerState, modifier = Modifier.fillMaxSize()) { index ->
                    if(index == 0 ) {
                        Video()
                    } else{
                        Text(index)
                    }

                }
            }
        }
    }

    @Composable
    @androidx.annotation.OptIn(UnstableApi::class)
    private fun Video() {
        AndroidView(factory = { context ->
            val aspectRatioFrameLayout = LayoutInflater.from(context)
                .inflate(R.layout.aspect_ratio_surface_view, null, false)

            val surfaceView =
                aspectRatioFrameLayout.findViewById<SurfaceView>(R.id.reddit_video_surface_view)

            val player = ExoPlayer.Builder(context).build()

            player.setVideoSurfaceView(surfaceView)

            this@MainActivity.surfaceView = surfaceView
            this@MainActivity.player = player
            aspectRatioFrameLayout
        },
            update = {
                player?.let { playBunny(it) }
            },
            modifier = Modifier.height(200.dp).fillMaxWidth(),
        )
    }

    override fun onDestroy() {
        player?.clearVideoSurfaceView(surfaceView)
        player?.release()
        super.onDestroy()
    }

    @Composable
    private fun Text(index: Int) {
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colorScheme.background
        ) {
            Greeting("Android from page ${index+1}")
        }
    }

    private fun playBunny(player: ExoPlayer) {
        val mediaItem =
            MediaItem.fromUri("http://commondatastorage.googleapis.com/gtv-videos-bucket/sample/BigBuckBunny.mp4")
        player.setMediaItem(mediaItem)
        player.prepare()
        player.play()
    }
}

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!",
        modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    OverscrollSurfaceViewBugTheme {
        Greeting("Android")
    }
}