package com.github.dudkomatt.androidcourse.chatproject.ui.screen.chat.horizontal

import androidx.compose.foundation.layout.Row
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable

@Composable
fun ChatScreenHorizontal(
    createNewChatOrChannelFunction: () -> Unit,
) {
    Row {
        Text("This is the horizontal screen")
        // ... other content for landscape orientation
    }
}
