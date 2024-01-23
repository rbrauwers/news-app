package com.rbrauwers.newsapp.network

import android.content.Context
import com.chuckerteam.chucker.api.ChuckerInterceptor
import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import com.rbrauwers.newsapp.model.HeadlinesResponse
import com.rbrauwers.newsapp.model.SourcesResponse
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
internal class RetrofitClient @Inject constructor(
    @ApplicationContext context: Context,
    json: Json
) : NetworkDataSource {

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
                        .addHeader("X-Api-Key", BuildConfig.NEWS_API_KEY)
                        .build()

                    chain.proceed(newRequest)
                }
                .addInterceptor(
                    ChuckerInterceptor.Builder(context)
                        .alwaysReadResponseBody(true)
                        .maxContentLength(250_000L)
                        .createShortcut(true)
                        .build()
                )
                .build()
            )
            .addConverterFactory(json.asConverterFactory("application/json".toMediaType()))
            .build()
            .create(NewsApi::class.java)

    override suspend fun getSources(): SourcesResponse {
        return api.getSources()
    }

    override suspend fun getHeadlines(
        country: String,
        pageSize: Int,
        page: Int
    ): HeadlinesResponse {
        return api.getHeadlines(country = country, pageSize = pageSize, page = page)
    }

}