package com.github.dudkomatt.androidcourse.chatproject.config

import com.github.dudkomatt.androidcourse.chatproject.config.RetrofitConfig.BASE_URL

object ImageConfig {
    const val THUMB_IMAGE_URL_PREFIX = "thumb"
    const val FULL_IMAGE_URL_PREFIX = "img"

    fun getThumbImageUrl(imageUrl: String): String {
        return "$BASE_URL/$THUMB_IMAGE_URL_PREFIX/$imageUrl"
    }

    fun getFullImageUrl(imageUrl: String): String {
        return "$BASE_URL/$FULL_IMAGE_URL_PREFIX/$imageUrl"
    }
}
