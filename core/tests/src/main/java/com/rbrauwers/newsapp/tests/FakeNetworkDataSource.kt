package com.rbrauwers.newsapp.tests

import com.rbrauwers.newsapp.model.HeadlinesResponse
import com.rbrauwers.newsapp.model.SourcesResponse
import com.rbrauwers.newsapp.network.NetworkDataSource

class FakeNetworkDataSource : NetworkDataSource {

    override suspend fun getSources(): SourcesResponse {
        return successSourcesResponse
    }

    override suspend fun getHeadlines(
        country: String,
        pageSize: Int,
        page: Int
    ): HeadlinesResponse {
        return successHeadlinesResponse
    }

}
