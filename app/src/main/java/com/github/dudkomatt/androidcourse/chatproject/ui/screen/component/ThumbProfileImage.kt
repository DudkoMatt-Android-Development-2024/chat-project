package com.github.dudkomatt.androidcourse.chatproject.ui.screen.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.requiredSize
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.github.dudkomatt.androidcourse.chatproject.R

@Composable
fun ThumbProfileClickableImage(
    onImageClick: () -> Unit = {},  // TODO - Remove defaults
    drawableResource: Int = R.drawable.ic_launcher_foreground,  // TODO - Remove defaults
    description: String = "Description",  // TODO - Remove defaults
    modifier: Modifier = Modifier,
) {
    IconButton(
        modifier = modifier,
        onClick = onImageClick
    ) {
        ThumbProfileImage(
            drawableResource,
            description
        )
    }
}

@Composable
fun ThumbProfileImage(
    drawableResource: Int = R.drawable.ic_launcher_foreground,  // TODO - Remove defaults
    description: String = "Description",  // TODO - Remove defaults
    modifier: Modifier = Modifier
) {
    Image(
        modifier = modifier
            .requiredSize(40.dp)
            .clip(CircleShape),
        alignment = Alignment.Center,
        painter = painterResource(id = drawableResource),  // TODO - Use Glide
        contentDescription = description,
    )
}

@Composable
@Preview
fun ThumbProfileClickableImagePreview() {
    ThumbProfileClickableImage(
        onImageClick = {},
        drawableResource = R.drawable.ic_launcher_foreground,
        description = "Description"
    )
}
