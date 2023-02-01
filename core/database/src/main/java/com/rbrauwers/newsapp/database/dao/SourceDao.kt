package com.rbrauwers.newsapp.database.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import com.rbrauwers.newsapp.database.model.NewsSourceEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface SourceDao {

    @Query("SELECT * FROM news_sources")
    fun getSources(): Flow<List<NewsSourceEntity>>

    @Upsert
    suspend fun upsertSources(sources: List<NewsSourceEntity>)

}