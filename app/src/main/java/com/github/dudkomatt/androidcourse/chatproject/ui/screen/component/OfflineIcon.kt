package com.github.dudkomatt.androidcourse.chatproject.ui.screen.component

import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CloudOff
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.github.dudkomatt.androidcourse.chatproject.R

@Composable
fun OfflineIcon(modifier: Modifier = Modifier) {
    Icon(
        modifier = modifier
            .size(24.dp),
        imageVector = Icons.Default.CloudOff,
        contentDescription = stringResource(R.string.offline),
        tint = MaterialTheme.colorScheme.onSurface
    )
}
