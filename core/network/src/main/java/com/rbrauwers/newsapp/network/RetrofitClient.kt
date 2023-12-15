package com.rbrauwers.newsapp.network

import android.content.Context
import com.chuckerteam.chucker.api.ChuckerInterceptor
import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import com.rbrauwers.newsapp.model.HeadlinesResponse
import com.rbrauwers.newsapp.model.SourcesResponse
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.http.Body
import retrofit2.http.POST
import javax.inject.Inject
import javax.inject.Singleton

@OptIn(ExperimentalSerializationApi::class)
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

    override suspend fun getHeadlines(country: String): HeadlinesResponse {
        return api.getHeadlines(country)
    }

}

private val json = Json {
    ignoreUnknownKeys = true
}

fun postsAPI(context: Context) = Retrofit
    .Builder()
    .baseUrl("https://jsonplaceholder.typicode.com/")
    .client(
        OkHttpClient.Builder()
            .addInterceptor(
                HttpLoggingInterceptor().apply {
                    setLevel(HttpLoggingInterceptor.Level.BODY)
                },
            )
            .addInterceptor(
                ChuckerInterceptor.Builder(context)
                    .alwaysReadResponseBody(true)
                    .maxContentLength(250_000L)
                    .createShortcut(true)
                    .build()
            ).build()
    )
    .addConverterFactory(json.asConverterFactory("application/json".toMediaType()))
    .build()
    .create(PostAPI::class.java)

interface PostAPI {
    @POST("posts")
    suspend fun createPost(@Body post: Post): Post
}

@Serializable
data class Post(val title: String, val body: String, val userId: Int)

suspend fun postTest(context: Context) {
    val result = runCatching {
        postsAPI(context).createPost(Post("A", "b", 1))
    }

    println("qqq result: $result")
}
