package com.rbrauwers.newsapp.info

import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.core.tween
import androidx.compose.animation.slideInHorizontally
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable

fun NavController.navigateToInfo(navOptions: NavOptions? = null) {
    this.navigate(infoScreen.route, navOptions)
}

fun NavGraphBuilder.infoScreen(modifier: Modifier) {
    composable(
        route = infoScreen.route,
    ) {
        InfoRoute(modifier = modifier)
    }
}