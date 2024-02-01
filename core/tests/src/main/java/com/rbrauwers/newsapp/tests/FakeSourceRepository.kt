package com.rbrauwers.newsapp.tests

import com.rbrauwers.newsapp.data.repository.SourceRepository
import com.rbrauwers.newsapp.model.NewsSource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.flowOf

class FakeSourceRepository : SourceRepository {

    private val flow = MutableSharedFlow<List<NewsSource>>()

    override fun getSources(): Flow<List<NewsSource>> {
        return flow
    }

    override fun getSource(sourceId: String): Flow<NewsSource?> {
        return flowOf(null)
    }

    override suspend fun sync() {
        emitAll()
    }

    suspend fun emitOne() {
        flow.emit(listOf(sourcesList.first()))
    }

    private suspend fun emitAll() {
        flow.emit(sourcesList)
    }

}