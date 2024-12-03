package com.github.dudkomatt.androidcourse.chatproject.model.retrofit.request

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class TextMessageRequest(
    val from: String,
    val to: String,
    val data: TextMessageInner?,
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
