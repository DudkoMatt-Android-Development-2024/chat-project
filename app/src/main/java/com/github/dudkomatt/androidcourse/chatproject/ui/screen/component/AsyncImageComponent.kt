package com.github.dudkomatt.androidcourse.chatproject.ui.screen.component

import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import coil3.SingletonImageLoader
import coil3.compose.SubcomposeAsyncImage
import com.github.dudkomatt.androidcourse.chatproject.config.ImageConfig

@Composable
fun AsyncImageComponent(
    imageUrl: String,
    isThumb: Boolean,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    SubcomposeAsyncImage(
        modifier = modifier,
        loading = {
            CircularProgressIndicator(
                color = MaterialTheme.colorScheme.onPrimary,
            )
        },
        model = if (isThumb) ImageConfig.getThumbImageUrl(imageUrl)
        else ImageConfig.getFullImageUrl(imageUrl),
        contentDescription = null,
        imageLoader = SingletonImageLoader.get(context)
    )
}
