package com.rbrauwers.newsapp.data.repository

import com.rbrauwers.newsapp.model.Country
import com.rbrauwers.newsapp.network.GraphQLDataSource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf
import javax.inject.Inject
import kotlin.coroutines.cancellation.CancellationException

internal class GraphQLCountryRepository @Inject constructor(
    private val dataSource: GraphQLDataSource
) : CountryRepository {

    override fun getCountry(code: String?): Flow<Country?> = flow {
        emit(dataSource.getCountry(code = code))
    }

}