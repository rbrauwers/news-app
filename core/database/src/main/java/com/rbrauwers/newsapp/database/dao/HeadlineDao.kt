package com.rbrauwers.newsapp.database.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import com.rbrauwers.newsapp.database.model.ArticleEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface HeadlineDao {

    @Query("SELECT * FROM articles ORDER BY published_at DESC")
    fun getHeadlines(): Flow<List<ArticleEntity>>

    @Upsert
    suspend fun upsertHeadlines(headlines: List<ArticleEntity>)

}