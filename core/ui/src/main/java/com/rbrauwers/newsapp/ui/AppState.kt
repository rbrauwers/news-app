package com.rbrauwers.newsapp.ui

import androidx.compose.foundation.layout.RowScope
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.remember
import kotlinx.coroutines.flow.MutableStateFlow

@Composable
fun rememberAppState() = remember(Unit) {
    AppState()
}

class AppState {
    private val topBarStateFlow = MutableStateFlow<TopBarState?>(null)

    val topBarState: TopBarState?
        @Composable get() = topBarStateFlow.collectAsState().value

    fun setTopBarState(topBarState: TopBarState?) {
        topBarStateFlow.value = topBarState
    }
}

data class TopBarState(
    //val title: (@Composable () -> Unit)? = null,
    val title: String? = null,
    val actions: (@Composable RowScope.() -> Unit)? = null
)


@Composable
fun SetTopBarState(topBarState: TopBarState?, onComposeAppBarState: (TopBarState?) -> Unit) {
    LaunchedEffect(Unit) {
        onComposeAppBarState(topBarState)
    }
}