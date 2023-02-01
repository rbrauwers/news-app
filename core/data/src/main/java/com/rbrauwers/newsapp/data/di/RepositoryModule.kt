package com.rbrauwers.newsapp.data.di

import com.rbrauwers.newsapp.data.repository.HeadlineRepository
import com.rbrauwers.newsapp.data.repository.SourceRepository
import com.rbrauwers.newsapp.data.repository.SyncedHeadlineRepository
import com.rbrauwers.newsapp.data.repository.SyncedSourceRepository
import com.rbrauwers.newsapp.database.dao.HeadlineDao
import com.rbrauwers.newsapp.database.dao.SourceDao
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

}