package com.rbrauwers.newsapp.tests.di

import com.rbrauwers.newsapp.data.di.RepositoryModule
import com.rbrauwers.newsapp.data.repository.CountryRepository
import com.rbrauwers.newsapp.data.repository.SourceRepository
import com.rbrauwers.newsapp.network.NetworkDataSource
import com.rbrauwers.newsapp.network.di.NetworkModule
import com.rbrauwers.newsapp.tests.FakeCountryRepository
import com.rbrauwers.newsapp.tests.FakeNetworkDataSource
import com.rbrauwers.newsapp.tests.FakeSourceRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.components.SingletonComponent
import dagger.hilt.testing.TestInstallIn
import javax.inject.Singleton

@Module
@TestInstallIn(
    components = [SingletonComponent::class],
    replaces = [NetworkModule::class, RepositoryModule::class]
)
object TestsModule {

    @Provides
    @Singleton
    fun providesNetworkDataSource(): NetworkDataSource = FakeNetworkDataSource()

    @Provides
    @Singleton
    fun provideSourceRepository(): SourceRepository = FakeSourceRepository()

    @Provides
    @Singleton
    fun provideCountryRepository(): CountryRepository = FakeCountryRepository()

}