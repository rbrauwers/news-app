package com.rbrauwers.newsapp.source

import androidx.compose.foundation.layout.RowScope
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import androidx.navigation.NavDestination
import androidx.navigation.NavGraph
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.NavOptions
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import androidx.navigation.navigation
import com.rbrauwers.newsapp.model.NewsSource
import com.rbrauwers.newsapp.ui.AppState
import com.rbrauwers.newsapp.ui.NewsAppNavigationBarItem
import com.rbrauwers.newsapp.ui.TopBarState

internal const val sourcesBaseRoute = "sources"

fun NavGraphBuilder.sourcesNavHost() {
    composable(route = sourcesBaseRoute) {
        val navController = rememberNavController()
        NavHost(navController = navController, startDestination = sourcesBaseRoute) {
            sourcesNavGraph(
                navController = navController
            )
        }
    }
}

private fun NavGraphBuilder.sourcesNavGraph(
    navController: NavController
) {
    navigation(startDestination = sourcesScreen.route, route = sourcesBaseRoute) {
        sourcesScreen(
            navController = navController
        )

        sourceScreen(
            onBackClick = {
                navController.popBackStack()
            }
        )
    }
}

private fun NavGraphBuilder.sourcesScreen(
    modifier: Modifier = Modifier,
    navController: NavController,
) {
    composable(route = sourcesScreen.route) {
        SourcesRoute(
            modifier = modifier,
            onNavigateToSource = {
                navController.navigateToSource(source = it)
            }
        )
    }
}

private fun NavGraphBuilder.sourceScreen(
    modifier: Modifier = Modifier,
    onBackClick: () -> Unit
) {
    composable(
        route = sourceScreen.route,
        arguments = listOf(navArgument(sourceIdArg) { type = NavType.StringType })
    ) { entry ->
        SourceRoute(
            modifier = modifier,
            onBackClick = onBackClick
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