package com.rbrauwers.newsapp.data.repository

import com.rbrauwers.newsapp.common.isOk
import com.rbrauwers.newsapp.data.model.toEntity
import com.rbrauwers.newsapp.database.dao.SourceDao
import com.rbrauwers.newsapp.database.model.toExternalModel
import com.rbrauwers.newsapp.model.NewsSource
import com.rbrauwers.newsapp.network.NetworkDataSource
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.NonCancellable
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import javax.inject.Inject

internal class SyncedSourceRepository @Inject constructor(
    private val networkDataSource: NetworkDataSource,
    private val dao: SourceDao
) : SourceRepository {

    override fun getSources(): Flow<List<NewsSource>> {
        return dao.getSources().map { it.map { source -> source.toExternalModel() } }
    }

    override suspend fun sync() {
        runCatching {
            val response = networkDataSource.getSources()

            // Saves data in local store regardless even if coroutine context was cancelled
            withContext(NonCancellable) {
                if (response.status.isOk()) {
                    dao.upsertSources(response.sources.map { it.toEntity() })
                }
            }
        }.onSuccess {
            println("SyncedSourceRepository::sync success")
        }.onFailure {
            // Do not suppress coroutine cancellations
            if (it is CancellationException) throw it
            println("SyncedSourceRepository::sync failure $it")
        }
    }

}