package com.example.grama_vaxihealthcare.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.example.grama_vaxihealthcare.data.entity.ChatHistory
import kotlinx.coroutines.flow.Flow

@Dao
interface ChatHistoryDao {
    @Query("SELECT * FROM chat_history ORDER BY timestamp ASC")
    fun getAllChats(): Flow<List<ChatHistory>>

    @Insert
    suspend fun insertChat(chat: ChatHistory)

    @Query("DELETE FROM chat_history")
    suspend fun clearHistory()
}
