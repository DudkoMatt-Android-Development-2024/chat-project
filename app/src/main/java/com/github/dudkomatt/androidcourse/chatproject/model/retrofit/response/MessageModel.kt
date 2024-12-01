package com.github.dudkomatt.androidcourse.chatproject.model.retrofit.response

import com.github.dudkomatt.androidcourse.chatproject.model.room.MessageEntity
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class MessageModel(
    val id: Int,
    val from: String,
    val to: String,
    val data: TextMessageInner,
    val time: Long,
) {
    @Serializable
    data class TextMessageInner(
        @SerialName("Text")
        val text: TextPayload? = null,
        @SerialName("Image")
        val image: ImagePayload? = null,
    )

    @Serializable
    data class TextPayload(
        val text: String
    )

    @Serializable
    data class ImagePayload(
        val link: String
    )
}

fun MessageModel.toMessageEntity(): MessageEntity = this.let {
    MessageEntity(
        id = it.id,
        from = it.from,
        to = it.to,
        text = it.data.text?.text,
        imageUrl = it.data.image?.link,
        time = it.time
    )
}
