package com.github.dudkomatt.androidcourse.chatproject.ui.screen.component

import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector

@Composable
fun IconButtonWithCallback(
    onImageClick: () -> Unit,
    imageVector: ImageVector,
    contentDescription: String?,
    enabled: Boolean,
    tint: Color = MaterialTheme.colorScheme.onSurface,
    modifier: Modifier = Modifier
) {
    IconButton(
        modifier = modifier,
        onClick = onImageClick,
        enabled = enabled
    ) {
        Icon(
            imageVector = imageVector,
            contentDescription = contentDescription,
            tint = tint
        )
    }
}
