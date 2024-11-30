package com.github.dudkomatt.androidcourse.chatproject.data

import com.github.dudkomatt.androidcourse.chatproject.network.AuthApi
import com.github.dudkomatt.androidcourse.chatproject.network.ImageApi
import com.github.dudkomatt.androidcourse.chatproject.network.InfoApi
import com.github.dudkomatt.androidcourse.chatproject.network.MessageApi
import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import retrofit2.Retrofit

object RetrofitConfigs {
    const val X_AUTH_TOKEN_HEADER = "X-Auth-Token"

    private const val BASE_URL = "https://faerytea.name:8008"

    private val retrofit: Retrofit = Retrofit.Builder()
        .addConverterFactory(Json.asConverterFactory("application/json".toMediaType()))
        .baseUrl(BASE_URL)
        .build()

    val authRetrofitApi: AuthApi by lazy {
        retrofit.create(AuthApi::class.java)
    }

    val imageRetrofitApi: ImageApi by lazy {
        retrofit.create(ImageApi::class.java)
    }

    val infoRetrofitApi: InfoApi by lazy {
        retrofit.create(InfoApi::class.java)
    }

    val messageRetrofitApi: MessageApi by lazy {
        retrofit.create(MessageApi::class.java)
    }
}
