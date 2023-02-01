package com.rbrauwers.newsapp.model

import kotlinx.serialization.Serializable

@Serializable
data class SourcesResponse(
    val status: String,
    val sources: List<NewsSource>
)