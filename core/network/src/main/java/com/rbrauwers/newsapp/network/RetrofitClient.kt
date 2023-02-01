package com.rbrauwers.newsapp.network

import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import com.rbrauwers.newsapp.model.HeadlinesResponse
import com.rbrauwers.newsapp.model.SourcesResponse
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import javax.inject.Inject
import javax.inject.Singleton

@OptIn(ExperimentalSerializationApi::class)
@Singleton
internal class RetrofitClient @Inject constructor(json: Json) : NetworkDataSource {

    private val api: NewsApi =
        Retrofit.Builder()
            .baseUrl("https://newsapi.org/")
            .client(OkHttpClient.Builder()
                .addInterceptor(
                    HttpLoggingInterceptor().apply {
                        setLevel(HttpLoggingInterceptor.Level.BODY)
                    },
                )
                .addInterceptor { chain ->
                    val request = chain.request()

                    val newRequest = request
                        .newBuilder()
                        .addHeader("X-Api-Key", "7b3a48cc5cc24801bdd280dde215dcd0")
                        .build()

                    chain.proceed(newRequest)
                }
                .build())
            .addConverterFactory(json.asConverterFactory("application/json".toMediaType()))
            .build()
            .create(NewsApi::class.java)

    override suspend fun getSources(): SourcesResponse {
        return api.getSources()
    }

    override suspend fun getHeadlines(): HeadlinesResponse {
        return api.getHeadlines()
    }

}