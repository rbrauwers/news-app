package com.rbrauwers.newsapp.source

import androidx.compose.foundation.layout.RowScope
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import androidx.navigation.NavDestination
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import com.rbrauwers.newsapp.ui.NewsAppNavigationBarItem

fun NavController.navigateToSources(navOptions: NavOptions? = null) {
    this.navigate(sourceScreen.route, navOptions)
}

fun NavGraphBuilder.sourcesScreen(modifier: Modifier) {
    composable(route = sourceScreen.route) {
        SourcesRoute(modifier = modifier)
    }
}

@Composable
fun RowScope.SourcesNavigationBarItem(
    navController: NavController,
    currentDestination: NavDestination?
) {
    NewsAppNavigationBarItem(
        screen = sourceScreen,
        navController = navController,
        currentDestination = currentDestination
    )
}