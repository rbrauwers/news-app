package com.rbrauwers.newsapp.data.repository

import com.rbrauwers.newsapp.model.NewsSource
import kotlinx.coroutines.flow.Flow

interface SourceRepository {

    suspend fun getSources(): Flow<List<NewsSource>>

    suspend fun sync()

}