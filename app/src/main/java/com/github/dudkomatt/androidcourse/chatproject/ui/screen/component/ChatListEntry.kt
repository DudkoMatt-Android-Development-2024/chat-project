package com.github.dudkomatt.androidcourse.chatproject.ui.screen.component

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.datasource.LoremIpsum
import androidx.compose.ui.unit.dp

@Composable
fun ChatListEntry(
    from: String = "From",  // TODO - Remove defaults
    lastMessage: String = "Last message",  // TODO - Remove defaults
    onImageClick: () -> Unit = {},  // TODO - Remove defaults
    onChatClick: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(2.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        ThumbProfileClickableImage(
            modifier = Modifier.padding(all = 2.dp),
            onImageClick = onImageClick
        )
        Column(
            modifier = Modifier
                .clickable {
                    onChatClick(from)
                },
            horizontalAlignment = Alignment.Start,
            verticalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            ChatTitle(
                from = from
            )
            LastMessageEntry(
                message = lastMessage
            )
        }
    }
}

@Composable
fun ChatTitle(
    from: String,
    modifier: Modifier = Modifier
) {
    Text(
        modifier = modifier,
        style = MaterialTheme.typography.titleMedium,
        text = from,
        textAlign = TextAlign.Start,
        maxLines = 1,
        overflow = TextOverflow.Ellipsis,
    )
}

@Composable
fun LastMessageEntry(
    message: String,
    modifier: Modifier = Modifier
) {
    Text(
        modifier = modifier,
        style = MaterialTheme.typography.bodyMedium,
        text = message,
        textAlign = TextAlign.Start,
        maxLines = 1,
        overflow = TextOverflow.Ellipsis,
    )
}

@Composable
@Preview(showBackground = true)
fun ChatListEntryPreview() {
    ChatListEntry(
        from = "From",
        lastMessage = "Last message",
        onImageClick = {},
        onChatClick = {}
    )
}

@Composable
@Preview(showBackground = true)
fun ChatListEntryLongLinesPreview() {
    val loremIpsum = LoremIpsum(500)
    ChatListEntry(
        from = loremIpsum.values.joinToString(),
        lastMessage = loremIpsum.values.joinToString(),
        onImageClick = {},
        onChatClick = {}
    )
}
