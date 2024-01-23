package com.rbrauwers.newsapp.database.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import com.rbrauwers.newsapp.database.model.NewsSourceEntity
import kotlinx.coroutines.flow.Flow

interface SourceDao {
    fun getSources(): Flow<List<NewsSourceEntity>>
    fun getSource(id: String): Flow<NewsSourceEntity?>
    suspend fun upsertSources(sources: List<NewsSourceEntity>)
}

@Dao
internal interface DefaultSourceDao : SourceDao {

    @Query("SELECT * FROM news_sources")
    override fun getSources(): Flow<List<NewsSourceEntity>>

    @Query("SELECT * FROM news_sources WHERE id = :id")
    override fun getSource(id: String): Flow<NewsSourceEntity?>

    @Upsert
    override suspend fun upsertSources(sources: List<NewsSourceEntity>)

}