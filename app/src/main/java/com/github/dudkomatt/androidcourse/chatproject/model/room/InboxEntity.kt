package com.github.dudkomatt.androidcourse.chatproject.model.room

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "inbox")
data class InboxEntity (
    @PrimaryKey
    val from: String
)
