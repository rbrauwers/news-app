package com.rbrauwers.newsapp.headline

import androidx.compose.foundation.layout.RowScope
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import androidx.navigation.NavDestination
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import com.rbrauwers.newsapp.ui.AppState
import com.rbrauwers.newsapp.ui.NewsAppNavigationBarItem

fun NavController.navigateToHeadlines(navOptions: NavOptions? = null) {
    this.navigate(headlineScreen.route, navOptions)
}

fun NavGraphBuilder.headlinesScreen(modifier: Modifier, appState: AppState) {
    composable(route = headlineScreen.route) {
        HeadlinesRoute(modifier = modifier, appState = appState)
    }
}

@Composable
fun RowScope.HeadlinesNavigationBarItem(
    navController: NavController,
    currentDestination: NavDestination?
) {
    NewsAppNavigationBarItem(
        screen = headlineScreen,
        navController = navController,
        currentDestination = currentDestination
    )
}