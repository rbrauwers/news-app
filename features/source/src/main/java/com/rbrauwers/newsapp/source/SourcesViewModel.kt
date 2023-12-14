package com.rbrauwers.newsapp.source

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rbrauwers.newsapp.common.Result
import com.rbrauwers.newsapp.common.asResult
import com.rbrauwers.newsapp.data.repository.SourceRepository
import com.rbrauwers.newsapp.model.NewsSource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@HiltViewModel
internal class SourcesViewModel @Inject constructor(
    private val sourceRepository: SourceRepository
) : ViewModel() {

    val sourcesUiState: StateFlow<SourcesUiState> =
        produceSourcesUiState()
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5000),
                initialValue = SourcesUiState.Loading
            )

    private fun produceSourcesUiState(): Flow<SourcesUiState> {
        return sourceRepository
            .getSources()
            .asResult()
            .map { it.toSourcesUiState() }
    }

}

internal sealed interface SourcesUiState {
    data class Success(val sources: List<NewsSource>) : SourcesUiState
    data object Error : SourcesUiState
    data object Loading : SourcesUiState
}

private fun Result<List<NewsSource>>.toSourcesUiState(): SourcesUiState {
    return when (this) {
        is Result.Loading -> SourcesUiState.Loading
        is Result.Error -> SourcesUiState.Error
        is Result.Success -> SourcesUiState.Success(data)
    }
}