package com.github.dudkomatt.androidcourse.chatproject.model.room

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "chats")
data class ChatEntity(
    @PrimaryKey
    val from: String,
)
