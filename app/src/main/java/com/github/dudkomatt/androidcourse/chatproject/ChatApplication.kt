package com.github.dudkomatt.androidcourse.chatproject

import android.app.Application
import com.github.dudkomatt.androidcourse.chatproject.data.AppContainer

class ChatApplication : Application() {
    lateinit var container : AppContainer

    override fun onCreate() {
        super.onCreate()
        container = AppContainer()
    }
}
