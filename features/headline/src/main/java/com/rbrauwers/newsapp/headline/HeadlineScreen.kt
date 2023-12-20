package com.rbrauwers.newsapp.headline

import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.outlined.OpenInBrowser
import androidx.compose.material3.Card
import androidx.compose.material3.FilledIconButton
import androidx.compose.material3.FilledIconToggleButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.rbrauwers.newsapp.ui.AppState
import com.rbrauwers.newsapp.ui.Screen
import com.rbrauwers.newsapp.ui.TopBarState
import com.rbrauwers.newsapp.ui.newsAppDefaultProgressIndicatorItem
import com.rbrauwers.newsapp.ui.rememberAppState
import com.rbrauwers.newsapp.ui.theme.NewsAppTheme

val headlineScreen = Screen(
    route = "headlines",
    title = R.string.headlines,
    icon = Icons.Filled.List,
    isHome = true
)

@Composable
internal fun HeadlinesRoute(
    modifier: Modifier = Modifier,
    viewModel: HeadlineViewModel = hiltViewModel(),
    appState: AppState
) {
    val uiState: HeadlineUiState by viewModel.headlineUiState.collectAsStateWithLifecycle()
    appState.setTopBarState(TopBarState(title = stringResource(id = R.string.headlines)))
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
                newsAppDefaultProgressIndicatorItem(placeOnCenter = true)
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

private fun LazyListScope.headlines(headlines: List<ArticleUi>) {
    itemsIndexed(
        items = headlines,
        key = { _, article -> article.id }
    ) { index, article ->
        Headline(article = article, isFirst = index == 0, isLast = index == headlines.lastIndex)
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
    isLast: Boolean = false
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
        ConstraintLayout(
            modifier = Modifier
                .fillMaxSize()
                .padding(start = 16.dp, end = 16.dp, top = 16.dp, bottom = 8.dp)
        ) {
            val (author, title, date, image, web) = createRefs()

            AsyncImage(
                model = ImageRequest.Builder(LocalContext.current)
                    .data(article.urlToImage)
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
                    .constrainAs(image) {
                        end.linkTo(parent.end)
                    }
            )

            Text(
                text = article.author.orEmpty(),
                maxLines = 1,
                style = MaterialTheme.typography.titleSmall,
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier.constrainAs(author) {
                    width = Dimension.fillToConstraints
                    start.linkTo(parent.start)
                    end.linkTo(image.start, margin = 16.dp)
                }
            )

            Text(
                text = article.title.orEmpty(),
                maxLines = 4,
                style = MaterialTheme.typography.bodyLarge,
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier
                    .constrainAs(title) {
                        width = Dimension.fillToConstraints
                        top.linkTo(author.bottom, margin = 2.dp)
                        start.linkTo(author.start)
                        end.linkTo(author.end)
                    }
            )

            Text(
                text = article.publishedAt.orEmpty(),
                style = MaterialTheme.typography.bodySmall,
                modifier = Modifier
                    .constrainAs(date) {
                        bottom.linkTo(web.bottom, margin = 4.dp)
                        start.linkTo(author.start)
                    }
                    .background(
                        MaterialTheme.colorScheme.onPrimary,
                        shape = MaterialTheme.shapes.small
                    )
                    .padding(8.dp)
            )

            FilledIconToggleButton(
                checked = isFirst,
                onCheckedChange = { isChecked -> },
                modifier = Modifier
                    .constrainAs(web) {
                        end.linkTo(image.end)
                        bottom.linkTo(parent.bottom, margin = 0.dp)
                    }
            ) {
                Icon(imageVector = Icons.Default.FavoriteBorder, contentDescription = "")
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
            publishedAt = "2023-01-02 10:21:00"
        )
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
            publishedAt = "2023-01-02 10:21:00"
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
