package com.rbrauwers.newsapp.source

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
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
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.rbrauwers.newsapp.model.NewsSource
import com.rbrauwers.newsapp.ui.AuthActionButton
import com.rbrauwers.newsapp.ui.BadgedTopBar
import com.rbrauwers.newsapp.ui.BottomBarState
import com.rbrauwers.newsapp.ui.CenteredError
import com.rbrauwers.newsapp.ui.InfoActionButton
import com.rbrauwers.newsapp.ui.LocalAppState
import com.rbrauwers.newsapp.ui.LocalSpacing
import com.rbrauwers.newsapp.ui.NewsAppDefaultProgressIndicator
import com.rbrauwers.newsapp.ui.Screen
import com.rbrauwers.newsapp.ui.SettingsActionButton
import com.rbrauwers.newsapp.ui.TopBarState

val sourcesScreen = Screen(
    baseRoute = sourcesBaseRoute,
    route = "$sourcesBaseRoute/list",
    title = R.string.sources,
    icon = Icons.Filled.Person
)

@Composable
internal fun SourcesRoute(
    modifier: Modifier = Modifier,
    viewModel: SourcesViewModel = hiltViewModel(),
    onNavigateToSource: (NewsSource) -> Unit,
    onNavigateToInfo: () -> Unit,
    onNavigateToSettings: () -> Unit,
    onNavigateToAuth: () -> Unit
) {
    val uiState: SourcesUiState by viewModel.sourcesUiState.collectAsStateWithLifecycle()

    SourcesScreen(
        uiState = uiState,
        modifier = modifier,
        onNavigateToSource = onNavigateToSource,
        onNavigateToInfo = onNavigateToInfo,
        onNavigateToSettings = onNavigateToSettings,
        onNavigateToAuth = onNavigateToAuth
    )
}

@Composable
private fun SourcesScreen(
    uiState: SourcesUiState,
    modifier: Modifier = Modifier,
    onNavigateToSource: (NewsSource) -> Unit,
    onNavigateToInfo: () -> Unit,
    onNavigateToSettings: () -> Unit,
    onNavigateToAuth: () -> Unit
) {
    LocalAppState.current.apply {
        LaunchedEffect(uiState) {
            setTopBarState(
                topBarState = TopBarState(
                    title = {
                        BadgedTopBar(
                            title = stringResource(id = R.string.sources),
                            count = (uiState as? SourcesUiState.Success)?.sources?.size
                        )
                    },
                    actions =  {
                        InfoActionButton(onClick = onNavigateToInfo)
                        SettingsActionButton(onClick = onNavigateToSettings)
                        AuthActionButton(onClick = onNavigateToAuth)
                    }
                )
            )
            setBottomBarState(bottomBarState = BottomBarState(isVisible = true))
        }
    }

    val listState = rememberLazyListState()
    val arrangement = remember {
        if (uiState is SourcesUiState.Success) Arrangement.Center else Arrangement.Top
    }

    Box(modifier = Modifier.fillMaxSize()) {
        when (uiState) {
            is SourcesUiState.Loading -> {
                NewsAppDefaultProgressIndicator(placeOnCenter = true)
            }

            is SourcesUiState.Error -> {
                CenteredError(text = "Something went wrong.")
            }

            is SourcesUiState.Success -> {
                LazyColumn(
                    state = listState,
                    modifier = modifier,
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = arrangement
                ) {
                    sources(sources = uiState.sources, onClick = onNavigateToSource)
                }
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
                imageVector = Icons.AutoMirrored.Filled.KeyboardArrowRight,
                contentDescription = "",
                tint = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }

        if (index != sources.lastIndex) {
            HorizontalDivider()
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun SourcesScreenPreview(
    @PreviewParameter(SourcesPreviewProvider::class) state: SourcesUiState
) {
    SourcesScreen(
        uiState = state,
        onNavigateToSource = { },
        onNavigateToInfo = { },
        onNavigateToSettings = { },
        onNavigateToAuth = { }
    )
}
