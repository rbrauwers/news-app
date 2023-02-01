package com.rbrauwers.newsapp.database.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.rbrauwers.newsapp.model.NewsSource

@Entity(tableName = "news_sources")
data class NewsSourceEntity(
    @PrimaryKey val id: String,
    val name: String?,
    val description: String?,
    val url: String?,
    val category: String?,
    val language: String?,
    val country: String?
)

fun NewsSourceEntity.toExternalModel() = NewsSource(
    id = id,
    name = name,
    description = description,
    url = url,
    category = category,
    language = language,
    country = country
)
