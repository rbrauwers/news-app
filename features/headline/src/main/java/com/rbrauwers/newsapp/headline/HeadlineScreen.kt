package com.rbrauwers.newsapp.headline

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.List
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
import com.rbrauwers.newsapp.model.Article
import com.rbrauwers.newsapp.ui.Screen
import com.rbrauwers.newsapp.ui.newsAppDefaultProgressIndicatorItem

val headlineScreen = Screen(
    route = "headlines",
    title = R.string.headlines,
    icon = Icons.Filled.List
)

@Composable
internal fun HeadlinesRoute(
    modifier: Modifier = Modifier.fillMaxSize(),
    viewModel: HeadlineViewModel = hiltViewModel()
) {
    val uiState: HeadlineUiState by viewModel.headlineUiState.collectAsStateWithLifecycle()
    HeadlinesScreen(uiState = uiState, modifier = modifier)
}

@Composable
private fun HeadlinesScreen(
    uiState: HeadlineUiState,
    modifier: Modifier = Modifier
) {
    val listState = rememberLazyListState()
    val arrangement = remember {
        if (uiState is HeadlineUiState.Success) Arrangement.Center else Arrangement.Top
    }

    LazyColumn(
        state = listState,
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = arrangement
    ) {
        when (uiState) {
            is HeadlineUiState.Loading -> {
                newsAppDefaultProgressIndicatorItem()
            }
            is HeadlineUiState.Error -> {
                // TODO
            }
            is HeadlineUiState.Success -> {
                headlines(headlines = uiState.headlines)
            }
        }
    }
}

private fun LazyListScope.headlines(headlines: List<Article>) {
    itemsIndexed(
        items = headlines,
        key = { _, article -> article.id }
    ) { index, article ->
        Text(
            text = article.title.orEmpty(),
            maxLines = 1,
            style = MaterialTheme.typography.headlineMedium)

        Text(
            text = article.description.orEmpty(),
            maxLines = 2,
            style = MaterialTheme.typography.bodyMedium)

        if (index != headlines.lastIndex) {
            Divider()
        }
    }
}

