package com.rbrauwers.newsapp.data.repository

import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import com.rbrauwers.newsapp.common.isOk
import com.rbrauwers.newsapp.data.model.toEntity
import com.rbrauwers.newsapp.database.NewsDatabase
import com.rbrauwers.newsapp.database.model.ArticleEntity
import com.rbrauwers.newsapp.network.NetworkDataSource
import kotlinx.coroutines.NonCancellable
import kotlinx.coroutines.withContext
import java.util.concurrent.atomic.AtomicInteger

/**
 * Please note that this implementation of pagination has simplifications,
 * mainly related to LoadKeys and queries.
 * A more complete guide is found at https://developer.android.com/codelabs/android-paging#0.
 */
@OptIn(ExperimentalPagingApi::class)
internal class ArticleRemoteMediator(
    private val database: NewsDatabase,
    private val networkDataSource: NetworkDataSource
) : RemoteMediator<Int, ArticleEntity>() {

    private var lastPage = AtomicInteger(1)

    override suspend fun initialize(): InitializeAction {
        return InitializeAction.LAUNCH_INITIAL_REFRESH
    }

    override suspend fun load(
        loadType: LoadType,
        state: PagingState<Int, ArticleEntity>
    ): MediatorResult {
        try {
            val loadKey = when (loadType) {
                LoadType.PREPEND -> {
                    return MediatorResult.Success(endOfPaginationReached = true)
                }

                LoadType.REFRESH -> {
                    lastPage.updateAndGet { 1 }
                }

                LoadType.APPEND -> {
                    when (state.lastItemOrNull()) {
                        null -> {
                            lastPage.updateAndGet { 1 }
                        }

                        else -> {
                            lastPage.incrementAndGet()
                        }
                    }
                }
            }

            val response = networkDataSource.getHeadlines(
                page = loadKey,
                pageSize = state.config.pageSize
            )

            val dao = database.headlineDao()

            // Saves data in local store regardless if coroutine context was cancelled
            withContext(NonCancellable) {
                database.runWithTransaction {
                    if (response.status.isOk()) {
                        if (loadType == LoadType.REFRESH) {
                            dao.clear()
                        }

                        dao.upsertHeadlines(response.articles.map {
                            it.toEntity()
                        })
                    }
                }
            }

            return MediatorResult.Success(endOfPaginationReached = response.articles.isEmpty())

        } catch(e: Exception) {
            return MediatorResult.Error(IllegalAccessError())
        }
    }

}