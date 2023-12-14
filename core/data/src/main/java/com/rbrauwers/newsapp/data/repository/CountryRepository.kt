package com.rbrauwers.newsapp.data.repository

import com.rbrauwers.newsapp.model.Country
import kotlinx.coroutines.flow.Flow

interface CountryRepository {

    fun getCountry(code: String?): Flow<Country?>

}