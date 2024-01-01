package com.rbrauwers.newsapp.headline

import androidx.compose.ui.tooling.preview.PreviewParameterProvider

internal class ArticlesPreviewProvider : PreviewParameterProvider<ArticleUi> {

    companion object {
        val articles = sequenceOf(
            ArticleUi(
                id = 1,
                author = "Simpsons",
                title = "Inflation is super high is super high is super high is super high is super high",
                urlToImage = "https://dims.apnews.com/dims4/default/8a34746/2147483647/strip/true/crop/1975x1111+12+0/resize/1440x810!/quality/90/?url=https%3A%2F%2Fassets.apnews.com%2F57%2F70%2Fabd46beef37a9ee73c6edfac6409%2F32fee9a15ef74bf7b3c1bea5435f5d42",
                url = "https://google.com",
                publishedAt = "2023-01-02 10:21:00",
                liked = false
            ),
            ArticleUi(
                id = 2,
                author = "JOHN O'CONNOR",
                title = "Laws banning semi-automatic weapons and library censorship to take effect in Illinois - The Associated Press",
                urlToImage = "https://dims.apnews.com/dims4/default/8a34746/2147483647/strip/true/crop/1975x1111+12+0/resize/1440x810!/quality/90/?url=https%3A%2F%2Fassets.apnews.com%2F57%2F70%2Fabd46beef37a9ee73c6edfac6409%2F32fee9a15ef74bf7b3c1bea5435f5d42",
                url = "https://google.com",
                publishedAt = "2023-01-02 08:21:00",
                liked = false
            )
        )
    }

    override val values = sequenceOf(
        articles.elementAt(0),
        articles.elementAt(1)
    )

}

internal class HeadlineScreenProvider : PreviewParameterProvider<HeadlineScreenProvider.UiState> {

    data class UiState(
        val headlineUiState: HeadlineUiState,
        val searchState: SearchState
    )

    override val values = sequenceOf(
        UiState(headlineUiState = HeadlineUiState.Loading, searchState = SearchState()),
        UiState(headlineUiState = HeadlineUiState.Error, searchState = SearchState()),
        UiState(
            headlineUiState = HeadlineUiState.Success(
                headlines = ArticlesPreviewProvider.articles.toList()
            ), searchState = SearchState()
        ),
        UiState(
            headlineUiState = HeadlineUiState.Success(
                headlines = ArticlesPreviewProvider.articles.toList()
            ), searchState = SearchState(query = "Term", searching = true)
        )
    )

}