package com.rbrauwers.newsapp.ui

import androidx.annotation.StringRes
import androidx.compose.ui.graphics.vector.ImageVector

class Screen(
    val baseRoute: String,
    val route: String,
    @StringRes val title: Int,
    val icon: ImageVector,
    val isHome: Boolean
)