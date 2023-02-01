package com.rbrauwers.newsapp.network.di

import com.rbrauwers.newsapp.network.NetworkDataSource
import com.rbrauwers.newsapp.network.RetrofitClient
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
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
    fun providesNetworkDataSource(json: Json): NetworkDataSource = RetrofitClient(json)

}