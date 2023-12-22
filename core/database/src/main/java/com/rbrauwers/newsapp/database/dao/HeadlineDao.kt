package com.rbrauwers.newsapp.database.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Update
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

    suspend fun upsertHeadlines(headlines: List<ArticleEntity>) {
        upsertHeadlines(headlines.map { ArticleEntityUpsertWrapper(it) })
    }

    @Upsert(entity = ArticleEntity::class)
    internal abstract suspend fun upsertHeadlines(headlines: List<ArticleEntityUpsertWrapper>)

}