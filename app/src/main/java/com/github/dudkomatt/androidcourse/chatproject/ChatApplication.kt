package com.github.dudkomatt.androidcourse.chatproject

import android.app.Application
import com.github.dudkomatt.androidcourse.chatproject.data.RetrofitConfigs
import com.github.dudkomatt.androidcourse.chatproject.data.RoomConfigs
import com.github.dudkomatt.androidcourse.chatproject.data.UserSessionRepository
import com.github.dudkomatt.androidcourse.chatproject.viewmodel.ChatViewModel
import com.github.dudkomatt.androidcourse.chatproject.viewmodel.RootViewModel
import com.github.dudkomatt.androidcourse.chatproject.viewmodel.LoginViewModel
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin
import org.koin.core.logger.Level
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

class ChatApplication : Application() {
    override fun onCreate() {
        super.onCreate()

        val viewModelModule = module {
            // Session token storage
            single { UserSessionRepository(androidContext()) }

            // Retrofit
            single { RetrofitConfigs.authRetrofitApi }
            single { RetrofitConfigs.imageRetrofitApi }
            single { RetrofitConfigs.infoRetrofitApi }
            single { RetrofitConfigs.messageRetrofitApi }

            // Room
            single { RoomConfigs.createRoomDb(androidContext()) }

            // ViewModels
            viewModelOf(::RootViewModel)
            viewModelOf(::LoginViewModel)
            viewModelOf(::ChatViewModel)
        }

        startKoin {
            androidLogger(Level.ERROR)
            androidContext(this@ChatApplication)
            modules(viewModelModule)
        }
    }
}
