package com.github.dudkomatt.androidcourse.chatproject.ui.screen.component

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.ArrowBack
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.github.dudkomatt.androidcourse.chatproject.R

@Composable
fun ReturnBackTopBarButton(
    onBackClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    IconButton(
        modifier = modifier,
        onClick = onBackClick
    ) {
        Icon(
            imageVector = Icons.AutoMirrored.Outlined.ArrowBack,
            contentDescription = stringResource(id = R.string.back_button_content_description),
            tint = MaterialTheme.colorScheme.onSurface
        )
    }
}
