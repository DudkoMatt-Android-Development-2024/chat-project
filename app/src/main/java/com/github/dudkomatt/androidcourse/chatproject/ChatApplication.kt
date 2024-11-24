package com.github.dudkomatt.androidcourse.chatproject

import android.app.Application
import com.github.dudkomatt.androidcourse.chatproject.data.AppConfigs
import com.github.dudkomatt.androidcourse.chatproject.data.SessionTokenManager
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
            single { SessionTokenManager(androidContext()) }
            single { AppConfigs.authRetrofitApi }
            single { AppConfigs.imageRetrofitApi }
            single { AppConfigs.infoRetrofitApi }
            single { AppConfigs.messageRetrofitApi }

            viewModelOf(::RootViewModel)
            viewModelOf(::LoginViewModel)
//        viewModel { ChatViewModel() }
//        viewModel { NewChatViewModel() }
        }

        startKoin {
            androidLogger(Level.ERROR)
            androidContext(this@ChatApplication)
            modules(viewModelModule)
        }
    }
}
