package com.github.dudkomatt.androidcourse.chatproject.ui.screen.component

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun TopBarText(
    text: String
) {
    Text(
        modifier = Modifier.padding(12.dp),
        text = text,
        style = MaterialTheme.typography.titleLarge
    )
}
