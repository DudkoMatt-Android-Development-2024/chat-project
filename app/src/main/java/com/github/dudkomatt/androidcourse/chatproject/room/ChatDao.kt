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

    @Query("SELECT * FROM chats")
    suspend fun getAll(): List<ChatEntity>

    @Query("DELETE FROM chats")
    suspend fun clearAll()
}
