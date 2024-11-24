package com.github.dudkomatt.androidcourse.chatproject.ui.screen.chat

import android.content.res.Configuration
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.tooling.preview.Preview
import com.github.dudkomatt.androidcourse.chatproject.ui.screen.chat.horizontal.ChatScreenHorizontal
import com.github.dudkomatt.androidcourse.chatproject.ui.screen.chat.vertical.ChatScreenVertical

@Composable
fun ChatScreen(
    username: String,
    onLogoutClick: () -> Unit,
) {
    val configuration = LocalConfiguration.current

    when (configuration.orientation) {
        Configuration.ORIENTATION_LANDSCAPE -> {
            ChatScreenHorizontal(
                username = username,
            )
        }
        else -> {
            ChatScreenVertical(
                username = username,
                onLogoutClick = onLogoutClick,
            )
        }
    }
}

@Composable
@Preview(showBackground = true)
fun ChatScreenPortraitPreview() {
    ChatScreen(
        username = "Username",
        onLogoutClick = {},
    )
}

@Composable
@Preview(showBackground = true, device = "spec:parent=pixel_5,orientation=landscape")
fun ChatScreenLandscapePreview() {
    ChatScreen(
        username = "Username",
        onLogoutClick = {},
    )
}
