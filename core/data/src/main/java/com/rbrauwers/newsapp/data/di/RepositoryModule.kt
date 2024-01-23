package com.rbrauwers.newsapp.data.di

import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import com.rbrauwers.newsapp.data.repository.ArticleRemoteMediator
import com.rbrauwers.newsapp.data.repository.CountryRepository
import com.rbrauwers.newsapp.data.repository.GraphQLCountryRepository
import com.rbrauwers.newsapp.data.repository.HeadlineRepository
import com.rbrauwers.newsapp.data.repository.SourceRepository
import com.rbrauwers.newsapp.data.repository.SyncedHeadlineRepository
import com.rbrauwers.newsapp.data.repository.SyncedSourceRepository
import com.rbrauwers.newsapp.database.NewsDatabase
import com.rbrauwers.newsapp.database.dao.HeadlineDao
import com.rbrauwers.newsapp.database.dao.SourceDao
import com.rbrauwers.newsapp.database.model.ArticleEntity
import com.rbrauwers.newsapp.network.GraphQLDataSource
import com.rbrauwers.newsapp.network.NetworkDataSource
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
object RepositoryModule {

    @Provides
    fun provideHeadlineRepository(
        networkDataSource: NetworkDataSource,
        dao: HeadlineDao
    ): HeadlineRepository =
        SyncedHeadlineRepository(networkDataSource = networkDataSource, dao = dao)

    @Provides
    fun provideSourceRepository(
        networkDataSource: NetworkDataSource,
        dao: SourceDao
    ): SourceRepository = SyncedSourceRepository(networkDataSource = networkDataSource, dao = dao)

    @Provides
    fun provideCountryRepository(
        graphQLDataSource: GraphQLDataSource
    ): CountryRepository = GraphQLCountryRepository(dataSource = graphQLDataSource)

    @OptIn(ExperimentalPagingApi::class)
    @Provides
    fun provideArticlesPager(
        database: NewsDatabase,
        networkDataSource: NetworkDataSource
    ): Pager<Int, ArticleEntity> {
        val pageSize = 5

        return Pager(
            config = PagingConfig(
                pageSize = pageSize,
                prefetchDistance = pageSize * 2,
                initialLoadSize = pageSize * 2,
                enablePlaceholders = true),
            remoteMediator = ArticleRemoteMediator(
                database = database,
                networkDataSource = networkDataSource
            ),
            pagingSourceFactory = {
                database.headlineDao().pagingSource()
            }
        )
    }

}