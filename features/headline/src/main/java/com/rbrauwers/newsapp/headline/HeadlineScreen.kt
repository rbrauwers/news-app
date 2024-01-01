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
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.rbrauwers.newsapp.ui.BadgedTopBar
import com.rbrauwers.newsapp.ui.BottomBarState
import com.rbrauwers.newsapp.ui.InfoActionButton
import com.rbrauwers.newsapp.ui.LocalAppState
import com.rbrauwers.newsapp.ui.NewsAppDefaultProgressIndicator
import com.rbrauwers.newsapp.ui.Screen
import com.rbrauwers.newsapp.ui.TopBarState
import com.rbrauwers.newsapp.ui.theme.NewsAppTheme
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
    onNavigateToInfo: () -> Unit
) {
    val uiState: HeadlineUiState by viewModel.headlineUiState.collectAsStateWithLifecycle()
    val searchState: SearchState by viewModel.searchState.collectAsStateWithLifecycle()

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
                    }
                )
            )

            setBottomBarState(bottomBarState = BottomBarState(isVisible = true))
        }
    }

    HeadlinesScreen(
        uiState = uiState,
        searchState = searchState,
        modifier = modifier,
        onRefresh = viewModel::sync,
        onLikedChanged = viewModel::updateLiked,
        onQueryChange = viewModel::onQueryChange
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
    onQueryChange: (String) -> Unit
) {
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
            if (uiState is HeadlineUiState.Loading) {
                NewsAppDefaultProgressIndicator(placeOnCenter = true)
            }

            LazyColumn(
                state = listState,
                modifier = modifier,
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = arrangement
            ) {
                when (uiState) {
                    is HeadlineUiState.Loading -> {

                    }

                    is HeadlineUiState.Error -> {
                        // TODO
                    }

                    is HeadlineUiState.Success -> {
                        headlines(
                            headlines = uiState.headlines,
                            onLikedChanged = onLikedChanged
                        )
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

/**
 * Using constraint layout due an issue on compose that makes impossible to build this layout
 * with columns and rows. See Headline2 for more details.
 */
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

                article.publishedAt?.let { date ->
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
                            model = ImageRequest.Builder(LocalContext.current)
                                .data(url)
                                .crossfade(true)
                                .build(),
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
private fun HeadlinePreview() {
    Headline(
        article = ArticleUi(
            id = 1,
            author = "Simpsons",
            title = "Inflation is super high is super high is super high is super high is super high",
            urlToImage = "https://images.pexels.com/photos/34299/herbs-flavoring-seasoning-cooking.jpg?cs=srgb&dl=pexels-pixabay-34299.jpg&fm=jpg&w=640&h=427&_gl=1*1urd5oa*_ga*MzQ2NzQzNzA3LjE2NzU3NTcwNzU.*_ga_8JE65Q40S6*MTY3NTc1NzA3NS4xLjEuMTY3NTc1NzEwNC4wLjAuMA..",
            url = "https://google.com",
            publishedAt = "2023-01-02 10:21:00",
            liked = false
        ),
        onLikedChanged = { _, _ -> }
    )
}


// TODO: test this layout on next compose releases. It should work
@Composable
private fun Headline2(article: ArticleUi) {
    NewsAppTheme {
        Card(
            modifier = Modifier
                .height(200.dp)
                .fillMaxWidth()
                .padding(24.dp)
        ) {
            Row(
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxSize()
            ) {
                Text(
                    text = article.title.orEmpty(),
                    maxLines = 1,
                    style = MaterialTheme.typography.headlineSmall
                )

                Text(
                    text = "Teste",
                    maxLines = 1,
                    style = MaterialTheme.typography.headlineSmall,
                    modifier = Modifier
                        .background(Color.Yellow)
                        .weight(1f, fill = false)
                )
            }
        }
    }
}

@Preview
@Composable
private fun HeadlinePreview2() {
    Headline2(
        article = ArticleUi(
            id = 1,
            author = "Simpsons",
            title = "Inflation is super high is super high is super high is super high is super high",
            urlToImage = "https://images.pexels.com/photos/34299/herbs-flavoring-seasoning-cooking.jpg?cs=srgb&dl=pexels-pixabay-34299.jpg&fm=jpg&w=640&h=427&_gl=1*1urd5oa*_ga*MzQ2NzQzNzA3LjE2NzU3NTcwNzU.*_ga_8JE65Q40S6*MTY3NTc1NzA3NS4xLjEuMTY3NTc1NzEwNC4wLjAuMA..",
            url = "https://google.com",
            publishedAt = "2023-01-02 10:21:00",
            liked = false
        )
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
