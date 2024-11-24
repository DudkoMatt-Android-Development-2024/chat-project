package com.github.dudkomatt.androidcourse.chatproject.network

import com.github.dudkomatt.androidcourse.chatproject.model.request.LoginRequest
import okhttp3.ResponseBody
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST

interface AuthApi {
    @POST("/addusr")
    @FormUrlEncoded
    suspend fun registerNewUser(@Field("name") name: String): Response<ResponseBody>

    @POST("/login")
    suspend fun loginPost(@Body loginRequest: LoginRequest): Response<ResponseBody>

    // TODO - Auth header
    @POST("/logout")
    suspend fun logoutPost()
}
