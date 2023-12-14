package com.rbrauwers.newsapp.network

import android.content.Context
import com.apollographql.apollo3.ApolloClient
import com.apollographql.apollo3.network.okHttpClient
import com.chuckerteam.chucker.api.ChuckerInterceptor
import com.rbrauwers.newsapp.model.Country
import dagger.hilt.android.qualifiers.ApplicationContext
import okhttp3.OkHttpClient
import javax.inject.Inject

internal class GraphQLClient @Inject constructor(
    @ApplicationContext context: Context,
) : GraphQLDataSource {

    private val client: ApolloClient by lazy {
        val okHttpClient = OkHttpClient.Builder()
            .addInterceptor(
                ChuckerInterceptor.Builder(context)
                    .alwaysReadResponseBody(true)
                    .maxContentLength(250_000L)
                    .createShortcut(true)
                    .build()
            )
            .build()

        ApolloClient.Builder()
            .serverUrl("https://countries.trevorblades.com/graphql")
            .okHttpClient(okHttpClient)
            .build()
    }

    override suspend fun getCountry(code: String?): Country? {
        code ?: return null

        return client
            .query(GetCountryQuery(code = code))
            .execute()
            .data
            ?.country
            ?.toModel()
    }

}

private fun GetCountryQuery.Country.toModel() : Country {
    return Country(
        name = this.name,
        capital = this.capital,
        continent = this.continent.name,
        statesCount = this.states.size,
        emoji = this.emoji
    )
}