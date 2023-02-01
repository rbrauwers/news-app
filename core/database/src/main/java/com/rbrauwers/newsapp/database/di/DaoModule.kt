package com.rbrauwers.newsapp.database.di

import com.rbrauwers.newsapp.database.NewsDatabase
import com.rbrauwers.newsapp.database.dao.HeadlineDao
import com.rbrauwers.newsapp.database.dao.SourceDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
internal object DaoModule {

    @Provides
    fun provideHeadlineDao(
        database: NewsDatabase
    ) : HeadlineDao = database.headlineDao()

    @Provides
    fun provideSourceDao(
        database: NewsDatabase
    ) : SourceDao = database.sourceDao()

}