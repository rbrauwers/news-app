package com.rbrauwers.newsapp.ui.di

import com.rbrauwers.newsapp.ui.AppState
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object UiModule {

    @Provides
    @Singleton
    fun providesAppState(): AppState = AppState()
}