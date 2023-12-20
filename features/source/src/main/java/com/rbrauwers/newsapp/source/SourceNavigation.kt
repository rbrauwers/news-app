package com.rbrauwers.newsapp.source

import androidx.compose.foundation.layout.RowScope
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import androidx.navigation.NavDestination
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.rbrauwers.newsapp.model.NewsSource
import com.rbrauwers.newsapp.ui.AppState
import com.rbrauwers.newsapp.ui.NewsAppNavigationBarItem

fun NavGraphBuilder.sourcesScreen(
    modifier: Modifier,
    navController: NavController,
    appState: AppState
) {
    composable(route = sourcesScreen.route) {
        SourcesRoute(
            modifier = modifier,
            onNavigateToSource = {
                navController.navigateToSource(source = it)
            },
            appState = appState
        )
    }
}

fun NavGraphBuilder.sourceScreen(
    modifier: Modifier = Modifier,
    appState: AppState
) {
    composable(
        route = sourceScreen.route,
        arguments = listOf(navArgument(sourceIdArg) { type = NavType.StringType })
    ) { entry ->
        SourceRoute(
            modifier = modifier,
            appState = appState
            /*
            There is no need to get this param explicitly, because it is automatically set to SourceViewModel.savedStateHandle
            sourceId = checkNotNull(
                entry.arguments?.getString(
                    sourceIdArg
                )
            )
             */
        )
    }
}

@Composable
fun RowScope.SourcesNavigationBarItem(
    navController: NavController,
    currentDestination: NavDestination?
) {
    NewsAppNavigationBarItem(
        screen = sourcesScreen,
        navController = navController,
        currentDestination = currentDestination
    )
}

fun NavController.navigateToSource(navOptions: NavOptions? = null, source: NewsSource) {
    navigate(buildSourceRoute(source), navOptions)
}

private fun buildSourceRoute(source: NewsSource): String {
    return sourceScreen.route.replace("{$sourceIdArg}", source.id)
}