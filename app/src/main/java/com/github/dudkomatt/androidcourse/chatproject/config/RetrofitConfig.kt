package com.github.dudkomatt.androidcourse.chatproject.config

import com.github.dudkomatt.androidcourse.chatproject.network.AuthApi
import com.github.dudkomatt.androidcourse.chatproject.network.InfoApi
import com.github.dudkomatt.androidcourse.chatproject.network.MessageApi
import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit


object RetrofitConfig {
    const val X_AUTH_TOKEN_HEADER = "X-Auth-Token"

    const val BASE_URL = "https://faerytea.name:8008"

    private fun getOkHttpClient(): OkHttpClient {
        val loggingInterceptor = HttpLoggingInterceptor()
        loggingInterceptor.level = HttpLoggingInterceptor.Level.BASIC

        return OkHttpClient.Builder()
            .addInterceptor(loggingInterceptor)
            .build()
    }

    private val retrofit: Retrofit = Retrofit.Builder()
        .addConverterFactory(Json.asConverterFactory("application/json".toMediaType()))
        .baseUrl(BASE_URL)
        .client(getOkHttpClient())
        .build()

    val authRetrofitApi: AuthApi by lazy {
        retrofit.create(AuthApi::class.java)
    }

    val infoRetrofitApi: InfoApi by lazy {
        retrofit.create(InfoApi::class.java)
    }

    val messageRetrofitApi: MessageApi by lazy {
        retrofit.create(MessageApi::class.java)
    }
}
