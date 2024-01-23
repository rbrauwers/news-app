package com.rbrauwers.newsapp.headline

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingData
import androidx.paging.cachedIn
import androidx.paging.filter
import androidx.paging.map
import com.rbrauwers.newsapp.common.converters.ConvertStringToDateTimeInstance
import com.rbrauwers.newsapp.data.repository.HeadlineRepository
import com.rbrauwers.newsapp.database.model.ArticleEntity
import com.rbrauwers.newsapp.database.model.toExternalModel
import com.rbrauwers.newsapp.model.Article
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
internal class PagedHeadlineViewModel @Inject constructor(
    private val headlineRepository: HeadlineRepository,
    pager: Pager<Int, ArticleEntity>
) : ViewModel() {

    private val queryState = MutableStateFlow("")
    private val searchingState = MutableStateFlow(false)
    private var wasSearching = false

    private val dateConverter: ConvertStringToDateTimeInstance by lazy {
        ConvertStringToDateTimeInstance()
    }

    private val pageFlow = pager.flow
        .cachedIn(viewModelScope)

    @OptIn(FlowPreview::class)
    val articlesPageFlow: Flow<PagingData<ArticleUi>> =
        combine(
            queryState
                .onEach { query ->
                    searchingState.update { query.isNotBlank() }
                }
                .debounce(1000L),
            pageFlow
        ) { query, pagingData ->
            pagingData
                .map {
                    it.toExternalModel().toArticleUi(dateConverter = dateConverter)
                }
                .filter {
                    it.matches(query)
                }
        }
            .onEach {
                searchingState.update { false }
            }
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.Lazily,
                initialValue = PagingData.empty()
            )

    val searchState =
        combine(queryState, searchingState) { query, searching ->
            SearchState(
                query = query,
                searching = searching,
                wasQuerying = wasSearching,
                enabled = true
            )
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.Lazily,
            initialValue = SearchState()
        )

    fun updateLiked(articleUi: ArticleUi, liked: Boolean) {
        viewModelScope.launch {
            headlineRepository.updateLiked(id = articleUi.id, liked = liked)
        }
    }

    fun onQueryChange(query: String) {
        wasSearching = queryState.value.isNotBlank()
        queryState.update {
            query
        }
    }

}

private fun Article.toArticleUi(
    dateConverter: ConvertStringToDateTimeInstance
) = ArticleUi(
    id = id,
    author = if (author.isNullOrBlank()) "Author: N/A" else author,
    title = "$title",
    urlToImage = urlToImage,
    url = url,
    publishedAt = publishedAt,
    liked = liked,
    dateConverter = dateConverter
)

