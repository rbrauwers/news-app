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
internal class SourceViewModel @Inject constructor(
    private val sourceRepository: SourceRepository
) : ViewModel() {

    val sourceUiState: StateFlow<SourceUiState> =
        produceSourceUiState()
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5000),
                initialValue = SourceUiState.Loading
            )

    private fun produceSourceUiState(): Flow<SourceUiState> {
        return sourceRepository
            .getSources()
            .asResult()
            .map { it.toSourceUiState() }
    }

}

internal sealed interface SourceUiState {
    data class Success(val sources: List<NewsSource>) : SourceUiState
    data object Error : SourceUiState
    data object Loading : SourceUiState
}

private fun Result<List<NewsSource>>.toSourceUiState(): SourceUiState {
    return when (this) {
        is Result.Loading -> SourceUiState.Loading
        is Result.Error -> SourceUiState.Error
        is Result.Success -> SourceUiState.Success(data)
    }
}