package com.github.dudkomatt.androidcourse.chatproject.room

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.github.dudkomatt.androidcourse.chatproject.model.room.InboxEntity

@Dao
interface InboxDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(inboxUsers: List<InboxEntity>)

    @Query("SELECT `from` FROM inbox")
    suspend fun getAll(): List<String>

    @Query("DELETE FROM inbox")
    suspend fun deleteAll()
}
