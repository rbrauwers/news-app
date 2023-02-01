package com.rbrauwers.newsapp.network

import com.rbrauwers.newsapp.model.HeadlinesResponse
import com.rbrauwers.newsapp.model.SourcesResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface NewsApi {

    @GET("v2/top-headlines/sources")
    suspend fun getSources(): SourcesResponse

    @GET("v2/top-headlines")
    suspend fun getHeadlines(@Query("country") country: String): HeadlinesResponse

}