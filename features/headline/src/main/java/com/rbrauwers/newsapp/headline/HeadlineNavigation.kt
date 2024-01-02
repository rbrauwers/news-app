package com.rbrauwers.newsapp.headline

import androidx.compose.foundation.layout.RowScope
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import androidx.navigation.NavDestination
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navigation
import com.rbrauwers.newsapp.ui.NewsAppNavigationBarItem

const val headlinesBaseRoute = "headlines"

fun NavGraphBuilder.headlinesNavHost(
    onNavigateToInfo: () -> Unit,
    onNavigateToSettings: () -> Unit
) {
    composable(route = headlinesBaseRoute) {
        NavHost(navController = rememberNavController(), startDestination = headlinesBaseRoute) {
            headlinesNavGraph(
                onNavigateToInfo = onNavigateToInfo,
                onNavigateToSettings = onNavigateToSettings
            )
        }
    }
}

private fun NavGraphBuilder.headlinesNavGraph(
    onNavigateToInfo: () -> Unit,
    onNavigateToSettings: () -> Unit
) {
    navigation(startDestination = headlinesScreen.route, route = headlinesBaseRoute) {
        headlinesScreen(
            onNavigateToInfo = onNavigateToInfo,
            onNavigateToSettings = onNavigateToSettings
        )
    }
}

fun NavController.navigateToHeadlines(navOptions: NavOptions? = null) {
    this.navigate(headlinesScreen.route, navOptions)
}

private fun NavGraphBuilder.headlinesScreen(
    modifier: Modifier = Modifier,
    onNavigateToInfo: () -> Unit,
    onNavigateToSettings: () -> Unit
) {
    composable(route = headlinesScreen.route) {
        HeadlinesRoute(
            modifier = modifier,
            onNavigateToInfo = onNavigateToInfo,
            onNavigateToSettings = onNavigateToSettings
        )
    }
}

@Composable
fun RowScope.HeadlinesNavigationBarItem(
    navController: NavController,
    currentDestination: NavDestination?
) {
    NewsAppNavigationBarItem(
        screen = headlinesScreen,
        navController = navController,
        currentDestination = currentDestination
    )
}