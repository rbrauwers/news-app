package com.rbrauwers.newsapp.common.di

import com.rbrauwers.newsapp.common.BiometricAuthenticator
import com.rbrauwers.newsapp.common.DefaultBiometricAuthenticator
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object CommonModule {

    @Singleton
    @Provides
    fun provideBiometricAuthenticator(): BiometricAuthenticator {
        return DefaultBiometricAuthenticator()
    }

}
