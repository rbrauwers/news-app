package com.rbrauwers.newsapp.headline

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rbrauwers.newsapp.common.Result
import com.rbrauwers.newsapp.common.asResult
import com.rbrauwers.newsapp.data.repository.HeadlineRepository
import com.rbrauwers.newsapp.model.Article
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@HiltViewModel
class HeadlineViewModel @Inject constructor(
    private val headlineRepository: HeadlineRepository
) : ViewModel() {

    val headlineUiState: StateFlow<HeadlineUiState> =
        produceHeadlineUiState()
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5000),
                initialValue = HeadlineUiState.Loading
            )

    private fun produceHeadlineUiState(): Flow<HeadlineUiState> {
        return headlineRepository
            .getHeadlines()
            .asResult()
            .map { it.toHeadlineUiState() }
    }

}

sealed interface HeadlineUiState {
    data class Success(val headlines: List<Article>) : HeadlineUiState
    object Error : HeadlineUiState
    object Loading : HeadlineUiState
}

private fun Result<List<Article>>.toHeadlineUiState(): HeadlineUiState {
    return when (this) {
        is Result.Loading -> HeadlineUiState.Loading
        is Result.Error -> HeadlineUiState.Error
        is Result.Success -> HeadlineUiState.Success(data)
    }
}