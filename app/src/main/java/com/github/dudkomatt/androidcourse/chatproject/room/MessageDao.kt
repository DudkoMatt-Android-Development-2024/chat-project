package com.github.dudkomatt.androidcourse.chatproject.room

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.github.dudkomatt.androidcourse.chatproject.model.room.MessageEntity

@Dao
interface MessageDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(messages: List<MessageEntity>)

    @Query("SELECT * FROM messages WHERE `to` = :channelOrUsername")
    fun getBy(channelOrUsername: String): PagingSource<Int, MessageEntity>

    @Query("SELECT id FROM messages WHERE `to` = :channelOrUsername ORDER BY id DESC LIMIT 1")
    suspend fun getMax(channelOrUsername: String): Int

    @Query("DELETE FROM messages WHERE `to` = :channelOrUsername")
    suspend fun deleteAllBy(channelOrUsername: String)
}
