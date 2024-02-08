package com.rbrauwers.newsapp.database.di

import com.rbrauwers.newsapp.database.NewsDatabase
import com.rbrauwers.newsapp.database.dao.HeadlineDao
import com.rbrauwers.newsapp.database.dao.PhotoWorkerDao
import com.rbrauwers.newsapp.database.dao.SourceDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
internal object DaoModule {

    @Provides
    @Singleton
    fun provideHeadlineDao(
        database: NewsDatabase
    ) : HeadlineDao = database.headlineDao()

    @Provides
    @Singleton
    fun provideSourceDao(
        database: NewsDatabase
    ) : SourceDao = database.sourceDao()

    @Provides
    @Singleton
    fun providePhotoWorkerDao(
        database: NewsDatabase
    ) : PhotoWorkerDao = database.photoWorkerDao()

}
