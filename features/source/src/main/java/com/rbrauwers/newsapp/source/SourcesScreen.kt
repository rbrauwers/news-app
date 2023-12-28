package com.rbrauwers.newsapp.source

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.rbrauwers.newsapp.model.NewsSource
import com.rbrauwers.newsapp.ui.AppState
import com.rbrauwers.newsapp.ui.LocalSpacing
import com.rbrauwers.newsapp.ui.Screen
import com.rbrauwers.newsapp.ui.TopBarState
import com.rbrauwers.newsapp.ui.newsAppDefaultProgressIndicatorItem

val sourcesScreen = Screen(
    baseRoute = sourcesBaseRoute,
    route = "$sourcesBaseRoute/list",
    title = R.string.sources,
    icon = Icons.Filled.Person,
    isHome = true
)

@Composable
internal fun SourcesRoute(
    modifier: Modifier = Modifier
        .fillMaxSize()
        .background(MaterialTheme.colorScheme.background),
    viewModel: SourcesViewModel = hiltViewModel(),
    onNavigateToSource: (NewsSource) -> Unit
) {
    val uiState: SourcesUiState by viewModel.sourcesUiState.collectAsStateWithLifecycle()

    AppState.setTopBarState(topBarState = TopBarState(
        title = stringResource(id = R.string.sources)
    ))

    SourcesScreen(
        uiState = uiState,
        modifier = modifier,
        onNavigateToSource = onNavigateToSource
    )
}

@Composable
private fun SourcesScreen(
    uiState: SourcesUiState,
    modifier: Modifier = Modifier,
    onNavigateToSource: (NewsSource) -> Unit
) {
    val listState = rememberLazyListState()
    val arrangement = remember {
        if (uiState is SourcesUiState.Success) Arrangement.Center else Arrangement.Top
    }

    LazyColumn(
        state = listState,
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = arrangement
    ) {
        when (uiState) {
            is SourcesUiState.Loading -> {
                newsAppDefaultProgressIndicatorItem(placeOnCenter = true)
            }

            is SourcesUiState.Error -> {
                // TODO
            }

            is SourcesUiState.Success -> {
                sources(sources = uiState.sources, onClick = onNavigateToSource)
            }
        }
    }
}

private fun LazyListScope.sources(
    sources: List<NewsSource>,
    onClick: (NewsSource) -> Unit
) {
    itemsIndexed(
        items = sources,
        key = { _, source -> source.id }
    ) { index, source ->
        Row(
            modifier = Modifier
                .padding(LocalSpacing.current.medium)
                .background(MaterialTheme.colorScheme.surface)
                .clickable {
                    onClick(source)
                },
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = source.name.orEmpty(),
                    maxLines = 1,
                    style = MaterialTheme.typography.headlineSmall
                )

                Row(
                    modifier = Modifier
                        .fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = source.category.orEmpty(),
                        style = MaterialTheme.typography.bodyMedium,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.primary
                    )

                    Text(
                        text = source.language.orEmpty(),
                        style = MaterialTheme.typography.bodyMedium,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.secondary
                    )
                }
            }

            Spacer(modifier = Modifier.width(LocalSpacing.current.medium))

            Icon(
                imageVector = Icons.Default.KeyboardArrowRight,
                contentDescription = "",
                tint = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }

        if (index != sources.lastIndex) {
            Divider()
        }
    }
}

