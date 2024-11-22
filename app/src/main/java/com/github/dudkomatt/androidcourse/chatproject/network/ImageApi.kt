package com.github.dudkomatt.androidcourse.chatproject.network

import okhttp3.ResponseBody
import retrofit2.http.GET
import retrofit2.http.Path

// TODO
interface ImageApi {
    // TODO - Glide & plain http client
    @GET("/img/{image_path}")
    suspend fun getImageFull(@Path("image_path") imagePath: String): ResponseBody

    // TODO - Glide & plain http client
    @GET("/thumb/{image_path}")
    suspend fun getImageThumb(@Path("image_path") imagePath: String): ResponseBody
}
