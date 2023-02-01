package com.rbrauwers.newsapp.data.model

import com.rbrauwers.newsapp.database.model.NewsSourceEntity
import com.rbrauwers.newsapp.model.NewsSource

fun NewsSource.toEntity() = NewsSourceEntity(
    id = id,
    name = name,
    description = description,
    url = url,
    category = category,
    language = language,
    country = country
)