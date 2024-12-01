package com.github.dudkomatt.androidcourse.chatproject.room

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.github.dudkomatt.androidcourse.chatproject.model.room.MessageEntity

@Dao
interface MessageDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(messages: List<MessageEntity>)

    @Query("SELECT * FROM messages WHERE `to` = :channelOrUsername AND id > :lastKnownId LIMIT :limit")
    suspend fun getBy(channelOrUsername: String, lastKnownId: Int, limit: Int): List<MessageEntity>

    @Query("DELETE FROM messages WHERE `to` = :channelOrUsername")
    suspend fun deleteAllBy(channelOrUsername: String)
}
