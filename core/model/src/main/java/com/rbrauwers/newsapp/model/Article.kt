package com.rbrauwers.newsapp.model

import kotlinx.serialization.Serializable

@Serializable
data class Article(
    val author: String?,
    val title: String?,
    val description: String?,
    val url: String?,
    val urlToImage: String?,
    val publishedAt: String?,
    val content: String?,
    // Artificial ID since it is missing on API
    val id: Int = (author+title+description+url+urlToImage+publishedAt+content).hashCode()
)