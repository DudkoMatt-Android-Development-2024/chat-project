package com.github.dudkomatt.androidcourse.chatproject.ui.screen.chat

import android.content.res.Configuration
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalConfiguration
import com.github.dudkomatt.androidcourse.chatproject.ui.screen.chat.horizontal.ChatScreenHorizontal
import com.github.dudkomatt.androidcourse.chatproject.ui.screen.chat.vertical.ChatScreenVertical

@Composable
fun ChatScreen() {
    val configuration = LocalConfiguration.current

    when (configuration.orientation) {
        Configuration.ORIENTATION_LANDSCAPE -> {
            ChatScreenHorizontal()
        }
        else -> {
            ChatScreenVertical()
        }
    }
}
