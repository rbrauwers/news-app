package com.rbrauwers.newsapp.network

import com.rbrauwers.newsapp.model.HeadlinesResponse
import com.rbrauwers.newsapp.model.SourcesResponse

interface NetworkDataSource {

    suspend fun getSources(): SourcesResponse

    suspend fun getHeadlines(country: String = "us"): HeadlinesResponse

}