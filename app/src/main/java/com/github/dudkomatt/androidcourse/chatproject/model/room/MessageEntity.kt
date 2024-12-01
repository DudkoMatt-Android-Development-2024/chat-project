package com.github.dudkomatt.androidcourse.chatproject.model.room

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.github.dudkomatt.androidcourse.chatproject.model.retrofit.response.MessageModel
import com.github.dudkomatt.androidcourse.chatproject.model.retrofit.response.MessageModel.TextPayload

@Entity(tableName = "messages")
data class MessageEntity(
    @PrimaryKey
    val id: Int,
    val from: String,
    val to: String,
    val text: String?,
    val imageUrl: String?,
    val time: Long,
)

fun MessageEntity.toMessageModel(): MessageModel = this.let {
    MessageModel(
        id = it.id,
        from = it.from,
        to = it.to,
        data = MessageModel.TextMessageInner(
            text = it.text?.let { it1 -> TextPayload(it1) },
            image = it.imageUrl?.let { it2 -> MessageModel.ImagePayload(it2) },
        ),
        time = time
    )
}
