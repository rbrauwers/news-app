package com.rbrauwers.newsapp.network

import com.rbrauwers.newsapp.model.Country


interface GraphQLDataSource {

    suspend fun getCountry(code: String?): Country?

}
