package com.rbrauwers.newsapp.authentication

import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import androidx.navigation.navigation

internal const val authBaseRoute = "auth"

fun NavGraphBuilder.authNavHost(onNavigateToHome: () -> Unit) {
    composable(route = authBaseRoute) {
        val navController = rememberNavController()
        NavHost(navController = navController, startDestination = authBaseRoute) {
            authNavGraph(
                navController = navController,
                onNavigateToHome = onNavigateToHome
            )
        }
    }
}

private fun NavGraphBuilder.authNavGraph(
    navController: NavController,
    onNavigateToHome: () -> Unit
) {
    navigation(startDestination = emailScreen.route, route = authBaseRoute) {
        emailScreen(navController = navController, onNavigateToHome = onNavigateToHome)
        passwordScreen(navController = navController, onNavigateToHome = onNavigateToHome)
    }
}

fun NavController.navigateToAuth(navOptions: NavOptions? = null) {
    navigate(authBaseRoute, navOptions)
}

private fun NavController.navigateToPassword(email: String, navOptions: NavOptions? = null) {
    navigate(buildPasswordRoute(email), navOptions)
}

private fun NavGraphBuilder.emailScreen(
    modifier: Modifier = Modifier,
    navController: NavController,
    onNavigateToHome: () -> Unit
) {
    composable(route = emailScreen.route) {
        EmailRoute(
            modifier = modifier,
            onBackClick = onNavigateToHome,
            onNavigateToPassword = navController::navigateToPassword
        )
    }
}

private fun NavGraphBuilder.passwordScreen(
    modifier: Modifier = Modifier,
    navController: NavController,
    onNavigateToHome: () -> Unit
) {
    composable(
        route = passwordScreen.route,
        arguments = listOf(navArgument(emailArg) { type = NavType.StringType })
    ) {
        PasswordRoute(
            modifier = modifier,
            onBackClick = {
                navController.popBackStack()
            },
            onNavigateToHome = onNavigateToHome
        )
    }
}

private fun buildPasswordRoute(email: String): String {
    return passwordScreen.route.replace("{$emailArg}", email)
}