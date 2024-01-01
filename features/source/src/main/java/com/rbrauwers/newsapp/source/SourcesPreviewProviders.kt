package com.rbrauwers.newsapp.source

import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import com.rbrauwers.newsapp.model.Country
import com.rbrauwers.newsapp.model.NewsSource

internal class SourcesPreviewProvider : PreviewParameterProvider<SourcesUiState> {

    companion object {
        val sources = sequenceOf(
            NewsSource(
                id = "abc-news",
                name = "ABC News",
                description = "Your trusted source for breaking news, analysis, exclusive interviews, headlines, and videos at ABCNews.com.",
                url = "https://abcnews.go.com",
                category = "general",
                language = "en",
                country = "us"
            ),
            NewsSource(
                id = "al-jazeera-english",
                name = "Al Jazeera English",
                description = "News, analysis from the Middle East and worldwide, multimedia and interactives, opinions, documentaries, podcasts, long reads and broadcast schedule.",
                url = "http://www.aljazeera.com",
                category = "general",
                language = "en",
                country = "us"
            )
        )

    }

    override val values = sequenceOf(
        SourcesUiState.Loading,
        SourcesUiState.Error,
        SourcesUiState.Success(sources = sources.toList())
    )

}

internal class SourcePreviewProvider : PreviewParameterProvider<SourcePreviewProvider.UiState> {

    data class UiState(
        val sourceUiState: SourceUiState,
        val countryUiState: CountryUiState
    )

    override val values = sequenceOf(
        UiState(
            sourceUiState = SourceUiState(source = SourcesPreviewProvider.sources.first()),
            countryUiState = CountryUiState.Loading
        ),
        UiState(
            sourceUiState = SourceUiState(source = SourcesPreviewProvider.sources.first()),
            countryUiState = CountryUiState.Error
        ),
        UiState(
            sourceUiState = SourceUiState(source = SourcesPreviewProvider.sources.first()),
            countryUiState = CountryUiState.Success(
                country = Country(
                    name = "Brazil",
                    capital = "Brasilia",
                    continent = "South America",
                    statesCount = 20,
                    emoji = "\uD83C\uDDFA"
                )
            )
        )
    )

}