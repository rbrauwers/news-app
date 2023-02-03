package com.rbrauwers.newsapp.ui

import androidx.annotation.RestrictTo
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

@RestrictTo(RestrictTo.Scope.LIBRARY)
val LocalSpacing = compositionLocalOf { Dimensions() }

@RestrictTo(RestrictTo.Scope.LIBRARY)
data class Dimensions(
    val extraSmall: Dp = 4.dp,
    val small: Dp = 8.dp,
    val medium: Dp = 16.dp,
    val large: Dp = 24.dp,
    val extraLarge: Dp = 64.dp,
    val horizontalDefault: Dp = 16.dp,
    val horizontalBtwGroupedComponents: Dp = 8.dp,
    val horizontalBtwTopAppBarActions: Dp = 16.dp,
    val verticalOnEdges: Dp = 16.dp,
    val verticalBtwListItems: Dp = 16.dp,
    val dividerHeight: Dp = 1.dp,
    val topAppBarActionIconSize: Dp = 24.dp
)