package com.github.dudkomatt.androidcourse.chatproject.network

import com.github.dudkomatt.androidcourse.chatproject.config.RetrofitConfig
import com.github.dudkomatt.androidcourse.chatproject.model.retrofit.request.TextMessageRequest
import com.github.dudkomatt.androidcourse.chatproject.model.retrofit.response.MessageModel
import okhttp3.MultipartBody
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part
import retrofit2.http.Path
import retrofit2.http.Query

interface MessageApi {

    // Receive data - GET
    // ==================

    @GET("/1ch")
    suspend fun getFrom1ch(@Query("limit") limit: Int? = null, @Query("lastKnownId") lastKnownId: Int? = null, @Query("reverse") reverse: Boolean? = null): List<MessageModel>

    @GET("/channel/{channel_name}")
    suspend fun getFromChannel(@Path("channel_name") channelName: String, @Query("limit") limit: Int? = null, @Query("lastKnownId") lastKnownId: Int? = null, @Query("reverse") reverse: Boolean? = null): List<MessageModel>

    @GET("/inbox/{username}")
    suspend fun getUserInbox(@Header(RetrofitConfig.X_AUTH_TOKEN_HEADER) token: String, @Path("username") username: String, @Query("limit") limit: Int? = null, @Query("lastKnownId") lastKnownId: Int? = null, @Query("reverse") reverse: Boolean? = null): List<MessageModel>

    // Send data - POST
    // ================

    @POST("/1ch")
    suspend fun postTo1ch(@Header(RetrofitConfig.X_AUTH_TOKEN_HEADER) token: String, @Body textMessage: TextMessageRequest): Int

    @POST("/messages")
    suspend fun postMessage(@Header(RetrofitConfig.X_AUTH_TOKEN_HEADER) token: String, @Body textMessage: TextMessageRequest): Int

    @Multipart
    @POST("/1ch")
    suspend fun postTo1ch(@Header(RetrofitConfig.X_AUTH_TOKEN_HEADER) token: String, @Part("msg") textMessage: TextMessageRequest, @Part image: MultipartBody.Part): Int

    @Multipart
    @POST("/messages")
    suspend fun postMessage(@Header(RetrofitConfig.X_AUTH_TOKEN_HEADER) token: String, @Part("msg") textMessage: TextMessageRequest, @Part image: MultipartBody.Part): Int
}
