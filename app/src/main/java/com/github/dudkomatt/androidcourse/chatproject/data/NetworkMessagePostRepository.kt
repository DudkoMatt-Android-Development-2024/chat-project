package com.github.dudkomatt.androidcourse.chatproject.data

import android.app.Application
import androidx.room.withTransaction
import com.github.dudkomatt.androidcourse.chatproject.R
import com.github.dudkomatt.androidcourse.chatproject.model.retrofit.request.TextMessageRequest
import com.github.dudkomatt.androidcourse.chatproject.model.room.MessageEntity
import com.github.dudkomatt.androidcourse.chatproject.network.MessageApi
import com.github.dudkomatt.androidcourse.chatproject.room.AppDatabase
import okhttp3.RequestBody

class NetworkMessagePostRepository(
    private val application: Application,
    private val retrofitMessageApi: MessageApi,
    private val dataStorePreferencesRepository: DataStorePreferencesRepository,
    private val database: AppDatabase
) {
    private val messageDao = database.messageDao()

    suspend fun postMessage(
        textMessage: TextMessageRequest,
    ): Int? {
        val token = dataStorePreferencesRepository.getToken() ?: return null
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
        val token = dataStorePreferencesRepository.getToken() ?: return null
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
                        text = textMessage.data.text?.text + application.applicationContext.getString(R.string.this_message_contains_an_image_refresh_chat_to_view_it),
                        time = System.currentTimeMillis()
                    )
                )
            )
        }

        return response
    }
}
