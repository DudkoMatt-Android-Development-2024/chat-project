package com.github.dudkomatt.androidcourse.chatproject.ui.screen.chat

import android.content.res.Configuration
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.tooling.preview.Preview
import com.github.dudkomatt.androidcourse.chatproject.ui.screen.chat.horizontal.ChatScreenHorizontal
import com.github.dudkomatt.androidcourse.chatproject.ui.screen.chat.vertical.ChatScreenVertical

@Composable
fun ChatScreen(
    onLogoutClick: () -> Unit,
    createNewChannel: () -> Unit,
    createNewChat: () -> Unit,
) {
    val configuration = LocalConfiguration.current

    when (configuration.orientation) {
        Configuration.ORIENTATION_LANDSCAPE -> {
            ChatScreenHorizontal(
                createNewChannel = createNewChannel,
                createNewChat = createNewChat,
            )
        }

        else -> {
            ChatScreenVertical(
                onLogoutClick = onLogoutClick,
                createNewChannel = createNewChannel,
                createNewChat = createNewChat,
            )
        }
    }
}

@Composable
@Preview(showBackground = true)
fun ChatScreenPortraitPreview() {
    ChatScreen(
        onLogoutClick = {},
        createNewChannel = {},
        createNewChat = {},
    )
}

@Composable
@Preview(showBackground = true, device = "spec:parent=pixel_5,orientation=landscape")
fun ChatScreenLandscapePreview() {
    ChatScreen(
        onLogoutClick = {},
        createNewChannel = {},
        createNewChat = {},
    )
}
