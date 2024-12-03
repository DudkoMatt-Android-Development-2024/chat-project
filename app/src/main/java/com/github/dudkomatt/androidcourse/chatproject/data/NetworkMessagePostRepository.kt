package com.github.dudkomatt.androidcourse.chatproject.data

import androidx.room.withTransaction
import com.github.dudkomatt.androidcourse.chatproject.model.retrofit.request.TextMessageRequest
import com.github.dudkomatt.androidcourse.chatproject.model.room.MessageEntity
import com.github.dudkomatt.androidcourse.chatproject.network.MessageApi
import com.github.dudkomatt.androidcourse.chatproject.room.AppDatabase
import okhttp3.RequestBody

class NetworkMessagePostRepository(
    private val retrofitMessageApi: MessageApi,
    private val userSessionRepository: UserSessionRepository,
    private val database: AppDatabase
) {
    private val messageDao = database.messageDao()

    suspend fun postMessage(
        textMessage: TextMessageRequest,
    ): Int? {
        val token = userSessionRepository.getToken() ?: return null
        val response = retrofitMessageApi.postMessage(
            token = token,
            textMessage = textMessage
        )

        database.withTransaction {
            messageDao.insertAll(
                listOf(
                    MessageEntity(
                        id = response,
                        from = textMessage.from,
                        to = textMessage.to,
                        imageUrl = null,
                        text = textMessage.data.text?.text,
                        time = System.currentTimeMillis()
                    )
                )
            )
        }

        return response
    }

    suspend fun postMessage(
        textMessage: TextMessageRequest,
        image: RequestBody,
    ): Int? {
        val token = userSessionRepository.getToken() ?: return null
        val response = retrofitMessageApi.postMessage(
            token = token,
            textMessage = textMessage,
            image = image
        )

        database.withTransaction {
            messageDao.insertAll(
                listOf(
                    MessageEntity(
                        id = response,
                        from = textMessage.from,
                        to = textMessage.to,
                        imageUrl = null,
                        text = textMessage.data.text?.text + " <THIS MESSAGE CONTAINS AN IMAGE - REFRESH CHAT TO VIEW IT>",
                        time = System.currentTimeMillis()
                    )
                )
            )
        }

        return response
    }
}
