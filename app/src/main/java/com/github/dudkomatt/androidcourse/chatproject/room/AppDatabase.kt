package com.github.dudkomatt.androidcourse.chatproject.room

import androidx.room.Database
import androidx.room.RoomDatabase
import com.github.dudkomatt.androidcourse.chatproject.model.room.ChatEntity
import com.github.dudkomatt.androidcourse.chatproject.model.room.InboxEntity
import com.github.dudkomatt.androidcourse.chatproject.model.room.MessageEntity

@Database(
    entities = [ChatEntity::class, MessageEntity::class, InboxEntity::class],
    version = 1
)
abstract class AppDatabase: RoomDatabase() {
    abstract fun chatDao(): ChatDao
    abstract fun messageDao(): MessageDao
    abstract fun inboxDao(): InboxDao
}
