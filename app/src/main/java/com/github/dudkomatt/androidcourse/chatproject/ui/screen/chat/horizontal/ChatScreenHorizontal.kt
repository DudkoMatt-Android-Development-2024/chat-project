package com.github.dudkomatt.androidcourse.chatproject.ui.screen.chat.horizontal

import androidx.compose.foundation.layout.Row
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview

@Composable
fun ChatScreenHorizontal(
    createNewChannel: () -> Unit,
    createNewChat: () -> Unit,
) {
    Row {
        Text("This is the horizontal screen")
        // ... other content for landscape orientation
    }
}


@Composable
@Preview(device = "spec:parent=pixel_5,orientation=landscape")
fun ChatScreenHorizontalPreview() {
    ChatScreenHorizontal(
        createNewChannel = {},
        createNewChat = {}
    )
}
