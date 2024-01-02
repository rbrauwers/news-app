package com.rbrauwers.newsapp.database.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Upsert
import com.rbrauwers.newsapp.database.model.ArticleEntity
import com.rbrauwers.newsapp.database.model.ArticleEntityUpsertWrapper
import kotlinx.coroutines.flow.Flow

@Dao
abstract class HeadlineDao {

    @Query(
        value = """
            SELECT * FROM articles 
            ORDER BY published_at DESC
        """
    )
    abstract fun getHeadlines(): Flow<List<ArticleEntity>>

    @Query(
        value = """
            UPDATE articles 
            SET liked = :liked 
            WHERE id = :id
        """)
    abstract suspend fun updateLiked(id: Int, liked: Boolean)

    @Transaction
    open suspend fun updateLikes(likes: Map<Int, Boolean>) {
        likes.forEach {
            updateLiked(id = it.key, liked = it.value)
        }
    }

    suspend fun upsertHeadlines(headlines: List<ArticleEntity>) {
        upsertHeadlines(headlines.map { ArticleEntityUpsertWrapper(it) })
    }

    @Upsert(entity = ArticleEntity::class)
    internal abstract suspend fun upsertHeadlines(headlines: List<ArticleEntityUpsertWrapper>)

}