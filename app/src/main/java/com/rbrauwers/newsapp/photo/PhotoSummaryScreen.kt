package com.rbrauwers.newsapp.photo

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Done
import androidx.compose.material.icons.filled.Error
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.rbrauwers.newsapp.R
import com.rbrauwers.newsapp.settings.settingsBaseRoute
import com.rbrauwers.newsapp.settings.settingsScreen
import com.rbrauwers.newsapp.ui.CloseNavigationIcon
import com.rbrauwers.newsapp.ui.LocalAppState
import com.rbrauwers.newsapp.ui.NewsDefaultTopBar
import com.rbrauwers.newsapp.ui.Screen
import com.rbrauwers.newsapp.ui.TopBarState

val photoSummaryScreen = Screen(
    baseRoute = settingsBaseRoute,
    route = "$settingsScreen/photo/summary",
    title = R.string.photo,
    icon = Icons.Filled.Person
)

@Composable
internal fun PhotoSummaryRoute(
    modifier: Modifier,
    onCloseClick: () -> Unit,
    viewModel: PhotoSummaryViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    LocalAppState.current.apply {
        LaunchedEffect(Unit) {
            setTopBarState(
                topBarState = TopBarState(
                    title = {
                        NewsDefaultTopBar(title = stringResource(id = R.string.photo))
                    },
                    navigationIcon = {
                        CloseNavigationIcon(onCloseClick = onCloseClick)
                    }
                )
            )
        }
    }

    PhotoSummaryScreen(
        uiState = uiState,
        onCloseClick = onCloseClick,
        modifier = modifier
    )
}

@Composable
private fun PhotoSummaryScreen(
    uiState: PhotoSummaryViewModel.UiState,
    onCloseClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(24.dp)
    ) {
        Text(
            text = "Summary",
            style = MaterialTheme.typography.headlineMedium,
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(8.dp))
        HorizontalDivider()
        Spacer(modifier = Modifier.height(24.dp))

        LazyColumn {
            items(uiState.items) {
                PhotoItem(item = it)
                Spacer(modifier = Modifier.height(24.dp))
            }
        }

        Spacer(modifier = Modifier.weight(1f))

        FilledTonalButton(onClick = onCloseClick, modifier = Modifier.fillMaxWidth()) {
            Text(text = "Close")
        }
    }
}

@Composable
private fun PhotoItem(item: PhotoSummaryViewModel.UiState.Item) {
    Row(modifier = Modifier.fillMaxWidth()) {
        Text(text = item.title, style = MaterialTheme.typography.headlineSmall)
        Spacer(modifier = Modifier.weight(1f))

        if (item.isError) {
            Icon(imageVector = Icons.Default.Error, contentDescription = "")
        }

        if (item.isSuccess) {
            Icon(imageVector = Icons.Default.Done, contentDescription = "")
        }
    }

    Spacer(modifier = Modifier.height(16.dp))
    Text(text = item.state, style = MaterialTheme.typography.bodyMedium)
    Spacer(modifier = Modifier.height(16.dp))

    item.progress?.let { progress ->
        LinearProgressIndicator(
            progress = { progress },
            modifier = Modifier.fillMaxWidth()
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun PhotoSummaryPreview() {
    PhotoSummaryScreen(
        onCloseClick = { },
        uiState = PhotoSummaryViewModel.UiState(
            items = listOf(
                PhotoSummaryViewModel.UiState.Item(
                    available = true,
                    title = "Photo 1",
                    state = "In progress",
                    progress = 0.7f,
                    isSuccess = true
                ),
                PhotoSummaryViewModel.UiState.Item(
                    available = true,
                    title = "Photo 2",
                    state = "In progress",
                    progress = 0.4f
                )
            )
        )
    )
}