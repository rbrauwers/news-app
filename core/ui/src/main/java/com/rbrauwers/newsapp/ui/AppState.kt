package com.rbrauwers.newsapp.ui

import androidx.compose.foundation.layout.RowScope
import androidx.compose.runtime.Composable
import androidx.compose.runtime.compositionLocalOf
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class AppState {
    private val _topBarStateFlow = MutableStateFlow(TopBarState())
    private val _bottomBarStateFlow = MutableStateFlow(BottomBarState(isVisible = true))

    val topBarStateFlow = _topBarStateFlow.asStateFlow()
    val bottomBarStateFlow = _bottomBarStateFlow.asStateFlow()

    fun setTopBarState(topBarState: TopBarState) {
        _topBarStateFlow.value = topBarState
    }

    fun setBottomBarState(bottomBarState: BottomBarState) {
        _bottomBarStateFlow.value = bottomBarState
    }
}

val LocalAppState = compositionLocalOf {
    AppState()
}

data class TopBarState(
    val title: (@Composable () -> Unit)? = null,
    val navigationIcon: (@Composable () -> Unit)? = null,
    val actions: (@Composable RowScope.() -> Unit)? = null
)

data class BottomBarState(
    val isVisible: Boolean
)
