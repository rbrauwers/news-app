package com.rbrauwers.newsapp.tests

import com.rbrauwers.newsapp.model.Article
import com.rbrauwers.newsapp.model.Country
import com.rbrauwers.newsapp.model.HeadlinesResponse
import com.rbrauwers.newsapp.model.NewsSource
import com.rbrauwers.newsapp.model.SourcesResponse

val sourcesList = listOf(
    NewsSource(
        id = "1",
        name = "First",
        description = "First description",
        url = "https://first.com",
        category = "Health",
        language = "en",
        country = "us"
    ),
    NewsSource(
        id = "2",
        name = "Second",
        description = "Second description",
        url = "https://second.com",
        category = "Economy",
        language = "en",
        country = "us"
    )
)

val successSourcesResponse = SourcesResponse(
    status = "ok",
    sources = sourcesList
)

val articlesList = listOf(
    Article(
        author = "First author",
        title = "First title",
        description = "First description",
        url = "https://first.com",
        urlToImage = "https://first.com",
        publishedAt = "2024-01-28T10:35:15Z",
        content = "First content",
        liked = false
    ),
    Article(
        author = "Second author",
        title = "Second title",
        description = "Second description",
        url = "https://second.com",
        urlToImage = "https://second.com",
        publishedAt = "2024-01-27T08:33:22Z",
        content = "Second content",
        liked = false
    )
)

val successHeadlinesResponse = HeadlinesResponse(
    status = "ok",
    totalResults = articlesList.size,
    articlesList
)

val country = Country(
    name = "USA",
    capital = "Washington",
    continent = "America",
    statesCount = 52,
    emoji = null
)