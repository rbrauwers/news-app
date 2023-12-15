package com.rbrauwers.newsapp.headline

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rbrauwers.newsapp.common.Result
import com.rbrauwers.newsapp.common.asResult
import com.rbrauwers.newsapp.common.converters.ConvertStringToDateTimeInstance
import com.rbrauwers.newsapp.data.repository.HeadlineRepository
import com.rbrauwers.newsapp.model.Article
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
internal class HeadlineViewModel @Inject constructor(
    private val headlineRepository: HeadlineRepository
) : ViewModel() {

    val headlineUiState: StateFlow<HeadlineUiState> =
        produceHeadlineUiState()
            .flowOn(Dispatchers.IO)
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

internal sealed interface HeadlineUiState {
    data class Success(val headlines: List<ArticleUi>) : HeadlineUiState
    object Error : HeadlineUiState
    object Loading : HeadlineUiState
}

private fun Result<List<Article>>.toHeadlineUiState(): HeadlineUiState {
    return when (this) {
        is Result.Loading -> HeadlineUiState.Loading
        is Result.Error -> HeadlineUiState.Error
        is Result.Success -> {
            val converter = ConvertStringToDateTimeInstance()
            HeadlineUiState.Success(data.map { it.toArticleUi(converter) })
        }
    }
}

private fun Article.toArticleUi(
    dateConverter: ConvertStringToDateTimeInstance
) = ArticleUi(
    id = id,
    author = if (author.isNullOrBlank()) "Author: N/A" else author,
    title = title,
    urlToImage = urlToImage,
    url = url,
    publishedAt = dateConverter(publishedAt)
)

internal data class ArticleUi(
    val id: Int,
    val author: String?,
    val title: String?,
    val urlToImage: String?,
    val url: String?,
    val publishedAt: String?
)