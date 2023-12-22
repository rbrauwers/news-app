package com.rbrauwers.newsapp.database.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.rbrauwers.newsapp.model.Article

@Entity(tableName = "articles")
data class ArticleEntity(
    @PrimaryKey
    val id: Int,
    val author: String?,
    val title: String?,
    val description: String?,
    val url: String?,
    @ColumnInfo(name = "url_to_image") val urlToImage: String?,
    @ColumnInfo(name = "published_at") val publishedAt: String?,
    val content: String?,
    @ColumnInfo(defaultValue = "0") val liked: Boolean
)

/**
 * Wrapper to upsert only some columns of ArticleEntity.
 * More specifically, liked column is not upserted, because it is stored only
 * on local database and should not be overwrite by data fetched from network.
 * https://developer.android.com/reference/androidx/room/Update
 */
internal data class ArticleEntityUpsertWrapper(
    val id: Int,
    val author: String?,
    val title: String?,
    val description: String?,
    val url: String?,
    @ColumnInfo(name = "url_to_image") val urlToImage: String?,
    @ColumnInfo(name = "published_at") val publishedAt: String?,
    val content: String?
) {
    constructor(entity: ArticleEntity) : this(
        id = entity.id,
        author = entity.author,
        title = entity.title,
        description = entity.description,
        url = entity.url,
        urlToImage = entity.urlToImage,
        publishedAt = entity.publishedAt,
        content = entity.content
    )
}

fun ArticleEntity.toExternalModel() = Article(
    id = id,
    author = author,
    title = title,
    description = description,
    url = url,
    urlToImage = urlToImage,
    publishedAt = publishedAt,
    content = content,
    liked = liked
)