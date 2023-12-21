package com.rbrauwers.newsapp.source

import androidx.compose.runtime.Immutable
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rbrauwers.newsapp.common.Result
import com.rbrauwers.newsapp.common.asResult
import com.rbrauwers.newsapp.data.repository.CountryRepository
import com.rbrauwers.newsapp.data.repository.SourceRepository
import com.rbrauwers.newsapp.model.Country
import com.rbrauwers.newsapp.model.NewsSource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@HiltViewModel
internal class SourceViewModel @Inject constructor(
    private val savedStateHandle: SavedStateHandle,
    private val sourceRepository: SourceRepository,
    private val countryRepository: CountryRepository
) : ViewModel() {

    private val sourceId by lazy {
        savedStateHandle.get<String>(sourceIdArg)
    }

    val sourceUiState: StateFlow<SourceUiState> by lazy {
        val id = sourceId
        val flow = if (id == null) {
            flowOf(SourceUiState(source = null))
        } else {
            sourceRepository
                .getSource(sourceId = id)
                .map { it.toSourceUiState() }
        }

        flow.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = SourceUiState(source = null)
        )
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    val countryUiState: StateFlow<CountryUiState> =
        sourceUiState.flatMapLatest {
            produceCountryUiState(source = it.source)
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = CountryUiState.Loading
        )

    private fun produceCountryUiState(source: NewsSource?): Flow<CountryUiState> {
        return countryRepository
            .getCountry(source?.country?.uppercase())
            .asResult()
            .map {
                it.toCountryUiState()
            }
    }

}

@Immutable
internal sealed interface CountryUiState {
    data class Success(val country: Country?) : CountryUiState
    data object Error : CountryUiState
    data object Loading : CountryUiState
}

private fun Result<Country?>.toCountryUiState(): CountryUiState {
    return when (this) {
        is Result.Loading -> CountryUiState.Loading
        is Result.Error -> CountryUiState.Error
        is Result.Success -> CountryUiState.Success(country = data)
    }
}

@Immutable
internal data class SourceUiState(val source: NewsSource?)

private fun NewsSource?.toSourceUiState() = SourceUiState(source = this)
