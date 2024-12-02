package com.github.dudkomatt.androidcourse.chatproject.ui.screen.chat

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.github.dudkomatt.androidcourse.chatproject.ui.screen.component.AsyncImageComponent

@Composable
fun FullImageScreen(
    selectedImageUrl: String,
    onBackClick: () -> Unit
) {
    BackHandler {
        onBackClick()
    }

    AsyncImageComponent(
        modifier = Modifier.fillMaxSize()
            .padding(32.dp),
        imageUrl = selectedImageUrl,
        isThumb = false
    )
}
