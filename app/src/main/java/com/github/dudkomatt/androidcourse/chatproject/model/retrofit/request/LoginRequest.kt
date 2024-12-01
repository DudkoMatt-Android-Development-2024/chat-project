package com.github.dudkomatt.androidcourse.chatproject.model.retrofit.request

import kotlinx.serialization.Serializable

@Serializable
data class LoginRequest(
    val name: String,
    val pwd: String,
)
