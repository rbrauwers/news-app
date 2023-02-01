package com.rbrauwers.newsapp.network

import com.rbrauwers.newsapp.model.HeadlinesResponse
import com.rbrauwers.newsapp.model.SourcesResponse
import retrofit2.http.GET

interface NewsApi {

    @GET("v2/top-headlines/sources")
    suspend fun getSources(): SourcesResponse

    @GET("v2/top-headlines")
    suspend fun getHeadlines(): HeadlinesResponse

}