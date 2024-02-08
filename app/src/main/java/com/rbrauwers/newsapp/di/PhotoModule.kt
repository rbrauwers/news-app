package com.rbrauwers.newsapp.di

import android.content.Context
import com.rbrauwers.newsapp.data.repository.PhotoWorkerRepository
import com.rbrauwers.newsapp.photo.PhotoWorkerManager
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object CommonModule {

    @Singleton
    @Provides
    fun providePhotoWorkerFlowsProvider(
        @ApplicationContext context: Context,
        repository: PhotoWorkerRepository
    ): PhotoWorkerManager {
        return PhotoWorkerManager(
            context = context,
            repository = repository
        )
    }

}
