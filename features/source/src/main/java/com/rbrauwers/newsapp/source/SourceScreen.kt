package com.rbrauwers.newsapp.source

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Divider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.rbrauwers.newsapp.model.NewsSource
import com.rbrauwers.newsapp.ui.Screen
import com.rbrauwers.newsapp.ui.newsAppDefaultProgressIndicatorItem

val sourceScreen = Screen(
    route = "sources",
    title = R.string.sources,
    icon = Icons.Filled.Person
)

@Composable
internal fun SourcesRoute(
    modifier: Modifier = Modifier.fillMaxSize(),
    viewModel: SourceViewModel = hiltViewModel()
) {
    val uiState: SourceUiState by viewModel.sourceUiState.collectAsStateWithLifecycle()
    SourcesScreen(uiState = uiState, modifier = modifier)
}

@Composable
private fun SourcesScreen(
    uiState: SourceUiState,
    modifier: Modifier = Modifier
) {
    val listState = rememberLazyListState()
    val arrangement = remember {
        if (uiState is SourceUiState.Success) Arrangement.Center else Arrangement.Top
    }

    LazyColumn(
        state = listState,
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = arrangement
    ) {
        when (uiState) {
            is SourceUiState.Loading -> {
                newsAppDefaultProgressIndicatorItem()
            }
            is SourceUiState.Error -> {
                // TODO
            }
            is SourceUiState.Success -> {
                sources(sources = uiState.sources)
            }
        }
    }
}

private fun LazyListScope.sources(sources: List<NewsSource>) {
    itemsIndexed(
        items = sources,
        key = { _, source -> source.id }
    ) { index, source ->
        Text(
            text = source.name.orEmpty(),
            maxLines = 1,
            style = MaterialTheme.typography.headlineMedium)

        Text(
            text = source.description.orEmpty(),
            maxLines = 2,
            style = MaterialTheme.typography.bodyMedium)

        if (index != sources.lastIndex) {
            Divider()
        }
    }
}

