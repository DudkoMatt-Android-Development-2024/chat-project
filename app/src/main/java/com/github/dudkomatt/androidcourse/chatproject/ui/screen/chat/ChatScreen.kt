package com.github.dudkomatt.androidcourse.chatproject.ui.screen.chat

import android.content.res.Configuration
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.tooling.preview.Preview
import com.github.dudkomatt.androidcourse.chatproject.ui.screen.chat.horizontal.ChatScreenHorizontal
import com.github.dudkomatt.androidcourse.chatproject.ui.screen.chat.vertical.ChatScreenVertical
import com.github.dudkomatt.androidcourse.chatproject.viewmodel.ChatViewModel
import org.koin.androidx.compose.koinViewModel

@Composable
fun ChatScreen(
    username: String,
    onLogoutClick: () -> Unit,
) {
    val chatViewModel: ChatViewModel = koinViewModel()
    val uiState by chatViewModel.uiState.collectAsState()

    val selectedImageUrl = uiState.fullscreenImageUrl
    if (selectedImageUrl != null) {
        FullImageScreen(
            selectedImageUrl = selectedImageUrl,
            onBackClick = chatViewModel::resetFullScreenImage,
        )
    } else {
        val configuration = LocalConfiguration.current
        when (configuration.orientation) {
            Configuration.ORIENTATION_PORTRAIT -> {
                ChatScreenVertical(
                    modifier = Modifier.fillMaxSize(),
                    username = username,
                    onLogoutClick = onLogoutClick,
                )
            }
            else -> {
                ChatScreenHorizontal(
                    username = username,
                    onLogoutClick = onLogoutClick,
                )
            }
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
