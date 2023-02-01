package com.rbrauwers.newsapp.data.repository

import com.rbrauwers.newsapp.common.isOk
import com.rbrauwers.newsapp.data.model.toEntity
import com.rbrauwers.newsapp.database.dao.HeadlineDao
import com.rbrauwers.newsapp.database.model.toExternalModel
import com.rbrauwers.newsapp.model.Article
import com.rbrauwers.newsapp.network.NetworkDataSource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

internal class SyncedHeadlineRepository @Inject constructor(
    private val networkDataSource: NetworkDataSource,
    private val dao: HeadlineDao
) : HeadlineRepository {

    override suspend fun getHeadlines(): Flow<List<Article>> {
        return dao.getHeadlines().map { it.map { article -> article.toExternalModel() } }
    }

    override suspend fun sync() {
        runCatching {
            val response = networkDataSource
                .getHeadlines()

            if (response.status.isOk()) {
                dao.upsertHeadlines(response.articles.map { it.toEntity() })
            }
        }.onSuccess {
            println("SyncedHeadlineRepository::sync success")
        }.onFailure {
            println("SyncedHeadlineRepository::sync failure $it")
        }
    }

}