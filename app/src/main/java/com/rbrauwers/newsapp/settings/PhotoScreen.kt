package com.rbrauwers.newsapp.settings

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.rbrauwers.newsapp.R
import com.rbrauwers.newsapp.ui.BackNavigationIcon
import com.rbrauwers.newsapp.ui.LocalAppState
import com.rbrauwers.newsapp.ui.NewsDefaultTopBar
import com.rbrauwers.newsapp.ui.Screen
import com.rbrauwers.newsapp.ui.TopBarState

val photoScreen = Screen(
    baseRoute = settingsBaseRoute,
    route = "$settingsScreen/photo/{$photoIdArg}",
    title = R.string.photo,
    icon = Icons.Filled.Person
)

@Composable
internal fun PhotoRoute(
    onBackClick: () -> Unit,
    onNavigateToNextPhoto: (Int) -> Unit,
    onNavigateToSummary: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: PhotoViewModel = hiltViewModel()
) {
    val uiState: PhotoViewModel.UiState by viewModel.uiState.collectAsStateWithLifecycle()

    LocalAppState.current.apply {
        LaunchedEffect(Unit) {
            setTopBarState(
                topBarState = TopBarState(
                    title = {
                        NewsDefaultTopBar(title = stringResource(id = R.string.photo))
                    },
                    navigationIcon = {
                        BackNavigationIcon(onBackClick = onBackClick)
                    }
                )
            )
        }
    }

    PhotoScreen(
        uiState = uiState,
        modifier = modifier,
        onNavigateToNextPhoto = onNavigateToNextPhoto,
        onNavigateToSummary = onNavigateToSummary
    )
}

@Composable
private fun PhotoScreen(
    uiState: PhotoViewModel.UiState,
    onNavigateToNextPhoto: (Int) -> Unit,
    onNavigateToSummary: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier.fillMaxSize().padding(24.dp), horizontalAlignment = Alignment.CenterHorizontally) {
        Text(
            text = "Photo upload ${uiState.photoId}",
            style = MaterialTheme.typography.labelMedium
        )

        Spacer(modifier = Modifier.weight(1f))

        if (uiState.nextPhotoIsEnabled) {
            FilledTonalButton(onClick = {
                uiState.nextPhotoId?.let { nextPhotoId ->
                    onNavigateToNextPhoto(nextPhotoId)
                }
            }) {
                Text(text = "Next photo")
            }
        }

        if (uiState.summaryIsEnabled) {
            FilledTonalButton(onClick = onNavigateToSummary, modifier = Modifier.fillMaxWidth()) {
                Text(text = "Go to summary")
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun PhotoScreenPreview() {
    PhotoScreen(
        uiState = PhotoViewModel.UiState(photoId = "1"),
        onNavigateToNextPhoto = { },
        onNavigateToSummary = { })
}