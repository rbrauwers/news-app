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
    val content: String?
)

fun ArticleEntity.toExternalModel() = Article(
    id = id,
    author = author,
    title = title,
    description = description,
    url = url,
    urlToImage = urlToImage,
    publishedAt = publishedAt,
    content = content
)