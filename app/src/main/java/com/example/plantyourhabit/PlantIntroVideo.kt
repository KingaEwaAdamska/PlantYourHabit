package com.example.plantyourhabit

import android.net.Uri
import android.util.Log
import android.widget.VideoView
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView

@Composable
fun PlantIntroVideo(onFinished: () -> Unit) {
    val context = LocalContext.current
    val latestOnFinished = rememberUpdatedState(onFinished)
    val videoView = remember(context) {
        VideoView(context).apply {
            setOnCompletionListener { latestOnFinished.value() }
            setOnErrorListener { _, what, extra ->
                Log.e("PlantYourHabit", "Intro video playback failed (what=$what, extra=$extra)")
                latestOnFinished.value()
                true
            }
            setOnPreparedListener { player -> player.setVolume(0f, 0f) }
            setVideoURI(
                Uri.parse("android.resource://${context.packageName}/${R.raw.plant_intro}")
            )
            start()
        }
    }

    DisposableEffect(videoView) {
        onDispose { videoView.stopPlayback() }
    }

    BoxWithConstraints(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black)
    ) {
        val videoSize = minOf(maxWidth, maxHeight)
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            AndroidView(
                factory = { videoView },
                modifier = Modifier.size(videoSize)
            )
        }
        TextButton(
            onClick = onFinished,
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(24.dp)
        ) {
            Text("Skip intro", color = Color.White)
        }
    }
}
