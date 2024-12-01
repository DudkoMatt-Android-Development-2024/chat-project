package com.github.dudkomatt.androidcourse.chatproject.config

import android.content.Context
import androidx.room.Room
import com.github.dudkomatt.androidcourse.chatproject.room.AppDatabase

object RoomConfigs {
    private const val DATABASE_NAME = "appDb"

    fun createRoomDb(applicationContext: Context): AppDatabase {
        return Room.databaseBuilder(
            applicationContext,
            AppDatabase::class.java,
            DATABASE_NAME
        ).build()
    }
}
