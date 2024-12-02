package com.github.dudkomatt.androidcourse.chatproject

import android.app.Application
import coil3.ImageLoader
import coil3.SingletonImageLoader
import coil3.disk.DiskCache
import coil3.disk.directory
import coil3.memory.MemoryCache
import coil3.request.crossfade
import com.github.dudkomatt.androidcourse.chatproject.config.RetrofitConfig
import com.github.dudkomatt.androidcourse.chatproject.config.RoomConfigs
import com.github.dudkomatt.androidcourse.chatproject.data.NetworkMessagePostRepository
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
            single { RetrofitConfig.authRetrofitApi }
            single { RetrofitConfig.infoRetrofitApi }
            single { RetrofitConfig.messageRetrofitApi }

            // Room
            single { RoomConfigs.createRoomDb(androidContext()) }

            // Repository
            single { NetworkMessagePostRepository(get(), get(), get()) }

            // ViewModels
            viewModelOf(::RootViewModel)
            viewModelOf(::LoginViewModel)
            viewModelOf(::ChatViewModel)
        }

        // Coin
        SingletonImageLoader.setSafe {
            ImageLoader.Builder(this@ChatApplication)
                .memoryCache {
                    MemoryCache.Builder()
                        .maxSizePercent(this@ChatApplication, 0.25)
                        .build()
                }
                .diskCache {
                    DiskCache.Builder()
                        .directory(this@ChatApplication.cacheDir.resolve("image_cache"))
                        .maxSizePercent(0.02)
                        .build()
                }
                .crossfade(true)
                .build()
        }

        startKoin {
            androidLogger(Level.ERROR)
            androidContext(this@ChatApplication)
            modules(viewModelModule)
        }
    }
}
