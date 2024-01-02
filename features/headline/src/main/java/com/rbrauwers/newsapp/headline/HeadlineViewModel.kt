package com.rbrauwers.newsapp.headline

import androidx.compose.runtime.Immutable
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rbrauwers.newsapp.common.Result
import com.rbrauwers.newsapp.common.asResult
import com.rbrauwers.newsapp.common.converters.ConvertStringToDateTimeInstance
import com.rbrauwers.newsapp.data.repository.HeadlineRepository
import com.rbrauwers.newsapp.model.Article
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.system.measureTimeMillis

private var globalCounter = 0

@HiltViewModel
internal class HeadlineViewModel @Inject constructor(
    private val headlineRepository: HeadlineRepository
) : ViewModel() {

    private val queryState = MutableStateFlow("")
    private val searchingState = MutableStateFlow(false)

    private val headlinesFlow = headlineRepository
        .getHeadlines()
        .asResult()
        .map {
            it.toHeadlineUiState()
        }

    @OptIn(FlowPreview::class)
    val headlineUiState: StateFlow<HeadlineUiState> =
        queryState
            .onEach {
                searchingState.update { true }
            }
            .debounce(1000L)
            .combine(headlinesFlow) { query, headlinesState ->
                when {
                    headlinesState is HeadlineUiState.Success && query.isNotBlank() -> {
                        headlinesState.copy(
                            headlines = headlinesState.headlines.filter {
                                it.matches(query)
                            }
                        )
                    }
                    else -> {
                        headlinesState
                    }
                }
            }
            .onEach {
                searchingState.update { false }
            }
            .flowOn(Dispatchers.IO)
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.Lazily,
                initialValue = HeadlineUiState.Loading
            )

    val searchState =
        combine(queryState, searchingState, headlinesFlow) { query, searching, headlinesState ->
            SearchState(
                query = query,
                searching = searching,
                enabled = headlinesState is HeadlineUiState.Success
            )
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.Lazily,
            initialValue = SearchState()
        )

    suspend fun sync() {
        globalCounter++

        val millis = measureTimeMillis {
            headlineRepository.sync()
        }
        delay((2000 - millis).coerceAtLeast(0))
    }

    fun updateLiked(articleUi: ArticleUi, liked: Boolean) {
        viewModelScope.launch {
            headlineRepository.updateLiked(id = articleUi.id, liked = liked)
        }
    }

    fun onQueryChange(query: String) {
        queryState.update {
            query
        }
    }

}

@Immutable
internal sealed interface HeadlineUiState {
    @Immutable
    data class Success(val headlines: List<ArticleUi>) : HeadlineUiState

    @Immutable
    data object Error : HeadlineUiState

    @Immutable
    data object Loading : HeadlineUiState
}

private fun Result<List<Article>>.toHeadlineUiState(): HeadlineUiState {
    return when (this) {
        is Result.Loading -> HeadlineUiState.Loading
        is Result.Error -> HeadlineUiState.Error
        is Result.Success -> {
            val converter = ConvertStringToDateTimeInstance()
            HeadlineUiState.Success(
                headlines = data.map {
                    it.toArticleUi(converter)
                }
            )
        }
    }
}

private fun Article.toArticleUi(
    dateConverter: ConvertStringToDateTimeInstance
) = ArticleUi(
    id = id,
    author = if (author.isNullOrBlank()) "Author: N/A" else author,
    title = "$globalCounter - $title",
    urlToImage = urlToImage,
    url = url,
    publishedAt = dateConverter(publishedAt),
    liked = liked
)

@Immutable
internal data class ArticleUi(
    val id: Int,
    val author: String?,
    val title: String?,
    val urlToImage: String?,
    val url: String?,
    val publishedAt: String?,
    val liked: Boolean
) {
    fun matches(query: String?): Boolean {
        query ?: return true
        return author.orEmpty().contains(query, ignoreCase = true) ||
                title.orEmpty().contains(query, ignoreCase = true)
    }
}

@Immutable
internal data class SearchState(
    val query: String? = null,
    val searching: Boolean = false,
    val enabled: Boolean = false
)