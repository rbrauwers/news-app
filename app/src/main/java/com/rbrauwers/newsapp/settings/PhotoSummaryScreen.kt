package com.rbrauwers.newsapp.settings

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.rbrauwers.newsapp.R
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
    onCloseClick: () -> Unit
) {
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
        modifier = modifier,
        onCloseClick = onCloseClick
    )
}

@Composable
private fun PhotoSummaryScreen(
    modifier: Modifier = Modifier,
    onCloseClick: () -> Unit
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(text = "Summary")

        FilledTonalButton(onClick = onCloseClick, modifier = Modifier.fillMaxWidth()) {
            Text(text = "Close")
        }
    }
}