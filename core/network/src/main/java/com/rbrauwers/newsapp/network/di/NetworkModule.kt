package com.rbrauwers.newsapp.network.di

import android.content.Context
import com.rbrauwers.newsapp.network.NetworkDataSource
import com.rbrauwers.newsapp.network.RetrofitClient
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import kotlinx.serialization.json.Json
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    @Provides
    @Singleton
    fun providesNetworkJson(): Json = Json {
        ignoreUnknownKeys = true
    }

    @Provides
    @Singleton
    fun providesNetworkDataSource(
        @ApplicationContext context: Context,
        json: Json
    ): NetworkDataSource = RetrofitClient(context, json)

}