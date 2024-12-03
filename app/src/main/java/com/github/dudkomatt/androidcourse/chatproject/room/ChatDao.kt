package com.github.dudkomatt.androidcourse.chatproject.room

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.github.dudkomatt.androidcourse.chatproject.model.room.ChatEntity

@Dao
interface ChatDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(chats: List<ChatEntity>)

    @Query("SELECT `from` FROM chats WHERE NOT isChannel")
    suspend fun getRegisteredUsers(): List<String>

    @Query("SELECT `from` FROM chats WHERE isChannel")
    suspend fun getAllChannels(): List<String>

    @Query("DELETE FROM chats WHERE NOT isChannel")
    suspend fun clearAllUsers()

    @Query("DELETE FROM chats WHERE isChannel")
    suspend fun clearAllChannels()
}
