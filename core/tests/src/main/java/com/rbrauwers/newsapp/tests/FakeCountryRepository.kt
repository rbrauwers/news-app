package com.rbrauwers.newsapp.tests

import com.rbrauwers.newsapp.data.repository.CountryRepository
import com.rbrauwers.newsapp.model.Country
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf

class FakeCountryRepository : CountryRepository {

    override fun getCountry(code: String?): Flow<Country?> {
        return flowOf(country)
    }

}