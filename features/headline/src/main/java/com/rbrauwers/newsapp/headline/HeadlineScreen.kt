package com.rbrauwers.newsapp.headline

import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.List
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.Image
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilledIconToggleButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SearchBar
import androidx.compose.material3.Text
import androidx.compose.material3.pulltorefresh.PullToRefreshContainer
import androidx.compose.material3.pulltorefresh.rememberPullToRefreshState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.AsyncImage
import com.rbrauwers.newsapp.ui.BadgedTopBar
import com.rbrauwers.newsapp.ui.BottomBarState
import com.rbrauwers.newsapp.ui.CenteredError
import com.rbrauwers.newsapp.ui.InfoActionButton
import com.rbrauwers.newsapp.ui.LocalAppState
import com.rbrauwers.newsapp.ui.NewsAppDefaultProgressIndicator
import com.rbrauwers.newsapp.ui.Screen
import com.rbrauwers.newsapp.ui.SettingsActionButton
import com.rbrauwers.newsapp.ui.TopBarState
import kotlinx.coroutines.async

val headlinesScreen = Screen(
    baseRoute = headlinesBaseRoute,
    route = "$headlinesBaseRoute/list",
    title = R.string.headlines,
    icon = Icons.AutoMirrored.Filled.List
)

@Composable
internal fun HeadlinesRoute(
    modifier: Modifier = Modifier,
    viewModel: HeadlineViewModel = hiltViewModel(),
    onNavigateToInfo: () -> Unit,
    onNavigateToSettings: () -> Unit
) {
    val uiState: HeadlineUiState by viewModel.headlineUiState.collectAsStateWithLifecycle()
    val searchState: SearchState by viewModel.searchState.collectAsStateWithLifecycle()

    HeadlinesScreen(
        uiState = uiState,
        searchState = searchState,
        modifier = modifier,
        onRefresh = viewModel::sync,
        onLikedChanged = viewModel::updateLiked,
        onQueryChange = viewModel::onQueryChange,
        onNavigateToInfo = onNavigateToInfo,
        onNavigateToSettings = onNavigateToSettings
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun HeadlinesScreen(
    uiState: HeadlineUiState,
    searchState: SearchState,
    modifier: Modifier = Modifier,
    onRefresh: suspend () -> Unit,
    onLikedChanged: (ArticleUi, Boolean) -> Unit,
    onQueryChange: (String) -> Unit,
    onNavigateToInfo: () -> Unit,
    onNavigateToSettings: () -> Unit
) {
    LocalAppState.current.apply {
        LaunchedEffect(uiState) {
            setTopBarState(
                topBarState = TopBarState(
                    title = {
                        BadgedTopBar(
                            title = stringResource(id = R.string.headlines),
                            count = (uiState as? HeadlineUiState.Success)?.headlines?.size
                        )
                    },
                    actions = {
                        InfoActionButton(onClick = onNavigateToInfo)
                        SettingsActionButton(onClick = onNavigateToSettings)
                    }
                )
            )

            setBottomBarState(bottomBarState = BottomBarState(isVisible = true))
        }
    }

    val listState = rememberLazyListState()
    val arrangement = remember {
        if (uiState is HeadlineUiState.Success) Arrangement.Center else Arrangement.Top
    }

    val pullToRefreshState = rememberPullToRefreshState(positionalThreshold = 120.dp)
    if (pullToRefreshState.isRefreshing) {
        LaunchedEffect(true) {
            async {
                onRefresh()
            }.await()

            pullToRefreshState.endRefresh()
        }
    }

    val scaleFraction = if (pullToRefreshState.isRefreshing) 1f else
        LinearOutSlowInEasing.transform(pullToRefreshState.progress).coerceIn(0f, 1f)

    val keyboardController = LocalSoftwareKeyboardController.current

    Box(Modifier.nestedScroll(pullToRefreshState.nestedScrollConnection)) {
        SearchBar(
            query = searchState.query.orEmpty(),
            onQueryChange = onQueryChange,
            onSearch = {
                keyboardController?.hide()
            },
            active = true,
            onActiveChange = { },
            enabled = searchState.enabled,
            placeholder = {
                Text("Search articles")
            },
            leadingIcon = {
                Icon(imageVector = Icons.Default.Search, contentDescription = null)
            },
            trailingIcon = {
                if (searchState.searching) {
                    CircularProgressIndicator(
                        modifier = Modifier
                            .width(20.dp)
                            .aspectRatio(ratio = 1f), strokeWidth = 2.dp
                    )
                }
            }
        ) {
            when (uiState) {
                is HeadlineUiState.Loading -> {
                    NewsAppDefaultProgressIndicator(placeOnCenter = true)
                }

                is HeadlineUiState.Error -> {
                    CenteredError(text = "Something went wrong.")
                }

                is HeadlineUiState.Success -> {
                    LazyColumn(
                        state = listState,
                        modifier = modifier,
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = arrangement
                    ) {
                        headlines(
                            headlines = uiState.headlines,
                            onLikedChanged = onLikedChanged
                        )
                    }

                    if (searchState.query.isNullOrBlank() && searchState.wasQuerying) {
                        LaunchedEffect(Unit) {
                            listState.scrollToItem(0)
                        }
                    }
                }
            }
        }

        PullToRefreshContainer(
            modifier = Modifier
                .align(Alignment.TopCenter)
                .graphicsLayer(scaleX = scaleFraction, scaleY = scaleFraction),
            state = pullToRefreshState,
        )
    }
}

private fun LazyListScope.headlines(
    headlines: List<ArticleUi>,
    onLikedChanged: (ArticleUi, Boolean) -> Unit
) {
    itemsIndexed(
        items = headlines,
        key = { _, article -> article.id }
    ) { index, article ->
        Headline(
            article = article,
            isFirst = index == 0,
            isLast = index == headlines.lastIndex,
            onLikedChanged = onLikedChanged
        )
    }
}

@Composable
internal fun Headline(
    article: ArticleUi,
    isFirst: Boolean = false,
    isLast: Boolean = false,
    onLikedChanged: (ArticleUi, Boolean) -> Unit
) {
    val imageShape = RoundedCornerShape(8.dp)
    val context = LocalContext.current

    Card(
        modifier = Modifier
            .height(216.dp)
            .fillMaxWidth()
            .padding(
                top = if (isFirst) 12.dp else 4.dp,
                bottom = if (isLast) 12.dp else 4.dp,
                start = 16.dp,
                end = 16.dp
            )
            .clickable {
                article.openUrl(context = context)
            }
    ) {
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(24.dp)
        ) {
            Column(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxHeight(),
                verticalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = article.author.orEmpty(),
                    maxLines = 1,
                    style = MaterialTheme.typography.titleSmall,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier
                )

                Text(
                    text = article.title.orEmpty(),
                    maxLines = 3,
                    style = MaterialTheme.typography.bodyLarge,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier.padding(vertical = 12.dp)
                )

                article.formattedPublishedAt?.let { date ->
                    Text(
                        text = date,
                        style = MaterialTheme.typography.bodySmall,
                        modifier = Modifier
                            .background(
                                MaterialTheme.colorScheme.onPrimary,
                                shape = MaterialTheme.shapes.small
                            )
                            .padding(8.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.width(12.dp))

            Column(
                modifier = Modifier.fillMaxHeight(),
                verticalArrangement = Arrangement.SpaceBetween
            ) {
                when (val url = article.urlToImage) {
                    null -> {
                        Box(
                            modifier = Modifier
                                .size(70.dp)
                                .clip(CircleShape)
                        )
                    }

                    else -> {
                        AsyncImage(
                            model = url,
                            placeholder = rememberVectorPainter(image = Icons.Default.Image),
                            contentDescription = "",
                            contentScale = ContentScale.Crop,
                            modifier = Modifier
                                .size(70.dp)
                                .clip(imageShape)
                                .border(
                                    border = BorderStroke(width = 1.dp, color = Color.Black),
                                    shape = imageShape
                                )
                        )
                    }
                }

                FilledIconToggleButton(
                    checked = article.liked,
                    onCheckedChange = { isChecked ->
                        onLikedChanged(article, isChecked)
                    },
                    modifier = Modifier.align(Alignment.End)
                ) {
                    Icon(imageVector = Icons.Default.FavoriteBorder, contentDescription = "")
                }
            }
        }
    }
}

@Preview
@Composable
private fun HeadlinePreview(
    @PreviewParameter(ArticlesPreviewProvider::class) article: ArticleUi
) {
    Headline(article = article, onLikedChanged = { _, _ -> })
}

@Preview
@Composable
private fun ScreenPreview(
    @PreviewParameter(HeadlineScreenProvider::class) state: HeadlineScreenProvider.UiState
) {
    HeadlinesScreen(
        uiState = state.headlineUiState,
        searchState = state.searchState,
        onRefresh = { },
        onLikedChanged = { _, _ -> Boolean },
        onQueryChange = { },
        onNavigateToInfo = { },
        onNavigateToSettings = { }
    )
}


private fun ArticleUi.openUrl(context: Context) {
    runCatching {
        Uri.parse(url)
    }.onSuccess {
        val intent = Intent(Intent.ACTION_VIEW, it)
        context.startActivity(intent)
    }
}
