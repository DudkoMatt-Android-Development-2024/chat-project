package com.github.dudkomatt.androidcourse.chatproject

import android.app.Application
import com.github.dudkomatt.androidcourse.chatproject.viewmodel.LoginViewModel
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

class ChatApplication : Application() {
    val viewModelModule = module {
        viewModel { LoginViewModel() }
//        viewModel { ChatViewModel() }
//        viewModel { NewChatViewModel() }
    }

    override fun onCreate() {
        super.onCreate()

        startKoin {
            androidContext(this@ChatApplication)
            modules(viewModelModule)
        }
    }
}
