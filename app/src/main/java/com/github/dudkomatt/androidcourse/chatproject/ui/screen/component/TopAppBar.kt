package com.github.dudkomatt.androidcourse.chatproject.ui.screen.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.github.dudkomatt.androidcourse.chatproject.R

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
        tonalElevation = 16.dp
    ) {
        contents(topBarHeight)
    }
}
