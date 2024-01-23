package com.rbrauwers.newsapp.database.dao

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Upsert
import com.rbrauwers.newsapp.database.model.ArticleEntity
import com.rbrauwers.newsapp.database.model.ArticleEntityUpsertWrapper
import kotlinx.coroutines.flow.Flow

interface HeadlineDao {
    fun getHeadlines(): Flow<List<ArticleEntity>>
    fun pagingSource(): PagingSource<Int, ArticleEntity>
    suspend fun updateLiked(id: Int, liked: Boolean)
    suspend fun updateLikes(likes: Map<Int, Boolean>)
    suspend fun upsertHeadlines(headlines: List<ArticleEntity>)
    suspend fun clear()
    suspend fun clearAndInsert(articles: List<ArticleEntity>)
}

@Dao
internal abstract class DefaultHeadlineDao : HeadlineDao {

    @Query(
        value = """
            SELECT * FROM articles 
            ORDER BY published_at DESC
        """
    )
    abstract override fun getHeadlines(): Flow<List<ArticleEntity>>

    @Query(
        value = """
            SELECT * FROM articles
            ORDER BY published_at DESC
        """
    )
    abstract override fun pagingSource(): PagingSource<Int, ArticleEntity>

    @Query(
        value = """
            UPDATE articles 
            SET liked = :liked 
            WHERE id = :id
        """)
    abstract override suspend fun updateLiked(id: Int, liked: Boolean)

    @Transaction
    open override suspend fun updateLikes(likes: Map<Int, Boolean>) {
        likes.forEach {
            updateLiked(id = it.key, liked = it.value)
        }
    }

    override suspend fun upsertHeadlines(headlines: List<ArticleEntity>) {
        upsertHeadlines(headlines.map { ArticleEntityUpsertWrapper(it) })
    }

    @Query("DELETE FROM articles")
    abstract override suspend fun clear()

    @Transaction
    override suspend fun clearAndInsert(articles: List<ArticleEntity>) {
        clear()
        upsertHeadlines(headlines = articles)
    }

    @Upsert(entity = ArticleEntity::class)
    internal abstract suspend fun upsertHeadlines(headlines: List<ArticleEntityUpsertWrapper>)

}