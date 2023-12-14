package com.rbrauwers.newsapp.data.repository

import com.rbrauwers.newsapp.model.NewsSource
import kotlinx.coroutines.flow.Flow

interface SourceRepository {

    fun getSources(): Flow<List<NewsSource>>

    fun getSource(sourceId: String): Flow<NewsSource?>

    suspend fun sync()

}