package com.rbrauwers.newsapp.data.repository

import com.rbrauwers.newsapp.model.Article
import kotlinx.coroutines.flow.Flow

interface HeadlineRepository {

    fun getHeadlines(): Flow<List<Article>>

    suspend fun sync()

}