package com.github.dudkomatt.androidcourse.chatproject.ui.screen.chat

import android.content.res.Configuration
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.tooling.preview.Preview
import com.github.dudkomatt.androidcourse.chatproject.ui.screen.chat.horizontal.ChatScreenHorizontal
import com.github.dudkomatt.androidcourse.chatproject.ui.screen.chat.vertical.ChatScreenVertical

@Composable
fun ChatScreen(
    createNewChatOrChannelFunction: () -> Unit,
) {
    val configuration = LocalConfiguration.current

    when (configuration.orientation) {
        Configuration.ORIENTATION_LANDSCAPE -> {
            ChatScreenHorizontal(createNewChatOrChannelFunction)
        }
        else -> {
            ChatScreenVertical(createNewChatOrChannelFunction)
        }
    }
}

@Composable
@Preview(showBackground = true)
fun ChatScreenPortraitPreview() {
    ChatScreen(
        createNewChatOrChannelFunction = {}
    )
}

@Composable
@Preview(showBackground = true, device = "spec:parent=pixel_5,orientation=landscape")
fun ChatScreenLandscapePreview() {
    ChatScreen(
        createNewChatOrChannelFunction = {}
    )
}
