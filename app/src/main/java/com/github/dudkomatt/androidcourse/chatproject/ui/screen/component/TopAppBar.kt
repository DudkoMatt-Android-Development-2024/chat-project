package com.github.dudkomatt.androidcourse.chatproject.ui.screen.component

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

@Composable
fun TopAppBar(
    modifier: Modifier = Modifier,
    contents: @Composable (Dp) -> Unit
) {
    val topBarHeight = 56.dp
    Surface(
        modifier = modifier
            .fillMaxWidth()
            .height(topBarHeight),
        tonalElevation = 72.dp
    ) {
        contents(topBarHeight)
    }
}
